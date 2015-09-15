package com.tonghang.web.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.tonghang.web.user.pojo.User;

/**
 * 业务逻辑中需要到环信的工具类
 * @author Administrator
 *
 */
public class HuanXinUtil {
	public static String HUANXINtoken;
	public static Integer duration;
	public static int count = 0;
	static {
		System.out.println("加载HUANXINUtil 类");
		getHuangXinServerToken();
		getTokenPeriodly();
	}
	
	public static void getTokenPeriodly(){
		Timer time = new Timer();
		time.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("获取一次环信token");
				getHuangXinServerToken();
				getTokenPeriodly();
			}
		},new Date(System.currentTimeMillis()+duration));

	}
	
	/**
	 * 获得环信管理员的TOKEN(定时更新尚未实现)
	 * @return
	 */
	private static void getHuangXinServerToken(){
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Content-Type","application/json");
		parts.put("grant_type", "client_credentials");
		parts.put("client_id", Constant.CLIENT_ID);
		parts.put("client_secret", Constant.CLIENT_SECRET);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		ResponseEntity<Map> response = DataUtil.postEntity(
							Constant.HUANXIN_URL+"token",requestEntity,Map.class);
		Map map = response.getBody();
		HUANXINtoken = (String) map.get("access_token");
		duration = (Integer)map.get("expires_in");
	}
	
	/**
	 * 业务功能：注册时给环信发请求
	 * @param user(注册时必须的用户信息都封装在这个User中)
	 */
	public static void registUser(User user){
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Content-Type","application/json");
		parts.put("username", user.getClient_id());
		parts.put("password", user.getPassword());
		parts.put("nickname", user.getUsername());
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		ResponseEntity<Map> result = DataUtil.postEntity(Constant.HUANXIN_URL+"users",requestEntity,Map.class);
	}
	
	/**
	 * 业务功能：修改昵称时给环信发请求
	 * @param username(要修改的用户昵称)
	 * @param huanxinusername(用户唯一标识client_id)
	 */
	public static void changeUsername(String username,String huanxinusername){
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization", "Bearer "+HUANXINtoken);
		parts.put("nickname", username);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		System.out.println(huanxinusername);
		DataUtil.putEntity(Constant.HUANXIN_URL+"users/"+huanxinusername.replaceAll("-", ""),requestEntity);
	}
	
	/**
	 * 业务功能：用户修改密码时给环信发请求
	 * @param password(要求改的密码)
	 * @param username(用户唯一标识client_id)
	 */
	public static void changePassword(String password,String username){
		HttpHeaders header = new HttpHeaders();
		System.out.println("username:"+username);
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization","Bearer "+HUANXINtoken);
		parts.put("newpassword", password);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		DataUtil.putEntity(Constant.HUANXIN_URL+"users/"+username.replaceAll("-", "")+"/password",requestEntity);
	}
	
	/**
	 * 业务功能：用户好友关系管理给环信发请求（环信API中 POST加好友DELETE删好友）
	 * @param my(使用者的client_id)
	 * @param friend(使用者要加或者要删除的好友的client_id)
	 * @param httpmethod(httpMethod是http请求的发送方法，默认有两个POST,DELETE。POST是加好友DELETE是删好友)
	 */
	public static void operateFriends(String my,String friend,String httpmethod){
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization","Bearer "+HUANXINtoken);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		if ("POST".equalsIgnoreCase(httpmethod)) {
			DataUtil.postEntity(Constant.HUANXIN_URL+"users/"+my+"/contacts/users/"+friend,requestEntity, Map.class);
		}else{
			DataUtil.deleteEntity(Constant.HUANXIN_URL+"users/"+my+"/contacts/users/"+friend);
		}
	}
	
	/**
	 * 业务功能：在环信中创建群组（创建话题）时给环信发请求
	 * @param groupname(群组的关键字subject)
	 * @param owner(群组创建者的client_id)
	 * @return 环信会为新创建的群组设置一个huanxin_group_id,这个ID将返回给后台并存在数据库中作为群组唯一标识
	 */
	public static String createGroup(String groupname,String owner){
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization","Bearer "+HUANXINtoken);
		System.out.println("!!!!!!!!:     " + HUANXINtoken);
		parts.put("groupname", groupname);
		parts.put("desc", Constant.CREATEGROUP);
		parts.put("public", true);
		parts.put("maxusers", 100);
		parts.put("approval", true);
		parts.put("allowinvites", true);
		parts.put("owner", owner);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		ResponseEntity<Map> result = DataUtil.postEntity(Constant.HUANXIN_URL+"chatgroups",requestEntity,Map.class);
		Map<String,Map> msg = result.getBody();
		LogUtil.printLog("环信返回的group_id:"+(String) msg.get("data").get("groupid"));
		return (String) msg.get("data").get("groupid");
	}
	
		/**
	 * 业务功能：切换某话题的群主
	 * @param oldowner
	 * notice:由于群主无法退出话题，现在只有利用一个账号去替换这个群主来达到让群主退出话题的目的
	 */
	public static void chanegOwner(String newowner,String group_id){
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization","Bearer "+HUANXINtoken);
		System.out.println("huanxin_token:  "+HUANXINtoken);
		parts.put("newowner", newowner);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		DataUtil.putEntity(Constant.HUANXIN_URL+"chatgroups/"+group_id,requestEntity);
	}
	/**
	 * 业务功能：删除话题
	 * @param huanxin_group_id
	 */
	public static void deleteTopic(String huanxin_group_id){
		HttpHeaders header = new HttpHeaders();
		header.add("Authorization","Bearer "+HUANXINtoken);
		System.out.println("deleteTopic:　　"+HUANXINtoken);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(header);
		DataUtil.deleteForEntity(Constant.HUANXIN_URL+"chatgroups/"+huanxin_group_id,requestEntity);
	}

	/**
	 * 业务功能：
	 * @param owner
	 * @param blocker
	 */
	public static void blockUser(String owner,String blocker){
		HttpHeaders header = new HttpHeaders();
		List<String> blockerlist = new ArrayList<String>();
		blockerlist.add(blocker);
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization","Bearer "+HUANXINtoken);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		parts.put("usernames", blockerlist);
		DataUtil.postEntity(Constant.HUANXIN_URL+"users/"+owner+"/blocks/users",requestEntity, Map.class);
	}
	/**
	 * 业务功能：从黑名单中删除某用户
	 * @param owner
	 * @param blocker
	 */
	public static void deblockUser(String owner,String blocker){
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization","Bearer "+HUANXINtoken);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		DataUtil.deleteForEntity(Constant.HUANXIN_URL+"users/"+owner+"/blocks/users/"+blocker,requestEntity);
	}
}
