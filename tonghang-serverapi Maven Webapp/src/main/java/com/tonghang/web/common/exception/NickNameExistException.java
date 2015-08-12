package com.tonghang.web.common.exception;

public class NickNameExistException extends BaseException{

	public String message = "该昵称已被注册";
	public int code = 512;
	
	public NickNameExistException(){
		super();
	}
	public NickNameExistException(String message){
		super(message);
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
