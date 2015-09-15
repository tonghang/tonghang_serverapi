package com.tonghang.web.location.dao.impl;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.location.dao.LocationDao;
import com.tonghang.web.location.pojo.Location;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.user.pojo.User;

@Repository("locationDao")
public class LocationDaoImpl implements LocationDao{

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	
	@Override
	public double getDistanceByLocation(Location me, Location him) {
		// TODO Auto-generated method stub
		double distance = Double.MAX_VALUE;
		System.out.println("double 最大值："+distance);
		if(him!=null){
			distance = (Double) sessionFactory.getCurrentSession().createSQLQuery("select round(6378.138*2*asin(sqrt(pow(sin( (:x1 *pi()/180-:x2 *pi()/180)/2),2)+cos(:x1*pi()/180)*cos(:x2*pi()/180)" +
					"* pow(sin( (:y1*pi()/180-:y2*pi()/180)/2),2)))*1000) as distance").setParameter("x1", me.getX_point()).setParameter("x2", him.getX_point())
					.setParameter("y1", me.getY_point()).setParameter("y2", him.getY_point()).uniqueResult();
		}
		return distance;
	}

	@Override
	public Location findLocationByUser(User user) {
		// TODO Auto-generated method stub
		Location local = (Location) sessionFactory.getCurrentSession().get(Location.class, user.getClient_id());
		return local;
	}

	@Override
	public void saveOrUpdateLocation(Location location) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(location);
	}

}
