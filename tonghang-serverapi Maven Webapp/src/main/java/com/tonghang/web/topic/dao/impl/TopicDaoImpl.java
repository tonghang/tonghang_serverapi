package com.tonghang.web.topic.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.topic.dao.TopicDao;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.user.pojo.User;
@Deprecated
@Repository("topicDao")
public class TopicDaoImpl implements TopicDao{

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
	/**
	 * 新建话题
	 */
	@Override
	@Transactional
	public void save(Topic topic) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(topic);
	}
	
	/**
	 * 通过标签名模糊查找话题
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> findTopicByLabelName(String label_name,int nowpage) {
		// TODO Auto-generated method stub
		List<Topic> topics = (List<Topic>) sessionFactory.getCurrentSession().createQuery("from Topic as topic where lower(topic.label.label_name) like concat('%',lower(:label_name),'%') order by topic.created_at")
																			.setParameter("label_name", label_name).setFirstResult(Constant.PAGESIZE*(nowpage-1)).setMaxResults(Constant.PAGESIZE).list();
		return topics;
	
	}

	/**
	 * 按照subject查找话题
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> findTopicBySubject(String subject,int nowpage) {
		// TODO Auto-generated method stub
		List<Topic> topics = (List<Topic>) sessionFactory.getCurrentSession().createQuery("from Topic as topic where lower(topic.subject) like concat('%',lower(:subject),'%')")
																	.setParameter("subject", subject).setFirstResult(Constant.PAGESIZE*(nowpage-1)).setMaxResults(Constant.PAGESIZE).list();
		return topics;
	}
	
	/**
	 * 查看指定用户创建的话题
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> findTopicByUserId(String client_id,int nowpage) {
		// TODO Auto-generated method stub
		List<Topic> topics = (List<Topic>) sessionFactory.getCurrentSession().createQuery("from Topic as topic where topic.user.client_id = :client_id")
																						.setParameter("client_id", client_id).setFirstResult(Constant.PAGESIZE*(nowpage-1)).setMaxResults(Constant.PAGESIZE).list();
		return topics;
	}

	/**
	 * 用户加入话题
	 */
	@Override
	public void userJoinTopic(User user, Topic topic) {
		// TODO Auto-generated method stub
		Topic t =findTopicById(topic.getHuanxin_group_id());	
		user = (User) sessionFactory.getCurrentSession().get(User.class, user.getClient_id());
		user.setTopic(t);
		sessionFactory.getCurrentSession().save(user);
	}

	/**
	 * 
	 * 用户离开话题
	 */
	@Override
	public void userLeaveTopic(User user) {
		// TODO Auto-generated method stub
		user = (User) sessionFactory.getCurrentSession().get(User.class, user.getClient_id());
		user.setTopic(null);
	}
	
	/**
	 * 按照huanxin_group_id 查找话题
	 */
	@Override
	public Topic findTopicById(String id) {
		// TODO Auto-generated method stub
		Topic topic = (Topic) sessionFactory.getCurrentSession().get(Topic.class, id);
		return topic;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> recommendTopics(String label, int nowpage) {
		// TODO Auto-generated method stub
		List<Topic> topics = (List<Topic>) sessionFactory.getCurrentSession().createQuery("from Topic as topic where lower(topic.label.label_name ) like concat('%',lower(:label_name),'%')")
													.setParameter("label_name", label).setFirstResult(Constant.PAGESIZE*(nowpage-1)).setMaxResults(Constant.PAGESIZE).list();
		return topics;
	}
	
	@Override
	public List<User> checkMemberFromTopic(String topic_id,int nowpage) {
		// TODO Auto-generated method stub
		Topic topic = (Topic) sessionFactory.getCurrentSession().get(Topic.class, topic_id);
		List<User> users = new ArrayList<User>();
		users.addAll(Constant.PAGESIZE*(nowpage-1),topic.getUsers());
		return users;
	}
	@Override
	public void delete(Topic topic) {
		// TODO Auto-generated method stub
		topic.setUsers(null);
		sessionFactory.getCurrentSession().update(topic);//删除话题中存在的用户
		sessionFactory.getCurrentSession().delete(topic);
	}
}
