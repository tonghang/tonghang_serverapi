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

@Repository("statisticsDao")
public class StatisticsDaoImpl implements StatisticsDao {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public void addActiveUser(ActiveUser auser) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		Query query = session.createQuery("from ActiveUser auser where auser.date = :date and auser.user.client_id = :client_id")
													.setParameter("date", auser.getDate()).setParameter("client_id", auser.getUser().getClient_id());
		if(query.uniqueResult()==null)
			session.saveOrUpdate(auser);
		session.getTransaction().commit();
	}

	@Override
	public int getActiveUserNumber() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		int count = session.createQuery("from ActiveUser au").list().size();
		return count;
	}

	@Override
	public int getDistinctRecord(String client_id, Date date) {
		// TODO Auto-generated method stubSession session = sessionFactory.openSession();
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		Query query = session.createQuery("from ActiveUser au where au.user.client_id = :client_id and au.date between '"+TimeUtil.getFormatStringDate(date)+"' and '"+TimeUtil.getFormatStringDate(date)+"'").
										setParameter("client_id", client_id);
		int count = query.list().size();
		System.out.println(query.getQueryString());
		return count;
	}

}
