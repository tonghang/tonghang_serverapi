package com.tonghang.web.common.exception;

public class UpdateUserException extends BaseException{

	public int code = 513;
	public String message = "";
	
	public UpdateUserException(){
		super();
	}
	public UpdateUserException(String message){
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
