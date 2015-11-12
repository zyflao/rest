package com.cfo.stock.web.rest.service.api.param;

public class FindPasswdParam extends Param{
	private String userid;
	private String mobileno;
	private String validcode;
	private String passwd;
	
	
	public FindPasswdParam() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public FindPasswdParam(String userid, String mobileno) {
		super();
		this.userid = userid;
		this.mobileno = mobileno;
	}
	

	public FindPasswdParam(String userid, String mobileno, String validcode) {
		super();
		this.userid = userid;
		this.mobileno = mobileno;
		this.validcode = validcode;
	}

	

	public FindPasswdParam(String userid, String mobileno, String validcode,
			String passwd) {
		super();
		this.userid = userid;
		this.mobileno = mobileno;
		this.validcode = validcode;
		this.passwd = passwd;
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
	public String getValidcode() {
		return validcode;
	}
	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	
}
