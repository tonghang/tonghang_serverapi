package com.tonghang.web.common.util;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

public class Initialization implements InitializingBean ,ServletContextAware{

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("启动Server的时候");
		HuanXinUtil.getHuangXinServerToken();
		HuanXinUtil.getTokenPeriodly();
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		
	}

}
