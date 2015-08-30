package com.tonghang.web.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.common.exception.BaseException;
import com.tonghang.web.common.exception.EmailExistException;
import com.tonghang.web.common.exception.LoginException;
import com.tonghang.web.common.exception.NickNameExistException;
import com.tonghang.web.common.exception.SearchNoResultException;
import com.tonghang.web.common.exception.UpdateUserException;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.EmailUtil;
import com.tonghang.web.common.util.HuanXinUtil;
import com.tonghang.web.common.util.JPushUtil;
import com.tonghang.web.common.util.SecurityUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.friend.dao.FriendDao;
import com.tonghang.web.label.dao.LabelDao;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.location.service.LocationService;
import com.tonghang.web.statistics.service.StatisticsService;
import com.tonghang.web.topic.dao.TopicDao;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.topic.util.TopicUtil;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.util.UserUtil;

@Service("userService")
public class UserService {
	
	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="labelDao")
	private LabelDao labelDao;
	@Resource(name="topicDao")
	private TopicDao topicDao;
	@Resource(name="friendDao")
	private FriendDao friendDao;
	@Resource(name="statisticsService")
	private StatisticsService statisticsService;
	@Resource(name="locationService")
	private LocationService locationService;
	@Resource(name="userUtil")
	private UserUtil userUtil;
	
	/**
	 * 用户登录
	 * @param 
	 * @return
	 * @throws Exception 
	 * 自己和自己肯定不是好友，调用带ignore参数的userToMapConvertor方法
	 */
	public Map<String,Object> login(String email,String password) throws BaseException{
		Map<String,Object> result = new HashMap<String, Object>();
		System.out.println("登录时的密码MD5加密后："+SecurityUtil.getMD5(password));
		User user = userDao.findUserByEmail(email);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，该邮箱不存在！", 510));
			return result;
		}else{
			if(user.getStatus().equals("0")){
				result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，用户被封号！", 510));
				return result;
			}else{
				//新需求需要密码MD5加密，此处用来兼容老用户
				if(user.getPassword().equals(SecurityUtil.getMD5(password))){
					Map<String,Object> usermap = userUtil.userToMapConvertor(user,false,user.getClient_id());
					usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
					result.put("success", usermap);
					user.setIsonline("1");
					userDao.saveOrUpdate(user);
				}else if(user.getPassword().equals(password)){
					Map<String,Object> usermap = userUtil.userToMapConvertor(user,false,user.getClient_id());
					usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
					result.put("success", usermap);
					user.setIsonline("1");
					user.setPassword(SecurityUtil.getMD5(password));
					HuanXinUtil.changePassword(SecurityUtil.getMD5(password), user.getClient_id());
					userDao.saveOrUpdate(user);
				}else{
					result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，用户名或密码错误！", 510));
					return result;
				}
			}
		}
		return result;
	}
	/**
	 * 业务功能:旧版本的APP登录通道
	 * @param email
	 * @param password
	 * @return
	 * @throws BaseException
	 */
	public Map<String,Object> oldLogin(String email,String password) throws BaseException{
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserByEmail(email);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，该邮箱不存在！", 510));
			return result;
		}else{
			if(user.getStatus().equals(0)){
				result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，用户被封号！", 510));
				return result;
			}else{
				if(user.getPassword().equals(password)){
					Map<String,Object> usermap = userUtil.userToMapConvertor(user,false,user.getClient_id());
					usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
					result.put("success", usermap);
					user.setIsonline("1");
					userDao.saveOrUpdate(user);
				}else{
					result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，用户名或密码错误！", 510));
					return result;
				}
			}
		}
		return result;
	}
	
	/**
	 * 找回密码
	 * @param 
	 * @return
	 * @throws EmailExistException 
	 * @throws LoginException 
	 * 
	 * notice: 2015-08-28 忘记密码的随机密码进行了MD5加密
	 */
	public Map<String,Object> forgetPassword(String email) throws LoginException{
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserByEmail(email);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("发送失败，该邮箱不存在！", 510));
			return result;
		}else{
			user.setPassword(StringUtil.randomCode(8));
//调修改密码方法
			EmailUtil.sendEmail(user);
			user.setPassword(SecurityUtil.getMD5(user.getPassword()));
			HuanXinUtil.changePassword(SecurityUtil.getMD5(user.getPassword()), user.getClient_id());
			userDao.saveOrUpdate(user);
			result.put("success", userUtil.messageToMapConvertor(200, "密码重置请求成功!"));
		}
		return result;
	}
	
	
	/**
	 * 业务功能：用户注册第一步
	 * @param user User对象(新注册的user对象)
	 * @return
	 * 自己和自己肯定不是好友，调用带ignore参数的userToMapConvertor方法
	 * @throws EmailExistException 
	 * 
	 * notice：第一步注册去掉了添加标签
	 * @throws NickNameExistException 
	 */
	public Map<String,Object> registUser(User user) throws EmailExistException, NickNameExistException{
		Map<String,Object> result = new HashMap<String, Object>();
		//去掉标签部分
//		Iterator<Label> it = user.getLabellist().iterator();
//		while(it.hasNext()){
//			Label label = it.next();
//			System.out.println("regist:"+label.getLabel_name());
//			if(labelDao.findLabelById(label.getLabel_name())==null)
//				labelDao.save(label);
//		}
		if(userDao.findUserByEmail(user.getEmail())!=null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("注册失败！该邮箱已被注册", 511));
			return result;
		}else if(userDao.findUserByNickName(user.getUsername())!=null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("注册失败！该昵称已经被注册", 512));
			return result;
		}else{
			user.setClient_id(SecurityUtil.getUUID());
			userDao.save(user);
			HuanXinUtil.registUser(user);
			Map<String,Object> usermap = userUtil.userToMapConvertor(user,false,user.getClient_id());
			usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
			result.put("success", usermap);
		}
		return result;
	}
	/**
	 *  旧的注册接口，因为注册业务换成三步注册，为了兼容0.8app留下该接口
	 * @param user
	 * @return
	 * @throws EmailExistException
	 * @throws NickNameExistException
	 */
	public Map<String,Object> oldRegistUser(User user) throws EmailExistException, NickNameExistException{
		Map<String,Object> result = new HashMap<String, Object>();
		//去掉标签部分
		Iterator<Label> it = user.getLabellist().iterator();
		while(it.hasNext()){
			Label label = it.next();
			if(labelDao.findLabelById(label.getLabel_name())==null)
				labelDao.save(label);
		}
		if(userDao.findUserByEmail(user.getEmail())!=null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("注册失败！该邮箱已被注册", 511));
			return result;
		}else/* if(userDao.findUserByNickName(user.getUsername())!=null){
			throw new NickNameExistException("注册失败！该昵称已经被注册");
		}else*/{
			user.setClient_id(SecurityUtil.getUUID());
			userDao.save(user);
			HuanXinUtil.registUser(user);
			Map<String,Object> usermap = userUtil.userToMapConvertor(user,false,user.getClient_id());
			usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
			result.put("success", usermap);
		}
		return result;
	}
	
	/**
	 * 查看用户详细信息
	 * @param client_id	前台请求中的参数	表示用户的唯一标识	client_id
	 * @return
	 * @throws SearchNoResultException 
	 */
	public Map<String,Object> checkUserMessage(String client_id) throws SearchNoResultException{
		Map<String,Object> result = new HashMap<String, Object>();
		User user =userDao.findUserById(client_id);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("未搜索到您想搜索的内容", 520));
			return result;
		}
		Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
		result.put("success", usermap);
		return result;
	}

	/**
	 * 判断两个人是不是好友
	 * @param my
	 * @param friend
	 * @return
	 */
	public boolean isFriend(String my,String friend){
		return friendDao.isFriend(my, friend);
	}
	/**
	 * 首页推荐
	 * @param 
	 * @return
	 * 使用Set<User> userss先进行包装是为了去掉多余的用户，比如java能推出A用户，android也能推出A用户。
	 * 此时需要用Set去除重复项。
	 * @throws SearchNoResultException 
	 * 
	 * 2015-8-11日新加入排序功能，详情请见SortUtil
	 * 2015-8-27日新加入 在标签排序基础上，按照距离排序功能
	 */
	public Map<String, Object> recommend(String client_id,boolean byDistance, int page){
		List<Map<String,Object>> sortlist = new ArrayList<Map<String,Object>>();
		Map<String,Object> result = new HashMap<String, Object>();
		List<User> users = new ArrayList<User>();
		Set<User> userss = new HashSet<User>();
		List<String> label_names = new ArrayList<String>();
		User user = userDao.findUserById(client_id);
		List<Label> labels = labelDao.findLabelByUser(user);
		for(Label label : labels){
			List<User> us = userDao.findUserByLabel(label.getLabel_name(), page);
			if(us.contains(user)){
				us.remove(user);
			}
			userss.addAll(us);
			//存放目标用户的标签，用来排序
			label_names.add(label.getLabel_name());
		}
		users.addAll(userss);
		if(users.size()==0||userss.size()==0&&page==1){
//			throw new SearchNoResultException("首页推荐没有结果");
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("首页推荐没有结果", 520));
			return result;
		}else if(userss.size()==0&&page>1){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("搜索不到更多了", 520));
			return result;
		}
