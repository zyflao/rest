package com.cfo.stock.web.rest.service.api.param;

public class UpdateNameParam extends Param {

	private String userid;
	// 身份证号
	private String idnumber;
	// 真实姓名
	private String realname;
	// 是否校验身份证
	private String idvalid;

	public UpdateNameParam() {
		// TODO Auto-generated constructor stub
	}

	public UpdateNameParam(String idnumber, String realname,String idvalid) {
		super();
		this.idnumber = idnumber;
		this.realname = realname;
		this.idvalid = idvalid;
	}
	
	public UpdateNameParam(String idnumber) {		
		this.idnumber = idnumber;
	}
	
	public UpdateNameParam(String idnumber, String realname) {
		super();
		this.idnumber = idnumber;
		this.realname = realname;
	}

	public UpdateNameParam(String userid, String idnumber, String realname,
			String idvalid) {
		super();
		this.userid = userid;
		this.idnumber = idnumber;
		this.realname = realname;
		this.idvalid = idvalid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getIdvalid() {
		return idvalid;
	}

	public void setIdvalid(String idvalid) {
		this.idvalid = idvalid;
	}

}
