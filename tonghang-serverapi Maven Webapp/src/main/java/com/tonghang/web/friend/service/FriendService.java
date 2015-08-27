package com.tonghang.web.friend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.HuanXinUtil;
import com.tonghang.web.common.util.JPushUtil;
import com.tonghang.web.friend.dao.FriendDao;
import com.tonghang.web.friend.pojo.Invitation;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.util.UserUtil;

@Service("friendService")
public class FriendService {
	
	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="friendDao")
	private FriendDao friendDao;
	@Resource(name="userUtil")
	private UserUtil userUtil;
	/**
	 * 业务功能：用户获得自己的好友列表
	 * @param client_id	前端请求中的参数	表示用户唯一标识client_id
	 * @return 
	 */
	public Map<String,Object> findFriendByUserId(String client_id){
		User user  = userDao.findUserById(client_id);
		Set<User> friends = user.getFriends();
		List<User> userList = new ArrayList<User>();
		userList.addAll(friends);
		return userUtil.usersToMapConvertor(userList,client_id);
	}
	
	/**
	 * 申请添加好友
	 * @param inviter_id 前端请求中的参数	添加者的唯一标识
	 * @param invitee_id 前端请求中的参数	被添加者的唯一标识
	 * @param reason	前端请求中的参数	添加理由
	 * @return
	 */
	public Map<String,Object> createInvitation(String inviter_id,String invitee_id,String reason){
		Invitation invitation = new Invitation();
		User inviter = userDao.findUserById(inviter_id);
		User invitee = userDao.findUserById(invitee_id);
		invitation.setInvitee(invitee);
		invitation.setInviter(inviter);
		invitation.setReason(reason);
		friendDao.saveInvitation(invitation);
		JPushUtil.push(invitee_id, inviter_id,inviter.getUsername(),Constant.INVITATION,Constant.INVITATION_MSG);
		Map<String,Object>  result = new HashMap<String, Object>();
		result.put("success", CommonMapUtil.baseMsgToMapConvertor());
		return result;
	}
	
	/**
	 * 删除好友申请(调试通过)
	 * @param inviter_id 前端请求中的参数	添加者的唯一标识
	 * @param invitee_id 前端请求中的参数	被添加者的唯一标识
	 * @return
	 */
	public Map<String,Object> deleteInvitation(String inviter_id,String invitee_id){
		Invitation invitation = new Invitation();
		User inviter = userDao.findUserById(inviter_id);
		User invitee = userDao.findUserById(invitee_id);
		invitation.setInvitee(invitee);
		invitation.setInviter(inviter);
		friendDao.deleteInvitation(invitation);
		Map<String,Object>  result = new HashMap<String, Object>();
		result.put("success", CommonMapUtil.baseMsgToMapConvertor());
		return result;
	}
	
	/**
	 * 添加指定好友
	 * @param my_id 	前端请求中的参数	使用者的唯一标识
	 * @param friend_id前端请求中的参数	好友的唯一标识
	 */
	public void addFriend(User my,User friend){
		if(!friendDao.isFriend(my.getClient_id(), friend.getClient_id())){
			userDao.addFriend(friend, my);
			userDao.addFriend(my, friend);
			HuanXinUtil.operateFriends(my.getClient_id(), friend.getClient_id(), "POST");
			HuanXinUtil.operateFriends(friend.getClient_id(), my.getClient_id(), "POST");
		}
	}

	
	/**
	 * 获得好友申请列表(调试成功)
	 * @param client_id 前端请求中的参数	使用者的唯一标识
	 * @return
	 * 在申请列表中的人肯定不是好友
	 * 注意getInvitationByUserId方法是传入被添加者的id查询（因为调用该方法的一般是被添加者）
	 */
	public Map<String,Object> getInvitationList(String client_id){
		List<Invitation> invitations = friendDao.getInvitationByUserId(client_id);
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String,Object> invitmsg = new HashMap<String,Object>();
		List<Map<String,Object>> inviters = new ArrayList<Map<String,Object>>();
		Iterator<Invitation> it = invitations.iterator();
		while(it.hasNext()){
			Invitation invitation = it.next();
			invitmsg.put("invitor", userUtil.userToMapConvertor(invitation.getInviter(),false,client_id).get("user"));
			invitmsg.put("reason", invitation.getReason());
			invitmsg.put("created_at", invitation.getCreated_at());
			invitmsg.put("invitee_id", invitation.getInvitee().getClient_id());
			invitmsg.put("inviter_id", invitation.getInviter().getClient_id());
			inviters.add(invitmsg);
		}
		result.put("success", CommonMapUtil.baseMsgToMapConvertor("invitation", inviters));
		return result;
	}
	
	/**
	 * 删除指定好友(调试成功)
	 * @param client_id	前端请求中的参数	使用者的唯一标识
	 * @param friend_id前端请求中的参数	被删好友的唯一标识
	 * @return
	 */
	public Map<String,Object> deleteFriend(String client_id,String friend_id){
		Map<String,Object>  result = new HashMap<String, Object>();
		result.put("success", CommonMapUtil.baseMsgToMapConvertor());
		User my = userDao.findUserById(client_id);
		User friend = userDao.findUserById(friend_id);
		userDao.deleteFriend(my, friend);
		userDao.deleteFriend(friend, my);
//		HuanXinUtil.operateFriends(my.getClient_id(), friend.getClient_id(), "DELETE");
//		HuanXinUtil.operateFriends(friend.getClient_id(), my.getClient_id(), "DELETE");
		return result;
	}

	/**
	 * 是否被邀请
	 * @param inviter_id	邀请人的client_id
	 * @param invitee_id	被邀请人的client_id
	 * @return
	 */
	public boolean isInvited(String inviter_id, String invitee_id) {
		// TODO Auto-generated method stub
		List<Invitation> invitations = friendDao.getInvitationByUserId(invitee_id);
		for(Invitation invitation:invitations){
			if(inviter_id.equals(invitation.getInviter().getClient_id()))
				return true;
		}
		return false;
	}
	
	
}
