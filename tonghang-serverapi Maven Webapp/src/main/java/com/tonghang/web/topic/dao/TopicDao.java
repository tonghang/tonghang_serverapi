package com.tonghang.web.topic.dao;

import java.util.List;
import java.util.Set;

import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.user.pojo.User;

public interface TopicDao {

	public void save(Topic topic);
	
	public Topic findTopicById(String id);
	
	public List<Topic> findTopicByLabelName(String label_name,int nowpage);
	public List<Topic> findTopicBySubject(String subject,int nowpage);
	public List<Topic> findTopicByUserId(String client_id,int nowpage);
	public List<Topic> recommendTopics(String client_id,int nowpage);
	
	public void userJoinTopic(User user,Topic topic);
	public void userLeaveTopic(User user);
	
	public List<User> checkMemberFromTopic(String topic_id,int nowpage);
}
