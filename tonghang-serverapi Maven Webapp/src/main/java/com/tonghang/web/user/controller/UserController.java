package com.tonghang.web.user.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tonghang.web.common.controller.BaseController;
import com.tonghang.web.common.exception.EmailExistException;
import com.tonghang.web.common.exception.LoginException;
import com.tonghang.web.common.exception.NickNameExistException;
import com.tonghang.web.common.exception.SearchNoResultException;
import com.tonghang.web.common.exception.UpdateUserException;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.RequestUtil;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.dao.impl.UserDaoImpl;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

/**
 * 处理一切有关用户模块的请求
 * 继承BaseController处理所有异常
 * @author Administrator
 *
 */
@RestController("userController")
@RequestMapping("/user")
public class UserController extends BaseController{
	
	@Resource(name="userService")
	private UserService userService;
	
	
	/**
	 * 业务功能：用户登录（调试通过）
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(email,password)
	 * @return user(Map) [labels(List) email (String) image(String) 
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date)]
	 * @throws Exception
	 * 1.jackson类库ObjectMapper类  readVlaue()方法 参数一为json格式的字符串，转换结果的类型
	 * 2.login()方法返回的map集合包括前端所需要的所有数据，并由ResponseEntity包装成JSON返回给前台
	 * 3.所有返回用户信息的地方都会返回是否是好友关系
	 */
	@RequestMapping(value = "/login")
	public ResponseEntity<Map<String,Object>> login(@RequestParam String mapstr) throws Exception {		
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		return new ResponseEntity<Map<String,Object>>(userService.login((String)map.get("email"),(String)map.get("password")), HttpStatus.OK);
	}
	
