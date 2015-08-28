package com.tonghang.web.statistics.aop;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.tonghang.web.statistics.pojo.ActiveUser;
import com.tonghang.web.statistics.service.StatisticsService;
import com.tonghang.web.user.service.UserService;

@Component("accountUser")
@Aspect
public class AccountUser {

	@Resource(name="statisticsService")
	private StatisticsService statisticsService;
	@Resource(name="userService")
	private UserService userService;
	
	@After("execution(* com.tonghang.web.user.controller.*.recommend(..))||" +
			"execution(* com.tonghang.web.user.controller.*.search*(..))") 
	public void accountActiveUser(JoinPoint point) throws JsonParseException, JsonMappingException, IOException{
		Object[] args = point.getArgs();
		System.out.println("args : "+Arrays.toString(args));
		Map map = new ObjectMapper().readValue((String)args[0], HashMap.class);
		String client_id = (String) ((Map)map.get("token")).get("client_id");
		System.out.println("拦截器拦截到的client_id: "+client_id);
		if(client_id!=null&&!"".equals(client_id)&&!statisticsService.isActivedToday(client_id)){
			System.out.println("添加活跃记录");
			ActiveUser auser = new ActiveUser();
			auser.setUser(userService.findUserById(client_id));
			auser.setDate(new Date());
			statisticsService.addActiveUser(auser);			
		}
	}
}
