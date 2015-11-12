package com.cfo.stock.web.rest.result;

import com.cfo.stock.web.rest.vo.UserBaseVo;

public class UserRegInfoResult extends Result<UserBaseVo>{
	
	//用户id	 String	 
	private String userid;	
	//注册时间	 String yyyy-MM-dd HH:mm:ss	 
	private String regtime;	 
	//用户状态 
	private int userstatus ;
	private String mobileno;
	private String sessionId;
	
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public UserRegInfoResult() {
		// TODO Auto-generated constructor stub
	}
	public UserRegInfoResult(String userid, String regtime, int userstatus) {
		super();
		this.userid = userid;
		this.regtime = regtime;
		this.userstatus = userstatus;
	}

	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getRegtime() {
		return regtime;
	}
	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}
	public int getUserstatus() {
		return userstatus;
	}
	public void setUserstatus(int userstatus) {
		this.userstatus = userstatus;
	}
	
	public UserBaseVo parse() {
		return new UserBaseVo(userid,regtime,userstatus);
	}
}
