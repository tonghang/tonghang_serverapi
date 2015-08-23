package com.tonghang.web.statistics.dao;

import java.util.Date;

import com.tonghang.web.statistics.pojo.ActiveUser;
import com.tonghang.web.statistics.pojo.Channel;

public interface StatisticsDao {

	public void addActiveUser(ActiveUser auser);
	public void createChannel(Channel chan);
	public int getActiveUserNumber();
	public int getDistinctRecord(String client_id,Date date);
}
