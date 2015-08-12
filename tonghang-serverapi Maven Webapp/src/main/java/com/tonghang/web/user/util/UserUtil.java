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
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Component("userUtil")
public class UserUtil {

	@Resource(name="userService")
	private UserService userService;
	@Resource(name="friendService")
	private FriendService friendService;
	
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
			boolean is_friend = userService.isFriend(client_id, u.getClient_id());
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("city", u.getCity());
			msg.put("client_id", u.getClient_id());
			msg.put("image", Constant.IMAGE_PATH+u.getClient_id()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", u.getCreated_at());
			msg.put("birth", u.getBirth());
			msg.put("is_friend", is_friend);
			boolean has_invited = friendService.isInvited(client_id, u.getClient_id());
			if(!is_friend)
				msg.put("has_invitation", has_invited);
			else msg.put("has_invitation",!is_friend);
			System.out.println("usersToMapConvertor: "+has_invited);
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
		for(Label l:user.getLabellist()){
			labels.add(l.getLabel_name());
		}
		msg.put("labels", labels);
		msg.put("email", user.getEmail());
		msg.put("sex", user.getSex());
		msg.put("username", user.getUsername());
		msg.put("phone", user.getPhone());
		msg.put("city", user.getCity());
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
		for(Label l:user.getLabellist()){
			labels.add(l.getLabel_name());
		}
		msg.put("labels", labels);
		msg.put("email", user.getEmail());
		msg.put("sex", user.getSex());
		msg.put("username", user.getUsername());
		msg.put("phone", user.getPhone());
		msg.put("city", user.getCity());
		msg.put("client_id", user.getClient_id());
		msg.put("image", Constant.IMAGE_PATH+user.getClient_id()+"/"+Constant.IMAGE_NAME);
		msg.put("created_at", user.getCreated_at());
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
		for(Label l:me.getLabellist()){
			label_names.add(l.getLabel_name());
		}
		for(User u:users){
			List<String> labels = new ArrayList<String>();
			Map<String,Object> msg = new HashMap<String, Object>();
			for(Label l:u.getLabellist()){
				labels.add(l.getLabel_name());
			}
			boolean is_friend = userService.isFriend(me.getClient_id(), u.getClient_id());
			//我的标签，送给前台和推荐的用户比对，相同的就标记高亮
			msg.put("same_labels", label_names);
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("city", u.getCity());
			msg.put("client_id", u.getClient_id());
			msg.put("image", Constant.IMAGE_PATH+u.getClient_id()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", u.getCreated_at());
			msg.put("birth", u.getBirth());
			msg.put("is_friend", is_friend);
			msg.put("type", true);
			boolean has_invited = friendService.isInvited(me.getClient_id(), u.getClient_id());
			if(!is_friend)
				msg.put("has_invitation", has_invited);
			else msg.put("has_invitation",!is_friend);
			System.out.println("usersToMapConvertor: "+has_invited);
			usersmsg.add(msg);
		}
		//排序操作，详细请看 SortUtil 类
//		usersmsg = SortUtil.sortByLabelName(usersmsg, label_names);
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
}
