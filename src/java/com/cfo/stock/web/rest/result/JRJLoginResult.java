package com.cfo.stock.web.rest.result;

import org.springframework.beans.BeanUtils;

public class JRJLoginResult extends ModelConfigResult {
	
	private String userId;
	private String sessionId;
	private String mobileNo;
	private String defaultBroker;
	private String fundAccount;
	private String status;
	private String realName;
	private String stockAccount;
	private String exchangeType;
	private String szStockAccount;
	private String shStockAccount;
	private String createTime ;
	private String openTime ;
	private int accountType;
	private String tradeableTime;
	private String brokerId;
	private String szStockAccountStatus;
	private String shStockAccountStatus;
	
	public JRJLoginResult() {
		// TODO Auto-generated constructor stub
	}
	
	public JRJLoginResult(ModelConfigResult configResult) {
		BeanUtils.copyProperties(configResult, this);
	}
	
	
	public String getSzStockAccountStatus() {
		return szStockAccountStatus;
	}

	public void setSzStockAccountStatus(String szStockAccountStatus) {
		this.szStockAccountStatus = szStockAccountStatus;
	}

	public String getShStockAccountStatus() {
		return shStockAccountStatus;
	}

	public void setShStockAccountStatus(String shStockAccountStatus) {
		this.shStockAccountStatus = shStockAccountStatus;
	}

	public String getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}
	public String getSzStockAccount() {
		return szStockAccount;
	}
	public void setSzStockAccount(String szStockAccount) {
		this.szStockAccount = szStockAccount;
	}
	public String getShStockAccount() {
		return shStockAccount;
	}
	public void setShStockAccount(String shStockAccount) {
		this.shStockAccount = shStockAccount;
	}
	public String getStockAccount() {
		return stockAccount;
	}
	public void setStockAccount(String stockAccount) {
		this.stockAccount = stockAccount;
	}
	public String getExchangeType() {
		return exchangeType;
	}
	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
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
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getDefaultBroker() {
		return defaultBroker;
	}
	public void setDefaultBroker(String defaultBroker) {
		this.defaultBroker = defaultBroker;
	}
	public String getFundAccount() {
		return fundAccount;
	}
	public void setFundAccount(String fundAccount) {
		this.fundAccount = fundAccount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	public String getTradeableTime() {
		return tradeableTime;
	}
	public void setTradeableTime(String tradeableTime) {
		this.tradeableTime = tradeableTime;
	}
}
