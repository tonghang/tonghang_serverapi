package com.tonghang.web.common.exception;

public class SearchNoResultException extends BaseException{

	public int code = 520;
	public String message = "搜索结果为空";
	
	public SearchNoResultException(){
		super();
	}
	public SearchNoResultException(String message){
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
