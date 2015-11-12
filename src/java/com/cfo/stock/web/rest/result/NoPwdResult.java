package com.cfo.stock.web.rest.result;

public class NoPwdResult {
	
	private String userid;
	private String mobileno;
	private String email;
	private int userstatus;
	private String markId;
	
	public NoPwdResult() {
		
	}
	
	public NoPwdResult(String userid, String mobileno, String email,
			int userstatus) {
		super();
		this.userid = userid;
		this.mobileno = mobileno;
		this.email = email;
		this.userstatus = userstatus;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getUserstatus() {
		return userstatus;
	}
	public void setUserstatus(int userstatus) {
		this.userstatus = userstatus;
	}

	public String getMarkId() {
		return markId;
	}

	public void setMarkId(String markId) {
		this.markId = markId;
	}	
	
}
