package com.cfo.stock.web.rest.result;

public class PassPortLoginResult {
	
	private String userId;
	private String sessionId;
	private long accountId;
	private String flag;
	private String mobileNo;
	private String isCompleted;
	private String isOpen;
	private String brokerId;
	private String realName;
	private String idNumber;
	private String servicePhone;
	private String fundAccount;
	private String isInvitateCode;//是否需要调起邀请码
	private String invitateCode;//已填写-邀请码
	private String brokerType; //券商类型  "0"=自接               "1"=恒生/ITN
	private String tradeWay; //交易类型  "0"自接交易   "1"ITN交易
	
	public String getTradeWay() {
		return tradeWay;
	}
	public void setTradeWay(String tradeWay) {
		this.tradeWay = tradeWay;
	}
	public String getBrokerType() {
		return brokerType;
	}
	public void setBrokerType(String brokerType) {
		this.brokerType = brokerType;
	}
	public String getInvitateCode() {
		return invitateCode;
	}
	public void setInvitateCode(String invitateCode) {
		this.invitateCode = invitateCode;
	}
	public String getIsInvitateCode() {
		return isInvitateCode;
	}
	public void setIsInvitateCode(String isInvitateCode) {
		this.isInvitateCode = isInvitateCode;
	}
	public String getFundAccount() {
		return fundAccount;
	}
	public void setFundAccount(String fundAccount) {
		this.fundAccount = fundAccount;
	}
	public String getServicePhone() {
		return servicePhone;
	}
	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}
	public String getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	public String getIsCompleted() {
		return isCompleted;
	}
	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	
}
