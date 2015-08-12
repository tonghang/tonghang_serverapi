package com.tonghang.web.statistics.filter;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.mapping.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.DataUtil;
import com.tonghang.web.statistics.pojo.ActiveUser;
import com.tonghang.web.statistics.service.StatisticsService;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.dao.impl.UserDaoImpl;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Controller("filter")
public class StatisticsFilter implements Filter {
	
	@Resource(name="userService")
	public UserService userService;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("拦截器拦截到请求");
		String client_id = request.getParameter("client_id");
		if(client_id!=null){
			System.out.println(Constant.PICTURE_SERVER+"/system/active_user.json?client_id="+client_id);
			DataUtil.getSimpleEntity(Constant.PICTURE_SERVER+"/system/active_user.json?client_id="+client_id, Map.class);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}

}
