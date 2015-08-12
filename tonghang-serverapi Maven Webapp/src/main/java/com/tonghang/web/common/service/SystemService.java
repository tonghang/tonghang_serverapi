package com.tonghang.web.common.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.common.dao.SystemDao;
import com.tonghang.web.common.pojo.FeedBack;
import com.tonghang.web.common.pojo.SystemConfig;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;

@Service("systemService")
public class SystemService {

	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="systemDao")
	private SystemDao systemDao;
	
	/**
	 * 
	 * @param client_id
	 * @param content
	 * @return
	 */
	public Map<String,Object> sendFeedBack(String client_id,String content){
		System.out.println("feedback"+client_id);
		User user = userDao.findUserById(client_id);
		FeedBack feedback = new FeedBack();
		feedback.setContent(content);
		feedback.setUser(user);
		systemDao.saveFeedBack(feedback);
		Map<String,Object> response = new HashMap<String, Object>();
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("result", true);
		result.putAll(CommonMapUtil.baseMsgToMapConvertor());
		response.put("success", result);
		return response;
	}
	/**
	 * 获得系统级参数
	 * @return
	 * can_register_user：是否能注册
	 * can_login：是否能登陆
	 * can_upgrade_silently：是否是静默升级
	 */
	public Map<String,Object> getSystemConfig(){
		Map<String,Object> result = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> sysmsg= new HashMap<String, Object>();
		Map<String,Object> config = new HashMap<String, Object>();
		SystemConfig system = systemDao.findSystemConfig();
		config.put("time", TimeUtil.getFormatString(new Date()));
		config.put("can_register_user", system.getCan_regist()==1?true:false);
		config.put("can_login", system.getCan_login()==1?true:false);
		config.put("can_upgrade_silently", system.getCan_upgrade_silence()==1?true:false);
		sysmsg.put("system", config);
		result.put("success", sysmsg);
		return result;
	}
	
}
