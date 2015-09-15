package com.tonghang.web.statistics.dao.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.statistics.dao.StatisticsDao;
import com.tonghang.web.statistics.pojo.ActiveUser;
import com.tonghang.web.statistics.pojo.Channel;

@Repository("statisticsDao")
public class StatisticsDaoImpl implements StatisticsDao {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public void addActiveUser(ActiveUser auser) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(auser);
	}

	@Override
	public int getActiveUserNumber() {
		// TODO Auto-generated method stub
		return sessionFactory.getCurrentSession().createQuery("from ActiveUser au").list().size();
	}

	@Override
	public int getDistinctRecord(String client_id, Date date) {
		// TODO Auto-generated method stubSession session = sessionFactory.openSession();
		Query query = sessionFactory.getCurrentSession().createQuery("from ActiveUser au where au.user.client_id = :client_id and au.date between '"+TimeUtil.getFormatStringDate(date)+"' and '"+TimeUtil.getFormatStringDate(date)+"'").
										setParameter("client_id", client_id);
		return query.list().size();
	}

	@Override
	public void createChannel(Channel chan) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(chan);
	}

}
