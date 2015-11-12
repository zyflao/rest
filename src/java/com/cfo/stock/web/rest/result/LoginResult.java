package com.cfo.stock.web.rest.result;

public class LoginResult {
	// 用户ID 也有可以能用户名
	private String userid;
	private String realname;
	private String uniquecode;
	// 是否为员工，0：不是，1：是
	private Integer companyuser;

	// 注册时间 yyyy-MM-dd HH:mm:ss
	private String regtime;
	// 1:已注册，-2:冻结,-1:未激活
	private Integer userstatus;
	// 登录失败次数
	private Integer failtimes;
	// 登录冻结剩余秒数
	private Integer frozenremainseconds;
	// 最后成功登录时间 yyyy-MM-dd HH:mm:ss
	private String lastsuccesstime;
	// 开户状态
	private Integer openstatus;
	// sessionid
	private String sessionId;

	private String mobileno;

	private String username;

	private int hasidinfo = 0;
	// 是否含有生僻字
	private int uncommonword;
	// 绑定状态
	private int bindStatus;
//	绑定身份证状态 1未绑 2已绑 
	private int bindId; 
//  默认券商
	private String deafultBroker;
//	资金账号
	private String fundAccount;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getOpenstatus() {
		return openstatus;
	}

	public void setOpenstatus(Integer openstatus) {
		this.openstatus = openstatus;
	}
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}
	public Integer getFailtimes() {
		return failtimes;
	}

	public void setFailtimes(Integer failtimes) {
		this.failtimes = failtimes;
	}

	public Integer getFrozenremainseconds() {
		return frozenremainseconds;
	}

	public void setFrozenremainseconds(Integer frozenremainseconds) {
		this.frozenremainseconds = frozenremainseconds;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getHasidinfo() {
		return hasidinfo;
	}

	public void setHasidinfo(int hasidinfo) {
		this.hasidinfo = hasidinfo;
	}

	public int getUncommonword() {
		return uncommonword;
	}

	public void setUncommonword(int uncommonword) {
		this.uncommonword = uncommonword;
	}

	public String getUniquecode() {
		return uniquecode;
	}

	public void setUniquecode(String uniquecode) {
		this.uniquecode = uniquecode;
	}

	public Integer getCompanyuser() {
		return companyuser;
	}

	public void setCompanyuser(Integer companyuser) {
		this.companyuser = companyuser;
	}

	public String getRegtime() {
		return regtime;
	}

	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}

	public Integer getUserstatus() {
		return userstatus;
	}

	public void setUserstatus(Integer userstatus) {
		this.userstatus = userstatus;
	}

	public String getLastsuccesstime() {
		return lastsuccesstime;
	}

	public void setLastsuccesstime(String lastsuccesstime) {
		this.lastsuccesstime = lastsuccesstime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(int bindStatus) {
		this.bindStatus = bindStatus;
	}

	public int getBindId() {
		return bindId;
	}

	public void setBindId(int bindId) {
		this.bindId = bindId;
	}

	public String getDeafultBroker() {
		return deafultBroker;
	}

	public void setDeafultBroker(String deafultBroker) {
		this.deafultBroker = deafultBroker;
	}

	public String getFundAccount() {
		return fundAccount;
	}

	public void setFundAccount(String fundAccount) {
		this.fundAccount = fundAccount;
	}
	
	

}
