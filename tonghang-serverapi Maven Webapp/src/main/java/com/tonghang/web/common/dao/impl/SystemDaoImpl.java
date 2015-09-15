package com.tonghang.web.common.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.common.dao.SystemDao;
import com.tonghang.web.common.pojo.FeedBack;
import com.tonghang.web.common.pojo.SystemConfig;

@Repository("systemDao")
public class SystemDaoImpl implements SystemDao{

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Override
	public void saveFeedBack(FeedBack feedback) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(feedback);
	}

	@Override
	public SystemConfig findSystemConfig() {
		// TODO Auto-generated method stub
		return (SystemConfig) sessionFactory.getCurrentSession().get(SystemConfig.class, 1);
	}

}
