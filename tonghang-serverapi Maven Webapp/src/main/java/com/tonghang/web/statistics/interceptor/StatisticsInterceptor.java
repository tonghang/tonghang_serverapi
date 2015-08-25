package com.tonghang.web.statistics.interceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tonghang.web.statistics.pojo.ActiveUser;
import com.tonghang.web.statistics.service.StatisticsService;
import com.tonghang.web.user.service.UserService;

@Component
public class StatisticsInterceptor implements HandlerInterceptor {

	@Resource(name="statisticsService")
	private StatisticsService statisticsService;
	@Resource(name="userService")
	private UserService userService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 业务功能：统计活跃用户
	 * 
	 * notice:活跃用户目前的定义是：每天向服务器发送一定请求的用户。每个用户每日仅限一次。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("进入拦截器-----------------------------------进入拦截器");
		String mapstr = request.getParameter("mapstr");
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		String client_id = (String) ((Map)map.get("token")).get("client_id");
		System.out.println("拦截器拦截到的client_id: "+client_id);
		if(client_id!=null&&!"".equals(client_id)&&!statisticsService.isActivedToday(client_id)){
			System.out.println("添加活跃记录");
			ActiveUser auser = new ActiveUser();
			auser.setUser(userService.findUserById(client_id));
			auser.setDate(new Date());
			statisticsService.addActiveUser(auser);			
		}
		return true;
	}

}
