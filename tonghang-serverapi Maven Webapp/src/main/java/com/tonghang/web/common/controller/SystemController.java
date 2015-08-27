package com.tonghang.web.common.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tonghang.web.common.service.SystemService;
import com.tonghang.web.statistics.pojo.ActiveUser;
import com.tonghang.web.statistics.service.StatisticsService;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

/**
 * 系统请求，包括系统及参数的获得和发送反馈信息
 * @author Administrator
 *
 */
@RestController("systemController")
@RequestMapping("system")
public class SystemController extends BaseController{

	@Resource(name="systemService")
	private SystemService systemService;
	@Resource(name="statisticsService")
	private StatisticsService statisticsService;
	@Resource(name="userService")
	private UserService userService;
	/**
	 *  业务功能：用户发送反馈信息
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(client_id,content)
	 * @return result(boolean)
	 * @throws Exception
	 */
	@RequestMapping("feedback")
	public ResponseEntity<Map<String,Object>> feedBack(@RequestParam String mapstr) throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		return new ResponseEntity<Map<String,Object>>(systemService.sendFeedBack((String)map.get("client_id"),(String)map.get("content")),HttpStatus.OK);
	}
	
	/**
	 * 业务功能：系统返回系统参数给客户端
	 * @return can_register_user(String) can_login(String) can_upgrade_silently(String)
	 * 三个参数分别表示：系统是否允许注册新用户，系统是否允许用户登录，系统对APP升级的方式是静默还是提示
	 */
	@RequestMapping("system_config")
	public ResponseEntity<Map<String,Object>> getSystemConfig(){
		return new ResponseEntity<Map<String,Object>>(systemService.getSystemConfig(),HttpStatus.OK);
	}
	
}
