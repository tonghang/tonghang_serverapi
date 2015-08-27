package com.tonghang.web.statistics.aop;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component("accountUser")
@Aspect
public class AccountUser {

	@Before("execution(* com.tonghang.web.user.controller.UserController.recommend*(..))||" +
			"execution(* com.tonghang.web.user.controller.UserController.searchLabel*(..))||" +
			"execution(* com.tonghang.web.user.controller.UserController.searchNick*(..))||" +
			"execution(* com.tonghang.web.user.controller.UserController.update*(..))||" +
			"execution(* com.tonghang.web.user.controller.UserController.updatePassword*(..))") 
	@Pointcut
	public void accountActiveUser(JoinPoint point){
		Object[] args = point.getArgs();
		System.out.println("AOP_______________________________"+Arrays.toString(args));
	}
}
