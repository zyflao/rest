package com.cfo.stock.web.rest.vo;

/**
 * 用户信息VO
 * @author hailong.qu
 *
 */
public class UserControlledInfoVo extends BaseVo{
	
	private String userId;
	private String realname;	
	private String idnumber;
	private String validDate;
	private String mobileno;
	private String email;
	private String postcode;
	private String address;
	private String regioncode;
	private String userstatus;
	
	public UserControlledInfoVo() {
		// TODO Auto-generated constructor stub
	}
		
	public UserControlledInfoVo(String userId, String realName,
			String idnumber, String validDate, String mobileno, String email,
			String postcode, String address, String regioncode,
			String userstatus) {
		super();
		this.userId = userId;
		this.realname = realName;
		this.idnumber = idnumber;
		this.validDate = validDate;
		this.mobileno = mobileno;
		this.email = email;
		this.postcode = postcode;
		this.address = address;
		this.regioncode = regioncode;
		this.userstatus = userstatus;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRealName() {
		return realname;
	}
	public void setRealName(String realName) {
		this.realname = realName;
	}
	public String getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	public String getValidDate() {
		return validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
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
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRegioncode() {
		return regioncode;
	}
	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
	}
	public String getUserstatus() {
		return userstatus;
	}
	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}
	

	
}

