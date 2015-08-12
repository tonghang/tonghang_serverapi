package com.tonghang.web.common.util;

import org.apache.log4j.Logger;

public class LogUtil {

	private static final Logger logger = Logger.getLogger("logger_name");
	
	public static void printLog(Object message){
		logger.info(message);
	}

}
