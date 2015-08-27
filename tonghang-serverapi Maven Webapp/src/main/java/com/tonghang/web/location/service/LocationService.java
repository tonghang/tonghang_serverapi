package com.tonghang.web.location.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.location.dao.LocationDao;
import com.tonghang.web.location.pojo.Location;
import com.tonghang.web.user.pojo.User;

@Service("locationService")
public class LocationService {

	@Resource(name="locationDao")
	private LocationDao locationDao;
	
	public void saveLocation(User user,double x_point,double y_point){
		Location location = new Location();
		location.setUser(user);
		location.setX_point(x_point);
		location.setY_point(y_point);
		locationDao.saveOrUpdateLocation(location);
	}
	/**
	 * 业务功能：计算两点之间的距离
	 * @param me
	 * @param him
	 * @return
	 */
	public double getDistanceByLocation(Location me,Location him){
		return locationDao.getDistanceByLocation(me, him);
	}
	/**
	 * 业务功能：查找某用户的地理位置（坐标）
	 * @param user
	 * @return
	 */
	public Location findLocationByUser(User user){
		return locationDao.findLocationByUser(user);
	}
}
