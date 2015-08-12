package com.tonghang.web.common.dao;

import java.io.Serializable;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.topic.pojo.Topic;

public class BaseDao<T> {

	private SessionFactory sessionFactory;
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	private Class<T> klass;
	
	public BaseDao(Class<T> klass){
		this.klass=klass;
	}
	
	@Transactional
	public void save(T t) {
		// TODO Auto-generated method stub
		sessionFactory.openSession().getTransaction().begin();
		sessionFactory.openSession().save(t);
		sessionFactory.openSession().getTransaction().commit();
	}
	
	@Transactional
	public void delete(T t){
		sessionFactory.openSession().delete(t);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public T findById(Serializable id) {
		return (T) sessionFactory.openSession().get(klass, id);
	}
	
}
