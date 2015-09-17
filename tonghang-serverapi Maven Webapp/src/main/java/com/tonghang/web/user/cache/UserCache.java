package com.tonghang.web.user.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.easemob.chat.EMChatService;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.HuanXinUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.label.dao.LabelDao;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.util.UserUtil;

@Component("userCache")
public class UserCache {

	@Resource(name="userUtil")
	private UserUtil userUtil;
	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="labelDao")
	private LabelDao labelDao;
	
	/**
	 * 业务功能：缓存UserService类中 getRecommendCache 方法的值，全部缓存。外部通过分页截取部分缓存结果
	 * @param client_id
	 * @param byDistance
	 * @return
	 * notice: value是缓存的名字，key是指定的缓存下的键名
	 */
	@Cacheable(value="com.tonghang.web.user.cache.UserCache.getRecommendCache",key="#client_id+#byDistance")
	public List<Map<String,Object>> getRecommendCache(String client_id,boolean byDistance){
		System.out.println("进入缓存方法：getByPage");
		List<User> users = new ArrayList<User>();
		Set<User> userss = new HashSet<User>();
		List<String> label_names = new ArrayList<String>();
		long begin = System.currentTimeMillis();
		System.out.println("### 走数据库开始："+begin);
		User user = userDao.findUserById(client_id);
		Set<Label> labels = user.getLabellist();
		for(Label label : labels){
			List<User> us = userDao.findUserByLabel(label.getLabel_name(), 0);
			if(us.contains(user)){
				us.remove(user);
			}
			label_names.add(label.getLabel_name());
			userss.addAll(us);
		}
		//按日期倒序 一次取出一个用户，去重复 不包括自己 取满100人
		System.out.println("###走数据库总耗时："+(System.currentTimeMillis()-begin));
		users.addAll(userss);
		Map<String,Object> result = byDistance?userUtil.usersToMapSortedWithDistanceConvertor(users, user):userUtil.usersToMapSortedConvertor(users,user);
		Map<String,Object> success = (Map<String, Object>) result.get("success");
		List<Map<String,Object>> us = (List<Map<String, Object>>) success.get("users");
		List<User> usrs = userDao.findOneUserByCreatedAtDesc(0,100);
		for(User u:usrs){
			System.out.println(us.contains(u)+"  非100人姓名："+u);
			if(!(us.contains(u)||u.equals(user))){
				Map<String,Object> map = userUtil.userToMapConvertor(u, client_id);
				us.add(map);
			}
			if(us.size()>=100){
				break;				
			}
		}
		return us;
	}
	
	/**
	 * 业务功能：缓存UserService类中 searchLabel 方法的值，全部缓存。外部通过分页截取部分缓存结果
	 * @param client_id
	 * @param label_name
	 * @param byDistance
	 * @return
	 */
	@Cacheable(value="com.tonghang.web.user.cache.UserCache.getSearchLabelCache",key="#client_id+#byDistance")
	public List<Map<String,Object>> getSearchLabelCache(String client_id,String label_name,boolean  byDistance){
		List<Label> labels = labelDao.findLabelByName(label_name);
		Set<User> userss = new HashSet<User>(); 
		List<User> users = new ArrayList<User>();
		for(Label label : labels){
			List<User> us = userDao.findUserByLabel(label.getLabel_name(), 0);
			userss.addAll(us);
		}
		users.addAll(userss);
		Map<String,Object> result = byDistance?userUtil.usersToMapSortByDistanceConvertor(users, client_id):userUtil.usersToMapConvertor(users,client_id);
		Map<String,Object> success = (Map<String, Object>) result.get("success");
		List<Map<String,Object>> us = (List<Map<String, Object>>) success.get("users");
		return us;
	}
	
	@Cacheable(value="com.tonghang.web.user.cache.UserCache.getSearchNickNameCache",key="#client_id+#byDistance")
	public List<Map<String,Object>> getSearchNickNameCache(String client_id,String username,boolean byDistance, int page){
		List<User> users = userDao.findUserByUsername(username, page);
		Map<String,Object> result = byDistance?userUtil.usersToMapSortByDistanceConvertor(users, client_id):userUtil.usersToMapConvertor(users,client_id);
		Map<String,Object> success = (Map<String, Object>) result.get("success");
		List<Map<String,Object>> us = (List<Map<String, Object>>) success.get("users");
		return us;
	}
	/**
	 * 业务功能：抽取出来的修改用户信息功能，修改信息时将搜索和首页推荐的缓存数据清空
	 * @param birth
	 * @param city
	 * @param sex
	 * @param username
	 * @param client_id
	 * @return
	 * notice:allEntries表示删除这个缓存的所有键对应的值（即清空缓存）
	 */
	@CacheEvict(value=
		{"com.tonghang.web.user.cache.UserCache.getSearchLabelCache",
		 "com.tonghang.web.user.cache.UserCache.getRecommendCache",
		 "com.tonghang.web.user.cache.UserCache.getSearchNickNameCache"
		},allEntries = true)
	public Map<String,Object> evictUpdateCache(String birth,String city,String sex,String username,String client_id){
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(client_id);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("更新失败，当前用户不存在", 513));
		}
		if(birth!=null&&!birth.equals(user.getBirth()))
			user.setBirth(birth);
		//修改省份前先清空省份
		if(city!=null){
			user.setProvince(null);
			user.setCity(null);
			if(city.contains("-")){
				String pr = StringUtil.seperate(city, 0);
				String ci = StringUtil.seperate(city, 1);
				if(!ci.equals(user.getCity())&&city!=null)
					user.setCity(ci);
				if(!pr.equals(user.getProvince())&&pr!=null)
					user.setProvince(pr);
			}else{
				user.setProvince(city);
			}
		}
		if(sex!=null&&!sex.equals(user.getSex()))
			user.setSex(sex);
		if(username!=null&&!username.equals(user.getUsername())){
			 if(userDao.findUserByUsernameUnique(username).size()!=0){
				result.put("success", CommonMapUtil.baseMsgToMapConvertor("该昵称已经被注册!", 512));
				return result;
			}else{
				user.setUsername(username);
				HuanXinUtil.changeUsername(user.getUsername(),user.getClient_id());				
			}
		}
		userDao.saveOrUpdate(user);
		Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
		result.put("success", usermap);
		return result;
	}
	
	@CacheEvict(value=
		{"com.tonghang.web.user.cache.UserCache.getSearchLabelCache",
		 "com.tonghang.web.user.cache.UserCache.getRecommendCache",
		 "com.tonghang.web.user.cache.UserCache.getSearchNickNameCache"
		},allEntries = true)
	public Map<String,Object> evictUpdateLabelCache(String client_id, List<String> list){
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(client_id);
		List<Label> labels = new ArrayList<Label>();
		userDao.deleteAllLabel(client_id);
		for(String label_name : list){
			Label label = labelDao.findLabelById(label_name);
			if(label==null){
				label = new Label();
				label.setLabel_name(label_name);
				labelDao.save(label);				
			}
			labels.add(label);
		}
		Set<Label> labellist = new HashSet<Label>();
		labellist.addAll(labels);
		user.setLabellist(labellist);
		userDao.saveOrUpdate(user);
		Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
		result.put("success", usermap);
		return result;
	}
	
}
