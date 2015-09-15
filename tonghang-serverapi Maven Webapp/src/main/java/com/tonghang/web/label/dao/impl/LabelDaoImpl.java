package com.tonghang.web.label.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.label.dao.LabelDao;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.pojo.User;

@Repository("labelDao")
public class LabelDaoImpl implements LabelDao{
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public void save(Label label) {
		// TODO Auto-generated method stub
//		System.out.println("开始插入"+label.getLabel_name());
		sessionFactory.getCurrentSession().save(label);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Label> findLabelByName(String label_name) {
		// TODO Auto-generated method stub
		Query query =  sessionFactory.getCurrentSession().createQuery("from Label as label where lower(label.label_name) like concat('%',lower(:label_name),'%')");
		return query.setParameter("label_name",label_name ).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Label> findLabelByUser(User user) {
		// TODO Auto-generated method stub	
		Query query = sessionFactory.getCurrentSession().createQuery("select label.label_name from Label as label join label.userlist as users where users.client_id= :client_id").
													setParameter("client_id", user.getClient_id()).setCacheable(false);
		List<String> labelnames = (List<String>) query.list();
		List<Label> labels = new ArrayList<Label>();
		Set<Label> labelset = new HashSet<Label>();
		for(String names:labelnames){
			Label l = (Label) sessionFactory.getCurrentSession().get(Label.class, names);
			labelset.add(l);
		}
		user.setLabellist(labelset);
		labels.addAll(user.getLabellist());	
		return labels;
	}

	@Override
	public Label findLabelById(String id) {
		// TODO Auto-generated method stub
		return (Label) sessionFactory.getCurrentSession().get(Label.class, id);
	}

	
}
