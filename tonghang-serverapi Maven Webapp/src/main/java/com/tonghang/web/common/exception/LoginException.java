package com.tonghang.web.common.exception;

public class LoginException extends BaseException{

	public int code = 510;
	public String message = "邮箱或密码错误";
	
	public LoginException(){
		super();
	}
	public LoginException(String message){
		this.message = message;
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	
	public int getCode(){
		return code;
	}

}
