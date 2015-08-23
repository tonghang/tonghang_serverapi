package com.tonghang.web.statistics.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.statistics.dao.StatisticsDao;
import com.tonghang.web.statistics.pojo.ActiveUser;
import com.tonghang.web.user.pojo.User;

@Service("statisticsService")
public class StatisticsService {

	@Resource(name="statisticsDao")
	private StatisticsDao statisticsDao;
	
	public void addActiveUser(ActiveUser auser){
		statisticsDao.addActiveUser(auser);
	}
	
	public int getActiveUserCount(){
		return statisticsDao.getActiveUserNumber();
	}
	
	/**
	 * 业务功能：查看某个用户在当天记录中是否已活跃
	 * @param client_id 用户的client_id
	 * @return
	 */
	public boolean isActivedToday(String client_id){
		System.out.println("isActivedToday:::"+statisticsDao.getDistinctRecord(client_id, new Date()));
		return (statisticsDao.getDistinctRecord(client_id, new Date())==0)?false:true;
	}
	
}
