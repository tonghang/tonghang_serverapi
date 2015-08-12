package com.tonghang.web.friend.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tonghang.web.common.util.JPushUtil;
import com.tonghang.web.friend.service.FriendService;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

/**
 * 处理friend相关请求
 * @author Administrator
 *
 */
@RestController("friendController")
@RequestMapping("friend")
public class FriendController {

	@Resource(name="friendService")
	private FriendService friendService;
	@Resource(name="userService")
	private UserService userService;
	/**
	 * 业务功能：查询用户的所有好友(调试通过)
	 * @param client_id rest请求中所携带的参数 表示用户的client_id
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(pageindex)
	 * @return user(List<Map>) [labels(List) email (String) image(String) 
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date) is_friend(boolean)]
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.findFriendByUserId()方法根据client_id查询当前用户所有的好友(查询client_id对应的friend_id)
	 */
	@RequestMapping("get/{client_id}")
	public ResponseEntity<Map<String,Object>> findFriendsList(@PathVariable String client_id) 
																		throws JsonParseException,JsonMappingException, IOException{
		Map<String,Object> friendmap = friendService.findFriendByUserId(client_id);
		return new ResponseEntity<Map<String,Object>>(friendmap,HttpStatus.OK); 
	}
	/**
	 * 业务功能：发送加好友请求(调试通过,极光推送失效)
	 * @param client_id client_id rest请求中所携带的参数 表示用户的client_id
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(invitee_id,reason)
	 * @return sys_time等基本参数
	 * @throws Exception
	 * 1.createInvitation()方法返回基本信息(sys_time等等)，通过被添加人id(invitee_id)添加invitation信息(一条invitation表示一个加好友记录)
	 * invitation信息可以是双向的，比如A加B，B加A，则可以在invitation信息中出现A-B和B-A
	 * 2.invitation信息中包括加好友原因reason
	 * 
	 */
	@RequestMapping("add/{client_id}")
	public ResponseEntity<Map<String,Object>> addFriend(@PathVariable String client_id,
									@RequestParam String mapstr) throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		return new ResponseEntity<Map<String,Object>>(friendService.createInvitation(client_id, (String)map.get("invitee_id"), (String)map.get("reason")),HttpStatus.OK);
	}
	/**
	 * 业务功能：同意或拒绝好友申请(调试通过  极光推送失效)
	 * @param status 布尔值true表示同意false表示拒绝
	 * @param inviter_id 表示申请者的client_id
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(invitee_id)
	 * @return sys_time等基本参数
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.判断是同意还是拒绝，并发送JPush通知请求发送者。同时在数据库中删除invitation记录，如果同意添加好友
	 * 则再friends表中插入好友关系。
	 * 2.若同意加好友，addFriend()方法 是同时插入A-B和B-A两个关系，并同时删除双向invitation关系。
	 */
	@RequestMapping("{status}/{inviter_id}")
	public ResponseEntity<Map<String,Object>> agreeOrRefuseRequest(@PathVariable boolean status,@PathVariable String inviter_id
																					,@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		String invitee_id = (String)map.get("invitee_id");
		User my = userService.findUserById(invitee_id);
		User friend = userService.findUserById(inviter_id);
		if(status){
			friendService.addFriend(my, friend);
			JPushUtil.push(inviter_id+"", my.getUsername()+"已经同意您的好友请求");
		}else
			JPushUtil.push(inviter_id+"", my.getUsername()+"已经拒绝您的好友请求");
		Map<String,Object> result = friendService.deleteInvitation(inviter_id,invitee_id);
		friendService.deleteInvitation(invitee_id,inviter_id);
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}
	/**
	 * 业务功能：获得自己的好友申请列表(新朋友)(调试通过)
	 * @param client_id rest请求中所携带的参数 表示用户的client_id
	 * @return inviter(List<Map>)[ labels(List) email (String) image(String) 
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date)] ,
	 * 				created_at(Date),
	 *		       reason(String),
	 *		       invitee_id(String),
	 *		       inviter_id(String)
	 * 1.getInvitationList()查找某个用户的所有邀请列表，将邀请信息封装到Map中并放入List返回给前台，并由ResponseEntity包装成JSON返回给前台
	 */
	@RequestMapping("invitation/{client_id}")
	public ResponseEntity<Map<String,Object>> getInvitation(@PathVariable String client_id){
		Map<String,Object> invitationmap = friendService.getInvitationList(client_id);
		return new ResponseEntity<Map<String,Object>>(invitationmap,HttpStatus.OK);
	}
	
	/**
	 * 业务功能：删除好友(调试通过)
	 * @param client_id rest请求中所携带的参数 表示用户的client_id
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(friend_id)
	 * @return sys_time等基本参数
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.deleteFriend()时在friends表中是双向删除。
	 */
	@RequestMapping("delete/{client_id}")
	public ResponseEntity<Map<String,Object>> deleteFriend(@PathVariable String client_id,@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = friendService.deleteFriend(client_id, (String)map.get("friend_id"));
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}
	
}
