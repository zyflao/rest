package com.cfo.stock.web.rest.service.api.param;

public class UserLoginParam extends Param{
	
// 登陆必传参数
	private String loginname;
	private int nametype;
	private String passwd;
//	登陆非必传参数
	private String clientip;
	private String clientinfo;
	private String cccode;
	
	public UserLoginParam(String loginname, int nametype, String passwd) {
		super();
		this.loginname = loginname;
		this.nametype = nametype;
		this.passwd = passwd;
	} 
		
	public UserLoginParam(String loginname, int nametype, String passwd,
			String clientip, String clientinfo, String cccode) {
		super();
		this.loginname = loginname;
		this.nametype = nametype;
		this.passwd = passwd;
		this.clientip = clientip;
		this.clientinfo = clientinfo;
		this.cccode = cccode;
	}


	public String getClientip() {
		return clientip;
	}


	public void setClientip(String clientip) {
		this.clientip = clientip;
	}


	public String getClientinfo() {
		return clientinfo;
	}


	public void setClientinfo(String clientinfo) {
		this.clientinfo = clientinfo;
	}


	public String getCccode() {
		return cccode;
	}


	public void setCccode(String cccode) {
		this.cccode = cccode;
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
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
}
