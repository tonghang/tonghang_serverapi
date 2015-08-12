package com.tonghang.web.label.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tonghang.web.label.dao.LabelDao;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.topic.dao.TopicDao;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.user.pojo.User;

/**
 * 
 * 测试类 不是涉及实际功能
 * @author Administrator
 *
 */
@RestController("labelController")
public class LabelController {

	private LabelDao labelDao;
	private TopicDao topicDao;
	@Resource(name="topicDao")
	public void setTopicDao(TopicDao topicDao){
		this.topicDao = topicDao;
	}
	@Resource(name="labelDao")
	public void setLabelDao(LabelDao labelDao) {
		this.labelDao = labelDao;
	}
	
	@RequestMapping("label")
	public ResponseEntity<List<Label>> findLabel(@RequestParam String labelname){
		List<Label> labels = labelDao.findLabelByName(labelname);
		return new ResponseEntity<List<Label>>(labels,HttpStatus.OK);
	}
	@RequestMapping("add")
	public String addLabe(@RequestParam String labelname){
		Label label = new Label();
		label.setLabel_name(labelname);
		label.setCreated_at(new Date());
		label.setId(202);
		labelDao.save(label);
		return "SUCCESS";
	}
	@RequestMapping("find")
	public ResponseEntity<List<Label>> findLabelByUser(@RequestParam String client_id){
		User user = new User();
		user.setClient_id(client_id);
		List<Label> labels = labelDao.findLabelByUser(user);
		return new ResponseEntity<List<Label>>(labels,HttpStatus.OK);
	}
	
	@RequestMapping("setTopic")
	public String joinTopic(@RequestParam String client_id,@RequestParam String huanxin_group_id){
		User user = new User();
		Topic topic = new Topic();
		topic.setHuanxin_group_id(huanxin_group_id);
		user.setClient_id(client_id);
		topicDao.userJoinTopic(user, topic);
		return "SUCCESS";
	}
	@RequestMapping("leaveTopic")
	public String leaveTopic(@RequestParam String client_id,@RequestParam String huanxin_group_id){
		User user = new User();
		Topic topic = new Topic();
		topic.setHuanxin_group_id(huanxin_group_id);
		user.setClient_id(client_id);
		topicDao.userLeaveTopic(user);
		return "SUCCESS";
	}
	
	@RequestMapping("addTopic")
	public String addTopic(HttpServletRequest request,@RequestParam String client_id,@RequestParam String huanxin_group_id
			,@RequestParam String subject,@RequestParam String label_name) throws Exception{
		request.setCharacterEncoding("UTF-8");
		User user = new User();
		user.setClient_id(client_id);
		Label label = new Label();
		label.setLabel_name(label_name);
		Topic topic = new Topic();
		topic.setHuanxin_group_id(huanxin_group_id);
		topic.setLabel(label);
		topic.setUser(user);
		topicDao.save(topic);
		return "success";
	}
	
	@RequestMapping("bySubject")
	public ResponseEntity<List<Topic>> findTopics(HttpServletRequest request,
															@RequestParam String subject) throws Exception{
		subject = new String(subject.getBytes("iso-8859-1"),"utf-8");
		List<Topic> topics = topicDao.findTopicBySubject(subject, 1);
		return new ResponseEntity<List<Topic>>(topics,HttpStatus.OK);
	}
	
	@RequestMapping("byLabel")
	public ResponseEntity<List<Topic>> findTopicsbyLabel(HttpServletRequest request,
															@RequestParam String labelname) throws Exception{
		labelname = new String(labelname.getBytes("iso-8859-1"),"utf-8");
		List<Topic> topics = topicDao.findTopicByLabelName(labelname, 1);
		return new ResponseEntity<List<Topic>>(topics,HttpStatus.OK);
	}
	@RequestMapping("byUser")
	public ResponseEntity<List<Topic>> findTopicsbyUserl(HttpServletRequest request,
															@RequestParam String client_id) throws Exception{
		List<Topic> topics = topicDao.findTopicByUserId(client_id, 1);
		return new ResponseEntity<List<Topic>>(topics,HttpStatus.OK);
	}
}
