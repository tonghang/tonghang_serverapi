package com.tonghang.web.common.dao;

import com.tonghang.web.common.pojo.FeedBack;
import com.tonghang.web.common.pojo.SystemConfig;

public interface SystemDao {

	public void saveFeedBack(FeedBack feedback);
	
	public SystemConfig findSystemConfig();
	
}
