package com.tonghang.web.common.exception;

public class BaseException extends Exception{

	public int code ;
	public String message;
	public BaseException(){
		super();
	}
	public BaseException(String message){
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
