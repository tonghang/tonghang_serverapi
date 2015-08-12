package com.tonghang.web.topic.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tonghang.web.common.controller.BaseController;
import com.tonghang.web.common.exception.SearchNoResultException;
import com.tonghang.web.label.service.LabelService;
import com.tonghang.web.topic.service.TopicService;
import com.tonghang.web.user.pojo.User;
/**
 * 处理有关话题模块的请求
 * 继承BaseController处理所有异常
 * @author Administrator
 *
 */
@RestController("topicController")
@RequestMapping("topic")
public class TopicController extends BaseController{

	private TopicService topicService;
	private LabelService labelService;

	public TopicService getTopicService() {
		return topicService;
	}
	@Resource(name="topicService")
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	public LabelService getLabelService() {
		return labelService;
	}
	@Resource(name="labelService")
	public void setLabelService(LabelService labelService) {
		this.labelService = labelService;
	}
	
	/**
	 * 业务功能：创建话题,发表话题(环信接口有问题)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(client_id,subject,label_name)
	 * @return topics(Map) huanxin_group_id(String) subject(String) 
	 * 				label_name(String) client_id(String), create_at(Date)
	 * @throws Exception
	 * 1.添加话题时话题的huanxin_group_id是环信自动生成的，先把话题的基本信息提交到环信，环信返回一个
	 * id后，存入数据库，并把该话题信息包装在map中，并由ResponseEntity包装成JSON返回给前台。
	 */
	@RequestMapping("create")
	public ResponseEntity<Map<String,Object>> createTopic(@RequestParam String mapstr) throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object>result = topicService.addTopic((String)map.get("client_id"), (String)map.get("subject"), (String)map.get("label_name"));
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}

	/**
	 * 业务功能：首页话题推荐(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(client_id)
	 * @return topics(List<Map>) huanxin_group_id(String) subject(String) 
	 * 				label_name(String) client_id(String), create_at(Date)
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.话题推荐和用户推荐思路差不多，recommendTopics()方法首先得到用户包含的标签，然后通过标签模糊查找对应的话题，
	 * 将话题信息包装成Map,放进List中，并由ResponseEntity包装成JSON返回给前台。
	 * 2.返回值为列表的请求全部需要分页，客户端需要传一个pageindex表示当前页数
	 * @throws SearchNoResultException 
	 */
	@RequestMapping("recommend")
	public ResponseEntity<Map<String,Object>> recommendTopic(@RequestParam String mapstr/* String client_id,@RequestParam String etag*/) throws JsonParseException, JsonMappingException, IOException, SearchNoResultException{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		User user = new User();
		user.setClient_id((String)map.get("client_id"));
		System.out.println("recommendTopic:"+(String)map.get("client_id"));
		Map<String,Object> result = topicService.recommendTopics(user,(Integer)map.get("pageindex"));
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}
	
	/**
	 * 业务功能:按标签搜索话题(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(label_name,pageindex)
	 * @return topics(List<Map>) huanxin_group_id(String) subject(String) 
	 * 				label_name(String) client_id(String), create_at(Date)
	 * @throws Exception
	 * 1.checkTopicByLabel()方法通过标签名模糊查询包含标签的话题，将话题信息包装成Map,所有话题信息放入List,并由ResponseEntity包装成JSON返回给前台。
	 * 2.返回值为列表的请求全部需要分页，客户端需要传一个pageindex表示当前页数
	 */
	@RequestMapping("search/label")
	public ResponseEntity<Map<String,Object>> searchTopicByLabel(@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String, Object> result = topicService.checkTopicByLabel((String)map.get("label_name"),(Integer)map.get("pageindex"));
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}
	
	/**
	 * 业务功能：按关键字搜索话题(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(subject,pageindex)
	 * @return topics(List<Map>) huanxin_group_id(String) subject(String) 
	 * 				label_name(String) client_id(String), create_at(Date)
	 * @throws Exception
	 * 1.checkTopicBySubject()方法通过关键字模糊查询包含标签的话题，将话题信息包装成Map,所有话题信息放入List,并由ResponseEntity包装成JSON返回给前台。
	 * 2.返回值为列表的请求全部需要分页，客户端需要传一个pageindex表示当前页数
	 */
	@RequestMapping("search/subject")
	public ResponseEntity<Map<String,Object>> searchTopicBySubject(@RequestParam String mapstr)throws Exception{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String, Object> result = topicService.checkTopicBySubject((String)map.get("subject"),(Integer)map.get("pageindex"));
		return new ResponseEntity<Map<String, Object>>(result,HttpStatus.OK);
	}
	/**
	 * 业务功能：用户加入话题(调试通过)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(topic_id,client_id)
	 * @return user(Map) labels(List) email (String) image(String)
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date 用户创建时间) birth(Date) is_friend(boolean)
	 * 			created_at(Date 话题的创建时间) huanxin_group_id(String) client_id(String 话题创建者id)
	 * 			label_name(String) subject(String) topic_id(String)
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.joinOrLeaveTopic()方法同时处理用户加入话题和离开话题，方法第二个boolean类型参数，true表示加入，false表示离开
	 * 方法返回加入者的信息和话题的信息，包装成map，并由ResponseEntity包装成JSON返回给前台。
	 */
	@RequestMapping("join")
	public ResponseEntity<Map<String,Object>> joinTopic(@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = topicService.joinOrLeaveTopic((String)map.get("topic_id"), (String)map.get("client_id"),true);
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}
	/**
	 * 业务功能：用户离开(接口废弃，离开操作在加入前检查并删除记录)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(topic_id,client_id)
	 * @return user(Map) labels(List) email (String) image(String)
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date 用户创建时间) birth(Date) is_friend(boolean)
	 * 			created_at(Date 话题的创建时间) huanxin_group_id(String) client_id(String 话题创建者id)
	 * 			label_name(String) subject(String) topic_id(String)
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.joinOrLeaveTopic()方法同时处理用户加入话题和离开话题，方法第二个boolean类型参数，true表示加入，false表示离开
	 * 方法返回加入者的信息和话题的信息，包装成map，并由ResponseEntity包装成JSON返回给前台。
	 */
	@RequestMapping("leave")
	public ResponseEntity<Map<String,Object>> leaveTopic(@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException{
		System.out.println("---------------------------leave");
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		Map<String,Object> result = topicService.joinOrLeaveTopic((String)map.get("topic_id"), (String)map.get("client_id"),false);
		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
	}
	/**
	 * 业务功能：查看话题中的用户(调试成功)
	 * @param mapstr 前端的JSON数据，全部包括在mapstr中(client_id,topic_id,pageindex)
	 * @return users(List<Map>) labels(List) email (String) image(String)
	 * 				sex(String) phone(String) city(String) username(String)
	 * 				client_id(String) created_at(Date 用户创建时间) birth(Date) is_friend(boolean)
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 1.checkTopicMember()方法能通过topic_id 查询其包含的所有用户，并将信息封装在map中，并由ResponseEntity包装成JSON返回给前台。
	 * 2.返回值为列表的请求全部需要分页，客户端需要传一个pageindex表示当前页数
	 * 3.所有返回用户信息的地方都会返回是否是好友关系
	 */
	@RequestMapping("member")
	public ResponseEntity<Map<String,Object>> checkMember(@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		return new ResponseEntity<Map<String,Object>>(topicService.checkTopicMember((String)map.get("client_id"),(String)map.get("topic_id"), (Integer)map.get("pageindex")),HttpStatus.OK);
	}
	
	@RequestMapping("/{huanxin_group_id}")
	public ResponseEntity<Map<String,Object>> checkTopic(@PathVariable String huanxin_group_id,@RequestParam String mapstr) throws JsonParseException, JsonMappingException, IOException{
		Map map = new ObjectMapper().readValue(mapstr, HashMap.class);
		return new ResponseEntity<Map<String,Object>>(topicService.getTopicById(huanxin_group_id),HttpStatus.OK);
	}
	
}
