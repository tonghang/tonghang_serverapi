package com.tonghang.web.topic.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.SecurityUtil;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.util.UserUtil;

/**
 * 所有对象列表的外层Map结构都有success键
 * @author Administrator
 *
 *notice:1.0/0.3 版本 已经删除话题功能
 */
@Deprecated
public class TopicUtil {

	/**
	 * 把topic列表转换成 Map<String,List<Map<String,Object>>> 结构
	 * @param topics
	 * @return
	 */
	public static Map<String,Object> topicsToMapConvertor(List<Topic> topics){
		List<Map<String,Object>> topicsmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> topicmap = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> result = new HashMap<String, Object>();
		for(Topic t:topics){
			Map<String,Object> msg = new HashMap<String, Object>();
			msg.put("topic_id", t.getHuanxin_group_id());
			msg.put("subject", t.getSubject());
			msg.put("label_name", t.getLabel().getLabel_name());
			msg.put("user_id", t.getUser().getClient_id());
			msg.put("image",Constant.IMAGE_PATH+t.getUser().getClient_id()+"/"+Constant.IMAGE_NAME);
			msg.put("username", t.getUser().getUsername());
			msg.put("created_at", TimeUtil.getFormatString(t.getCreated_at()));
			topicsmsg.add(msg);
		}
		//前台数据需要什么样的格式
//		topicmap.put("etag", SecurityUtil.getMD5(topicsmsg));
/*		if(!SecurityUtil.getMD5(topicsmsg).equals(etag)){
			topicmap.put("topics", topicsmsg);
		}else{
			topicmap.put("topics", "");
		}*/
		topicmap.put("topics", topicsmsg);
		result.put("success", topicmap);
		return result;
	}
	/**
	 * 把topic对象转换成Map<String,Object>结构
	 * @param topic
	 * @param user
	 * @return
	 */
	public static Map<String,Object> topicToMapConvertor(Topic topic,User user){
		Map<String,Object> topicmap = new HashMap<String, Object>();
		topicmap.put("created_at", TimeUtil.getFormatString(topic.getCreated_at()));
		topicmap.put("topic_id", topic.getHuanxin_group_id());
		topicmap.put("user_id", topic.getUser().getClient_id());
		topicmap.put("label_name", topic.getLabel().getLabel_name());
		topicmap.put("subject", topic.getSubject());
		topicmap.put("image",Constant.IMAGE_PATH+topic.getUser().getClient_id()+"/"+Constant.IMAGE_NAME);
		topicmap.put("username", topic.getUser().getUsername());
		System.out.println(" TopicUtil:::"+topic.getUser().getUsername()+" another:"+user.getUsername());
//		topicmap.put("topic_id", topic.getHuanxin_group_id());废弃
		return topicmap;
	}
}
