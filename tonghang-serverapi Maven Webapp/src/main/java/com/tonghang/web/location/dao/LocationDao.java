package com.tonghang.web.location.dao;

import com.tonghang.web.location.pojo.Location;
import com.tonghang.web.user.pojo.User;

public interface LocationDao {

	public void saveOrUpdateLocation(Location location);
	
	public double getDistanceByLocation(Location me,Location him);
	
	public Location findLocationByUser(User user);
}
