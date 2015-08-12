package com.tonghang.web.common.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class DBUtil {

	private static SessionFactory sessionfactory = null;
	private static ThreadLocal<Session> threadlocal = new ThreadLocal();
	private DBUtil() {
		// TODO Auto-generated constructor stub
	}
	
	static{
		Configuration config = new Configuration().configure();
		ServiceRegistry sr = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
		sessionfactory = config.buildSessionFactory(sr);
	}
	//获取全新的session
	public static Session openSession(){
		return sessionfactory.openSession();
	}
	//获取和线程关联的session
	public static Session getCurrentSession(){
		Session session = threadlocal.get();
		//判断是否得到session
		if(session==null){
			session = sessionfactory.openSession();
			//把session对象设置到threadlocal中。此时该session和线程绑定
			threadlocal.set(session);
		}
		return session;
	}
	
	public static void closeSession(Session session){
		if(session != null){
			session.close();
		}
	}
}

