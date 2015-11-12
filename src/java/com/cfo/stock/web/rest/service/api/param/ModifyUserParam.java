package com.cfo.stock.web.rest.service.api.param;

public class ModifyUserParam extends Param{
	
	private String userid;
	private String validdate;
	private String postcode;
	private String regioncode;
	private String address;
	private int sex;
	private String description;
	private String reservedinfo;
	
	public String getReservedinfo() {
		return reservedinfo;
	}


	public void setReservedinfo(String reservedinfo) {
		this.reservedinfo = reservedinfo;
	}


	public ModifyUserParam() {
		
	}
	
	
	public ModifyUserParam(String userid, String validdate, String postcode,
			String regioncode, String address, int sex, String description,
			String reservedinfo) {
		super();
		this.userid = userid;
		this.validdate = validdate;
		this.postcode = postcode;
		this.regioncode = regioncode;
		this.address = address;
		this.sex = sex;
		this.description = description;
		this.reservedinfo = reservedinfo;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getValiddate() {
		return validdate;
	}
	public void setValiddate(String validdate) {
		this.validdate = validdate;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getRegioncode() {
		return regioncode;
	}
	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	}