//			throw new SearchNoResultException("搜索不到更多了");
		return byDistance?userUtil.usersToMapSortedWithDistanceConvertor(users, user):userUtil.usersToMapSortedConvertor(users,user);
	}

	/**
	 * 按标签搜索用户
	 * @param client_id
	 * @param label_name
	 * @param page
	 * @return
	 * @throws SearchNoResultException
	 * 标签搜索是模糊搜索，当搜索不到时，提示前台搜索不到更多，但是第一次搜索不到则提示没有搜索结果。
	 */
	public Map<String, Object> searchLabel(String client_id,String label_name, boolean byDistance,int page){
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		List<Label> labels = labelDao.findLabelByName(label_name);
		Set<User> userss = new HashSet<User>(); 
		List<User> users = new ArrayList<User>();
		for(Label label : labels){
			List<User> us = userDao.findUserByLabel(label.getLabel_name(), page);
			userss.addAll(us);
		}
		users.addAll(userss);
		//当page=1时userss.size()为0说明用户一开始就搜不到数据，
		//page>1时userss.size()为0说明用户刷新了数据，但是没有结果了
		if(userss.size()==0&&page==0){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("未搜索到您想搜索的内容", 520));
			return result;
		}else if(userss.size()==0&&page>1){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("搜索不到更多了", 520));
			return result;
		}
		return byDistance?userUtil.usersToMapSortByDistanceConvertor(users, client_id):userUtil.usersToMapConvertor(users,client_id);
	}
	
	/**
	 * 按昵称搜索用户
	 * @param client_id
	 * @param username
	 * @param page
	 * @return
	 * @throws SearchNoResultException
	 * 昵称模糊搜索，当搜索不到时，提示前台搜索不到更多，但是第一次搜索不到则提示没有搜索结果。
	 */
	public Map<String, Object> searchNick(String client_id,String username,boolean byDistance, int page){
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		List<User> users = userDao.findUserByUsername(username, page);
		if(users.size()==0&&page==0){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("未搜索到您想搜索的内容", 520));
			return result;
		}else if(users.size()==0&&page>1){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("搜索不到更多了", 520));
		}
		return byDistance?userUtil.usersToMapSortByDistanceConvertor(users, client_id):userUtil.usersToMapConvertor(users,client_id);
	}

	/**
	 * 按照id获取某个用户的所有信息
	 * @param obj_id
	 * @param client_id
	 * @return
	 */
	public Map<String, Object> userMessage(String obj_id,String client_id) {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(obj_id);
		System.out.println("查看某用户详细信息："+user.getLabellist());
		System.out.println("查看某用户详细信息："+user);
		Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
		result.put("success", usermap);
		return result;
	}

	/**
	 * 现在所有的error都放到success中，因为前台无法区分sucess和error
	 * @param client_id
	 * @param username
	 * @param sex
	 * @param birth
	 * @param city
	 * @return
	 * @throws LoginException 
	 * @throws UpdateUserException 
	 * @throws NickNameExistException 
	 * 
	 * notice:修改信息变成一个一个信息进行修改，所以这里逐个判断每个信息是不是空
	 */
	public Map<String, Object> update(String client_id, String username,
			String sex, String birth, String city){
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(client_id);
//		User u = userDao.findUserByNickName(username);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("更新失败，当前用户不存在", 513));
			return result;
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
			 if(userDao.findUserByNickName(user.getUsername())!=null){
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
	/**
	 * 修改密码
	 * @param client_id
	 * @param old_passwd
	 * @param new_passwd
	 * @return
	 * @throws UpdateUserException
	 * 修改密码操作会给环信发送修改请求，环信密码和自己服务器密码不一致会在发信的时候出问题
	 */
	public Map<String, Object> updatePassword(String client_id,String old_passwd, String new_passwd){
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = new User();
		user.setClient_id(client_id);
		user = userDao.findUserById(client_id);
		if(user.getPassword().equals(old_passwd)||user.getPassword().equals(SecurityUtil.getMD5(old_passwd))){
			user.setPassword(new_passwd);
			HuanXinUtil.changePassword(user.getPassword(), user.getClient_id());
			userDao.saveOrUpdate(user);
			user = userDao.findUserById(client_id);
			Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
			usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
			result.put("success", usermap);
		}else {
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("修改失败，原密码不正确", 513));
		}
		return result;
	}
	/**
	 * 业务功能：修改标签
	 * @param client_id
	 * @param list
	 * @return
	 */
	public Map<String, Object> updateLabel(String client_id, List<String> list) {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(client_id);
		System.out.println("修改人的ID:"+user);
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

	/***
	 * 业务功能：查看某用户的话题
	 * @param client_id
	 * @param page
	 * @return
	 * @throws SearchNoResultException
	 */
	public Map<String, Object> userTopic(String client_id, int page) throws SearchNoResultException {
		// TODO Auto-generated method stub
		List<Topic> topics = topicDao.findTopicByUserId(client_id, page);
		Map<String,Object> result = new HashMap<String, Object>();
		if(topics==null||topics.size()==0){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("该用户没有话题", 520));
			return result;
		}
		return TopicUtil.topicsToMapConvertor(topics);
	}
	/**
	 * 通过id查找某用户
	 * @param client_id
	 * @return
	 */
	public User findUserById(String client_id){
		return userDao.findUserById(client_id);
	}
	/**
	 * 2015-08-26日新增
	 * 
	 * 业务功能：新用户推荐给老用户
	 * @param client_id
	 * @return
	 * @throws SearchNoResultException
	 * 
	 * notice:逐个推送可能会有问题，最好一次推荐一群人而不是一群人一个个推
	 */
	public Map<String,Object> newUserRecommendation(String client_id) throws SearchNoResultException{
		User newuser = findUserById(client_id);
		Map<String,Object>  result = new HashMap<String, Object>();
		result.put("success", CommonMapUtil.baseMsgToMapConvertor());
		int index = 1;
		while(true){
			Map<String,Object> olders = recommend(client_id,false, index);
			Map<String,Object> oldersmap = ((Map<String,Object>)olders.get("success"));
			if(!oldersmap.get("code").equals("200"))
				break;
			List<Map<String,Object>> olderlist = (List<Map<String, Object>>) oldersmap.get("users");
			List<String> client_ids = new ArrayList<String>();
			for(Map<String,Object> older:olderlist){
				client_ids.add((String)older.get("client_id"));
			}
			JPushUtil.pushList(client_ids, client_id, newuser.getUsername(),Constant.RECOMMEND_NEWBE,newuser.getUsername()+Constant.NEWBE_MSG);
			index++;
		}
		return result;
	}
	/**
	 * 2015-08-27新增
	 * 业务功能：用户新增地理位置信息
	 * @param client_id		用户client_id
	 * @param x_point		纬度
	 * @param y_point		经度
	 */
	public void saveUsersLocation(String client_id,double x_point,double y_point){
		User user = findUserById(client_id);
		locationService.saveLocation(user, x_point, y_point);
	}
}
