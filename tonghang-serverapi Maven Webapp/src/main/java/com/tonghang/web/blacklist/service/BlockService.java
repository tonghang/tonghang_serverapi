package com.tonghang.web.blacklist.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.HuanXinUtil;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;

@Service("blockService")
public class BlockService {

	@Resource(name="userDao")
	private UserDao userDao;
	
	/**
	 * 业务功能：创建某用户和某用户的黑名单关系
	 * @param my_id
	 * @param blocker_id
	 */
	public Map<String,Object> createBlock(String my_id,String blocker_id){
		Map<String,Object>  result = new HashMap<String, Object>();
		result.put("success", CommonMapUtil.baseMsgToMapConvertor());
		User me = userDao.findUserById(my_id);
		User blocker = userDao.findUserById(blocker_id);
		HuanXinUtil.blockUser(my_id, blocker_id);
		HuanXinUtil.blockUser(blocker_id,my_id);
		userDao.addBlocker(me, blocker);
		userDao.addBlocker(blocker, me);
		return result ;
	}
	/**
	 * 业务功能：从黑名单中移除某用户
	 * @param my_id
	 * @param blocker_id
	 */
	public Map<String,Object> deleteBlock(String my_id,String blocker_id){
		Map<String,Object>  result = new HashMap<String, Object>();
		result.put("success", CommonMapUtil.baseMsgToMapConvertor());
		User me = userDao.findUserById(my_id);
		User blocker = userDao.findUserById(blocker_id);
		HuanXinUtil.deblockUser(my_id, blocker_id);
		HuanXinUtil.deblockUser(blocker_id, my_id);
		userDao.deleteBlock(me, blocker);
		userDao.deleteBlock(blocker, me);
		return result ;
	}
}
