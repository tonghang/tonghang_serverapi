package com.tonghang.web.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommonMapUtil {

	public static Map<String,Object> baseMsgToMapConvertor(){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("sys_time", TimeUtil.getFormatString(new Date()));
		result.put("pic_server", Constant.PICTURE_SERVER);
		System.out.println("Constant.PICTURE_SERVER:"+Constant.PICTURE_SERVER);
		result.put("code", 200);
		result.put("message", "server normal");
		return result;
	}
	
	public static Map<String,Object> baseMsgToMapConvertor(String str, Object object){
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("sys_time", TimeUtil.getFormatString(new Date()));
		result.put("pic_server", Constant.PICTURE_SERVER);
		result.put(str, object);
		result.put("etag", SecurityUtil.getMD5(object));
		result.put("code", 200);
		return result;
	}
}
