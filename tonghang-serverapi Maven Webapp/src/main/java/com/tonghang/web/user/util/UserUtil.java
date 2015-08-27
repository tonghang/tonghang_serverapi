package com.tonghang.web.user.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.SortUtil;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.friend.service.FriendService;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.location.pojo.Location;
import com.tonghang.web.location.service.LocationService;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Component("userUtil")
public class UserUtil {

	@Resource(name="userService")
	private UserService userService;
	@Resource(name="friendService")
	private FriendService friendService;
	@Resource(name="locationService")
	private LocationService locationService;
	/**
	 * 该包装方式目前已废弃
	 * @param users
	 * @return
	 */
	@Deprecated
	public Map<String,Object> usersToMapConvertor(List<User> users){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> usermap = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> result = new HashMap<String, Object>();
		for(User u:users){
			List<String> labels = new ArrayList<String>();
			Map<String,Object> msg = new HashMap<String, Object>();
			for(Label l:u.getLabellist()){
				labels.add(l.getLabel_name());
			}
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("city", u.getCity());
			msg.put("client_id", u.getClient_id());
			msg.put("image", Constant.IMAGE_PATH+u.getClient_id()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", TimeUtil.getFormatString(u.getCreated_at()));
			msg.put("birth", u.getBirth());
			usersmsg.add(msg);
		}
		
		usermap.put("users", usersmsg);
		result.put("success", usermap);
		return result;
	}
	
	/**
	 * 
	 * @param users(要包装的目标对象List)
	 * @param client_id(当前用户的client_id,用来判断List中每一个用户对象和当前用户是不是好友关系，关系用is_friend来表示)
	 * @return
	 *  新加了查看用户是不是给某用户发送了好友申请
	 */
	public Map<String,Object> usersToMapConvertor(List<User> users,String client_id){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> usermap = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> result = new HashMap<String, Object>();
		for(User u:users){
			List<String> labels = new ArrayList<String>();
			Map<String,Object> msg = new HashMap<String, Object>();
			for(Label l:u.getLabellist()){
				labels.add(l.getLabel_name());
			}
			String city = "";
			if(u.getProvince()==null||"".equals(u.getProvince()))
				city = "未知";
			else city = u.getCity()==null||"".equals(u.getCity())?u.getProvince():u.getProvince()+"-"+u.getCity();
			boolean is_friend = userService.isFriend(client_id, u.getClient_id());
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("city", city);
			msg.put("client_id", u.getClient_id());
			msg.put("image", Constant.IMAGE_PATH+u.getClient_id()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", u.getCreated_at());
			msg.put("birth", u.getBirth());
			msg.put("is_friend", is_friend);
			boolean has_invited = friendService.isInvited(client_id, u.getClient_id());
			if(!is_friend)
				msg.put("has_invitation", has_invited);
			else msg.put("has_invitation",!is_friend);
			usersmsg.add(msg);
		}
		usermap.put("users", usersmsg);
		result.put("success", usermap);
		return result;
	}
	
	public Map<String,Object> userToMapConvertor(User user,String client_id){
		List<String> labels = new ArrayList<String>();
		Map<String,Object> msg = new HashMap<String, Object>();
		Map<String,Object> usermap = new HashMap<String, Object>();
		boolean is_friend = userService.isFriend(client_id, user.getClient_id());
		if(user.getLabellist()!=null){
			for(Label l:user.getLabellist()){
				labels.add(l.getLabel_name());
			}
			msg.put("labels", labels);			
		}else{
			msg.put("labels", null);	
		}
		String city = "";
		if(user.getProvince()==null||"".equals(user.getProvince()))
			city = "未知";
		else city = user.getCity()==null||"".equals(user.getCity())?user.getProvince():user.getProvince()+"-"+user.getCity();
		msg.put("email", user.getEmail());
		msg.put("sex", user.getSex());
		msg.put("username", user.getUsername());
		msg.put("phone", user.getPhone());
		msg.put("city", city);
		msg.put("client_id", user.getClient_id());
		msg.put("image", Constant.IMAGE_PATH+user.getClient_id()+"/"+Constant.IMAGE_NAME);
		msg.put("created_at", user.getCreated_at());
		msg.put("birth", user.getBirth());
		msg.put("is_friend", is_friend);
		if(!is_friend)
			msg.put("has_invitation", friendService.isInvited(client_id, user.getClient_id()));
		else msg.put("has_invitation",!is_friend);
		usermap.put("user", msg);
		return usermap;
	}
	/**
	 * 和usersToMapConvertor方法功能一样，多一个按照距离排序的功能
	 * @param user
	 * @param client_id
	 * @return
	 */
	public Map<String,Object> usersToMapSortByDistanceConvertor(List<User> users,String client_id){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> usermap = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> result = new HashMap<String, Object>();
		Location my_local = locationService.findLocationByUser(userService.findUserById(client_id));
		for(User u:users){
			List<String> labels = new ArrayList<String>();
			Map<String,Object> msg = new HashMap<String, Object>();
			
			Location his_local = locationService.findLocationByUser(u);
			double distance = locationService.getDistanceByLocation(my_local, his_local);
			
			for(Label l:u.getLabellist()){
				labels.add(l.getLabel_name());
			}
			String city = "";
			if(u.getProvince()==null||"".equals(u.getProvince()))
				city = "未知";
			else city = u.getCity()==null||"".equals(u.getCity())?u.getProvince():u.getProvince()+"-"+u.getCity();
			boolean is_friend = userService.isFriend(client_id, u.getClient_id());
			msg.put("distance", distance);
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("city", city);
			msg.put("client_id", u.getClient_id());
			msg.put("image", Constant.IMAGE_PATH+u.getClient_id()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", u.getCreated_at());
			msg.put("birth", u.getBirth());
			msg.put("is_friend", is_friend);
			boolean has_invited = friendService.isInvited(client_id, u.getClient_id());
			if(!is_friend)
				msg.put("has_invitation", has_invited);
			else msg.put("has_invitation",!is_friend);
			usersmsg.add(msg);
		}
		//根据距离排序
		usersmsg = SortUtil.sortByDistance(usersmsg);
		usermap.put("users", usersmsg);
		result.put("success", usermap);
		return result;
	}
	/**
	 * 重载userToMapConvertor方法，ignore表示忽略好友关系（因为有可能已知对方肯定是或者不是好友关系）
	 * 新加了查看用户是不是给某用户发送了好友申请
	 * @param user
	 * @param ignore
	 * @return
	 */
	public Map<String,Object> userToMapConvertor(User user,boolean ignore,String client_id){
		List<String> labels = new ArrayList<String>();
		Map<String,Object> msg = new HashMap<String, Object>();
		Map<String,Object> usermap = new HashMap<String, Object>();
		if(user.getLabellist()!=null){
			for(Label l:user.getLabellist()){
				labels.add(l.getLabel_name());
			}
			msg.put("labels", labels);			
		}else{
			msg.put("labels", null);	
		}
		String city = "";
		if(user.getProvince()==null||"".equals(user.getProvince()))
			city = "未知";
		else city = user.getCity()==null||"".equals(user.getCity())?user.getProvince():user.getProvince()+"-"+user.getCity();
		msg.put("email", user.getEmail());
		msg.put("sex", user.getSex());
		msg.put("username", user.getUsername());
		msg.put("phone", user.getPhone());
		msg.put("client_id", user.getClient_id());
		msg.put("image", Constant.IMAGE_PATH+user.getClient_id()+"/"+Constant.IMAGE_NAME);
		msg.put("created_at", user.getCreated_at());
		msg.put("city", city);
		msg.put("birth", user.getBirth());
		msg.put("is_friend", ignore);
		if(!ignore)
			msg.put("has_invitation",friendService.isInvited(client_id, user.getClient_id()));
		else msg.put("has_invitation",!ignore);
		usermap.put("user", msg);
		return usermap;
	}
	/**
	 * 该方法是usersToMapConvertor(List<User> users,String client_id)方法的改良版，
	 * 核心作用相同，该方法多了一层按标签排序
	 * @param users
	 * @return
	 */
	public Map<String,Object> usersToMapSortedConvertor(List<User> users,User me){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> usermap = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> result = new HashMap<String, Object>();
		List<String> label_names = new ArrayList<String>();
		List<Boolean> is_same = new ArrayList<Boolean>();
		for(Label l:me.getLabellist()){
			label_names.add(l.getLabel_name());
		}
		for(User u:users){
			List<String> labels = new ArrayList<String>();
			Map<String,Object> msg = new HashMap<String, Object>();
			//比较当前用户哪些标签是根据使用者的标签被推出来的
			labels.addAll(markLabel(u, label_names));
			boolean is_friend = userService.isFriend(me.getClient_id(), u.getClient_id());
			//我的标签，送给前台和推荐的用户比对，相同的就标记高亮
			String city = "";
			if(u.getProvince()==null||"".equals(u.getProvince()))
				city = "未知";
			else city = u.getCity()==null||"".equals(u.getCity())?u.getProvince():u.getProvince()+"-"+u.getCity();
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("client_id", u.getClient_id());
			msg.put("image", Constant.IMAGE_PATH+u.getClient_id()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", u.getCreated_at());
			msg.put("birth", u.getBirth());
			msg.put("is_friend", is_friend);
			msg.put("city", city);
			msg.put("type", true);
			boolean has_invited = friendService.isInvited(me.getClient_id(), u.getClient_id());
			if(!is_friend)
				msg.put("has_invitation", has_invited);
			else msg.put("has_invitation",!is_friend);
			System.out.println("usersToMapConvertor: "+has_invited);
			usersmsg.add(msg);
		}
		//排序操作，详细请看 SortUtil 类
		usersmsg = SortUtil.sortByLabelName(usersmsg, label_names);
		usermap.put("users", usersmsg);
		result.put("success", usermap);
		return result;
	}
	/**
	 * 该方法是usersToMapSortedConvertor(List<User> users,String client_id)方法的改良版，
	 * 核心作用相同，除了有按行业排序，该方法多了一层按距离排序。现根据行业排序，然后根据距离排序
	 * @param users
	 * @return
	 * notice:在推荐的同行中如果有人没有location记录，则为其设置一个默认坐标
	 */
	public Map<String,Object> usersToMapSortedWithDistanceConvertor(List<User> users,User me){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> usermap = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> result = new HashMap<String, Object>();
		List<String> label_names = new ArrayList<String>();
		List<Boolean> is_same = new ArrayList<Boolean>();
		Location my_local = locationService.findLocationByUser(me);
		for(Label l:me.getLabellist()){
			label_names.add(l.getLabel_name());
		}
		for(User u:users){
			List<String> labels = new ArrayList<String>();
			Location his_local = locationService.findLocationByUser(u);
			double distance = locationService.getDistanceByLocation(my_local, his_local);
			Map<String,Object> msg = new HashMap<String, Object>();
			//比较当前用户哪些标签是根据使用者的标签被推出来的
			labels.addAll(markLabel(u, label_names));
			boolean is_friend = userService.isFriend(me.getClient_id(), u.getClient_id());
			//我的标签，送给前台和推荐的用户比对，相同的就标记高亮
			String city = "";
			if(u.getProvince()==null||"".equals(u.getProvince()))
				city = "未知";
			else city = u.getCity()==null||"".equals(u.getCity())?u.getProvince():u.getProvince()+"-"+u.getCity();
			msg.put("distance", distance);
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("client_id", u.getClient_id());
			msg.put("image", Constant.IMAGE_PATH+u.getClient_id()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", u.getCreated_at());
			msg.put("birth", u.getBirth());
			msg.put("is_friend", is_friend);
			msg.put("city", city);
			msg.put("type", true);
			boolean has_invited = friendService.isInvited(me.getClient_id(), u.getClient_id());
			if(!is_friend)
				msg.put("has_invitation", has_invited);
			else msg.put("has_invitation",!is_friend);
			System.out.println("usersToMapConvertor: "+has_invited);
			usersmsg.add(msg);
		}
		//排序操作，详细请看 SortUtil 类
		usersmsg = SortUtil.sortByLabelName(usersmsg, label_names);
		usersmsg = SortUtil.sortByDistance(usersmsg);
		usermap.put("users", usersmsg);
		result.put("success", usermap);
		return result;
	}
	
	public Map<String,Object> messageToMapConvertor(int code,String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("code",code);
		map.put("message",message);
		return map;
	}
	/**
	 * 
	 *业务功能：标记用户和目标标签列表有哪些是相同的 
	 * @param u
	 * @param label_names
	 * @return
	 */
	private List<String> markLabel(User u,List<String> label_names){
		List<String> labels = new ArrayList<String>();
		for(Label l:u.getLabellist()){
			boolean ismarked = false;
			for(String lab_name:label_names){
				if(l.getLabel_name().equalsIgnoreCase(lab_name)||l.getLabel_name().contains(lab_name)||lab_name.contains(l.getLabel_name())){
					ismarked = true;
				}	
			}
			if(ismarked){	
				labels.add(l.getLabel_name()+"\t\t");
			}else{
				labels.add(l.getLabel_name());							
			}
		}
		return labels;
	}
}
