package com.cfo.stock.web.rest.result;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cfo.stock.web.rest.vo.UserControlledInfoVo;
import com.cfo.stock.web.rest.vo.UserInfoVo;

/**
 * 用户信息VO
 * @author hailong.qu
 *
 */
public class UserInfoResult extends Result<UserControlledInfoVo>{
	
	private String userid;
	private String realname;
	private String username;
	private String idnumber;
	private String validdate;
	private Integer risklevel;
	private String mobileno;
	private String email;
	private String postcode;
	private String address;
	private String regioncode;
	private String opentime;
	private String statusStr;
	private String userstatus;
	private String uniquecode;
	private Integer uncommonword;//是否包含生僻字，0：不包含，1：包含
	private Integer companyuser;//是否公司员工,0:不是，1：是
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public String getValiddate() {
		return validdate;
	}

	public void setValiddate(String validdate) {
		this.validdate = validdate;
	}

	public Integer getRisklevel() {
		return risklevel;
	}

	public void setRisklevel(Integer risklevel) {
		this.risklevel = risklevel;
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

	public String getOpentime() {
		return opentime;
	}

	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getUniquecode() {
		return uniquecode;
	}

	public void setUniquecode(String uniquecode) {
		this.uniquecode = uniquecode;
	}


	public Integer getUncommonword() {
		return uncommonword;
	}

	public void setUncommonword(Integer uncommonword) {
		this.uncommonword = uncommonword;
	}

	public Integer getCompanyuser() {
		return companyuser;
	}

	public void setCompanyuser(Integer companyuser) {
		this.companyuser = companyuser;
	}

	public String getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}


	public UserControlledInfoVo parse() {
		Date date = new Date(Long.parseLong(validdate));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		validdate = sdf.format(date);
		return new UserControlledInfoVo(userid,realname,
				idnumber,validdate, mobileno,email,
				postcode,address, regioncode,
				userstatus);
	}
	
	public UserInfoVo parseUserInfo() {
		return null;
	}
}