	/**
	 * 业务功能：忘记密码(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(email,password)
	 * @return code(int) message(String)
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.jackson类库ObjectMapper类  readVlaue()方法 参数一为json格式的字符串，转换结果的类型
	 * 2.forgetPassword()方法执行密码重置,先校验用户，成功后发送邮箱并返回相应的code(码)和message(信息),
	 * 失败也会返回相应的code和message,并由ResponseEntity包装成JSON返回给前台
	 * 3.所有返回用户信息的地方都会返回是否是好友关系
	 * @throws LoginException 
	 */
	@RequestMapping(value = "/forget_password")
	public ResponseEntity<Map<String,Object>> forgetPassword(@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException, LoginException {
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		return new ResponseEntity<Map<String,Object>>(userService.forgetPassword((String)map.get("email")), HttpStatus.OK);
	}
	
	/**
	 * 业务功能：用户注册必要信息(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(username,password,email,labels)
	 * @return user(Map)[ labels(List) email (String) image(String) 
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date)]
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.jackson类库ObjectMapper类  readVlaue()方法 参数一为json格式的字符串，转换结果的类型
	 * 新建user后设置user的状态为在线(user.setStatus("1")),设置账号状态为正常(user.setIsonline("0")),
	 * 并把请求参数全部设置到user对象中。
	 * 2.regist()方法会检查新的用户所包含的标签信息在数据库中是否存在，若存在则直接插入新的用户；
	 * 反之则先插入新的标签，然后插入新的用户。返回值包装了新user的相关信息，并由ResponseEntity包装成JSON返回给前台
	 * 3.所有返回用户信息的地方都会返回是否是好友关系
	 * @throws EmailExistException 
	 * @throws NickNameExistException 
	 */
	@RequestMapping(value = "/regist")
	public ResponseEntity<Map<String,Object>> registUser(@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException, EmailExistException, NickNameExistException {
//		AccountBean acc = objectMapper.readValue(json, AccountBean.class);
		System.out.println("开始注册");
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		User user = new User();
		String username = (String)map.get("username");
		user.setUsername(username);
		user.setPassword((String)map.get("password"));
		user.setEmail((String)map.get("email"));
		user.setIsonline("0");
		user.setStatus("1");
		return new ResponseEntity<Map<String,Object>>(userService.registUser(user), HttpStatus.OK);
	}
	
	/**
	 * 2015-08-28新增按距离推荐,新增字段 byDistance,是否需要按照距离排序
	 * 
	 * 业务功能: 用户首页推荐(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(client_id,pageindex)
	 * @return user(Map)[ labels(List) email (String) image(String) 
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date) is_friend(boolean)]
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.jackson类库ObjectMapper类  readVlaue()方法 参数一为json格式的字符串，转换结果的类型
	 * 2.recommand方法会按照client_id找到当前用户，获得其包含的所有标签，然后按模糊匹配原则获
	 * 得所有相关的推荐人，封装成map格式,并由ResponseEntity包装成JSON返回给前台
	 * 3.返回值为列表的请求全部需要分页，客户端需要传一个pageindex表示当前页数
	 * 4.所有返回用户信息的地方都会返回是否是好友关系
	 * @throws SearchNoResultException 
	 * 
	 * 
	 */
	@RequestMapping(value = "/recommend")
	public ResponseEntity<Map<String,Object>> recommend(@RequestParam String mapstr) 
								throws JsonParseException, JsonMappingException, IOException, SearchNoResultException {
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		String client_id = (String)map.get("client_id");
		boolean byDistance = false;
		if(map.get("byDistance")!=null)
			byDistance = (Boolean)map.get("byDistance");
		return new ResponseEntity<Map<String,Object>>(userService.recommend(client_id,byDistance,(Integer)map.get("pageindex")), HttpStatus.OK);
	}

	/**
	 * 2015-08-28新增按距离推荐,新增字段 byDistance,是否需要按照距离排序
	 * 
	 * 业务功能: 通过标签查询用户(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中
	 * @return user(List<Map>)[ labels(List) email (String) image(String) 
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date) is_friend(boolean)]
	 * @throws Exception
	 * 1.jackson类库ObjectMapper类  readVlaue()方法 参数一为json格式的字符串，转换结果的类型
	 * searchLabel()方法按标签名 模糊匹配 得到的标签对象，再按照这些标签名模糊查找对应的用户，用户信息包装成map,
	 * 然后包装成一个大的List,并由ResponseEntity包装成JSON返回给前台
	 * 2.返回值为列表的请求全部需要分页，客户端需要传一个pageindex表示当前页数
	 */
	@RequestMapping(value = "/search/label")
	public ResponseEntity<Map<String,Object>> searchLabel(@RequestParam String mapstr) throws Exception {
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		String client_id = (String)map.get("client_id");
		boolean byDistance = (Boolean)map.get("byDistance");
		return new ResponseEntity<Map<String,Object>>(userService.searchLabel(client_id,(String)map.get("label_name"),byDistance,(Integer)map.get("pageindex")), HttpStatus.OK);
	}
	
	/**
	 * 2015-08-28新增按距离推荐,新增字段 byDistance,是否需要按照距离排序
	 * 
	 * 业务功能：通过昵称查询用户(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(client_id,username)
	 * @return user(List<Map>)[ labels(List) email (String) image(String) 
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date) is_friend(boolean)]
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.jackson类库ObjectMapper类  readVlaue()方法 参数一为json格式的字符串，转换结果的类型
	 * 2.searchNick()方法按照用户名模糊匹配用户信息，将得到的用户结果封装成Map,然后包装成一个大的List,
	 * 并由ResponseEntity包装成JSON返回给前台
	 * 3.返回值为列表的请求全部需要分页，客户端需要传一个pageindex表示当前页数
	 * 4.所有返回用户信息的地方都会返回是否是好友关系
	 * @throws SearchNoResultException 
	 */
	@RequestMapping(value = "search/nick")
	public ResponseEntity<Map<String,Object>> searchNick(@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException, SearchNoResultException {
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		String client_id = (String)map.get("client_id");
		boolean byDistance = (Boolean)map.get("byDistance");
		return new ResponseEntity<Map<String,Object>>(userService.searchNick(client_id,(String)map.get("username"),byDistance,(Integer)map.get("pageindex")), HttpStatus.OK);
	}
	
	/**
	 * 业务功能：查看用户信息（该请求暂时没找到对应的业务）
	 * @param client_id rest请求中所携带的参数 表示用户的client_id
	 * @return user(Map) [labels(List) email (String) image(String)
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date) is_friend(boolean)]
	 * 
	 * 1.userMessage()方法通过client_id精确查找到一个User,并将这个User的详细信息包装成Map,并由ResponseEntity包装成JSON返回给前台
	 * 2.返回值为列表的请求全部需要分页，客户端需要传一个pageindex表示当前页数
	 * 3.所有返回用户信息的地方都会返回是否是好友关系
	 */
	@RequestMapping(value = "/{obj_id}")
	public ResponseEntity<Map<String,Object>> userMessage(@PathVariable String obj_id,@RequestParam String client_id) {
		return new ResponseEntity<Map<String,Object>>(userService.userMessage(obj_id,client_id), HttpStatus.OK);
	}
	
	/**
	 * 业务功能：修改用户信息(除了文件上传其他的调试通过)
	 * @param request 
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(image,username,sex,birth,city)
	 * @param client_id rest请求中所携带的参数 表示用户的client_id
	 * @return user(Map) [labels(List) email (String) image(String) 
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date) is_friend(boolean)]
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.先获得用户上传来的头像，不为空则存放在服务器指定的位置，然后根据参数为用户设置详细信息。
	 * 2.update方法修改用户信息，如果修改的是用户名则还需要向环信提交请求。用户不存在则返回错误信息。
	 * 用户存在与否都会返回当前用户信息，并由ResponseEntity包装成JSON返回给前台。
	 * 3.所有返回用户信息的地方都会返回是否是好友关系
	 * @throws UpdateUserException 
	 * @throws NickNameExistException 
	 */
	@RequestMapping(value = "/update/{client_id}")
	public ResponseEntity<Map<String,Object>> update(HttpServletRequest request,@RequestParam(required=false) CommonsMultipartFile image,@RequestParam String mapstr,@PathVariable String client_id) throws JsonParseException, JsonMappingException, IOException, UpdateUserException, NickNameExistException {
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		RequestUtil.UserImageReceiver(request, client_id, image);
		System.out.println("username:"+(String)map.get("username")+" sex:"+(String)map.get("sex")+" birth:"+(String)map.get("birth")+" city:"+(String)map.get("city"));
		return new ResponseEntity<Map<String,Object>>(userService.update(client_id,(String)map.get("username"),(String)map.get("sex"),(String)map.get("birth"),(String)map.get("city")), HttpStatus.OK);
	}
	
	/**
	 * 业务功能：修改密码(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(old_password,new_password)
	 * @param client_id rest请求中所携带的参数 表示用户的client_id
	 * @return user(Map) [labels(List) email (String) image(String) 
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date) is_friend(boolean)]
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.updatePassword()方法首先根据client_id查询到指定用户，然后判断旧密码和用户当前密码是否一样。
	 * 一样则修改密码，否则则返回错误码，错误和正确都将返回当前用户的全部信息，信息存放在map中，并由ResponseEntity包装成JSON返回给前台
	 * 2.所有返回用户信息的地方都会返回是否是好友关系
	 * @throws UpdateUserException 
	 */
	@RequestMapping(value = "/update_pwd/{client_id}")
	public ResponseEntity<Map<String,Object>> updatePassword(@RequestParam String mapstr,@PathVariable String client_id) throws JsonParseException, JsonMappingException, IOException, UpdateUserException {		
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		return new ResponseEntity<Map<String,Object>>(userService.updatePassword(client_id,(String)map.get("old_passwd"),(String)map.get("new_passwd")), HttpStatus.OK);
	}
	
	/**
	 * 业务功能：修改标签(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(label_name)
	 * @param client_id rest请求中所携带的参数 表示用户的client_id
	 * @return user(Map)[ labels(List) email (String) image(String)
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date) birth(Date) is_friend(boolean)]
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.updateLabel()方法找到当前用户后，按标签名更新,如果系统中不包括新修改的标签，
	 * 则先添加新标签，然后给用户添加新标签。用户信息包装在Map中，并由ResponseEntity包装成JSON返回给前台
	 * 2.所有返回用户信息的地方都会返回是否是好友关系
	 * 
	 * notice:该方法在0.2snapshot中也用做第二步注册标签
	 */
	@RequestMapping(value = "/update_label/{client_id}")
	public ResponseEntity<Map<String,Object>> updateLabel(@RequestParam String mapstr,@PathVariable String client_id) throws JsonParseException, JsonMappingException, IOException {
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		System.out.println("修改完的标签信息："+(List<String>)map.get("label_name"));
		return new ResponseEntity<Map<String,Object>>(userService.updateLabel(client_id,(List<String>)map.get("label_name")), HttpStatus.OK);
	}
	
	/**
	 * 业务功能：查看用户话题(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(client_id,pageindex)
	 * @return topics(List<Map>)[ topic_id(String) subjct(String) label_name(String) user_id(String) image(String)]
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.userTopic()方法通过用户client_id查找到其包含的话题，将信息包装成map，并由ResponseEntity包装成JSON返回给前台
	 * 2.所有返回用户信息的地方都会返回是否是好友关系
	 * @throws SearchNoResultException 
	 */
	@RequestMapping(value = "/topic")
	public ResponseEntity<Map<String,Object>> userTopic(@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException, SearchNoResultException {
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		return new ResponseEntity<Map<String,Object>>(userService.userTopic((String)map.get("client_id"),(Integer)map.get("pageindex")), HttpStatus.OK);
	}
	/**
	 * 2015-08-27新增
	 * 
	 * 业务功能：新注册用户向老用户发送推送
	 * @param client_id
	 * @return
	 * @throws SearchNoResultException
	 * 
	 * notice:该方法内部可能面临重构，推荐方法或许需要重设
	 */
	@RequestMapping(value="{client_id}/push")
	public ResponseEntity<Map<String,Object>> pushNewuserToOlder(@PathVariable String client_id) throws SearchNoResultException{
		return new ResponseEntity<Map<String,Object>>(userService.newUserRecommendation(client_id), HttpStatus.OK);
	}
	
	/**
	 * 2015-08-27新增
	 * 业务功能：首页推荐按照行业和距离
	 * @param mapstr
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws SearchNoResultException
	 */
	@RequestMapping(value="/recommend/distance")
	public ResponseEntity<Map<String,Object>> recommendUserByDistance(@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException, SearchNoResultException{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class); 
		String client_id = (String)map.get("client_id");
		double x_point = (Double)map.get("x_point");
		double y_point = (Double)map.get("y_point");
		userService.saveUsersLocation(client_id, x_point, y_point);
		return new ResponseEntity<Map<String,Object>>(userService.recommend(client_id,true,(Integer)map.get("pageindex")), HttpStatus.OK);
	}
	/**
	 * 
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping(value="distance/{client_id}")
	public ResponseEntity<Map<String,Object>> updateLocation(@PathVariable String client_id,@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("success", CommonMapUtil.baseMsgToMapConvertor());
		double x_point = (Double)map.get("x_point");
		double y_point = (Double)map.get("y_point");
		userService.saveUsersLocation(client_id, x_point, y_point);
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}
	
}
