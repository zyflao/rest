package com.cfo.stock.web.rest.vo;

/**
 * 用户信息VO
 * @author hailong.qu
 *
 */
public class UserInfoVo extends BaseVo{
	
	private String userId;
	private String realName;
	private String userName;

	private String  userstatus;
	private String uniquecode;
	private Integer uncommonword;//是否包含生僻字，0：不包含，1：包含
	private Integer companyuser=0;//是否公司员工,0:不是，1：是
	private String  reservedinfo ;
	public UserInfoVo() {
		// TODO Auto-generated constructor stub
	}
	
	
	public UserInfoVo(String userId, String realName, String userName,
			String userstatus, String uniquecode, Integer uncommonword,
			Integer companyuser, String reservedinfo) {
		super();
		this.userId = userId;
		this.realName = realName;
		this.userName = userName;
		this.userstatus = userstatus;
		this.uniquecode = uniquecode;
		this.uncommonword = uncommonword;
		this.companyuser = companyuser;
		this.reservedinfo = reservedinfo;
	}


	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserstatus() {
		return userstatus;
	}
	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
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
	public String getReservedinfo() {
		return reservedinfo;
	}
	public void setReservedinfo(String reservedinfo) {
		this.reservedinfo = reservedinfo;
	}
	

	
}

