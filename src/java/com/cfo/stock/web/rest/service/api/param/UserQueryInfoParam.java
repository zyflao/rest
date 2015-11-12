package com.cfo.stock.web.rest.service.api.param;

public class UserQueryInfoParam extends Param{
	
	private String loginname;
	private int nametype;
	
	public UserQueryInfoParam(){
		
	}
	
	public UserQueryInfoParam(String loginname, int nametype){
		super();
		this.loginname = loginname;
		this.nametype = nametype;
	}

	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public int getNametype() {
		return nametype;
	}
	public void setNametype(int nametype) {
		this.nametype = nametype;
	}
		
	}