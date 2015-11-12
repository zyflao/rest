package com.cfo.stock.web.rest.result;


public class StockAccountStatusResult {
	
	private String realName;//真实姓名
	private int idType;//证件类型
	private String idNo;//证件号码
	private String accountStatus;//账户状态
	private String brokerId;//券商Id
	private String createTime;//创建时间
	private String openTime;//开户成功时间
	private String fundId;//资金账号
	private String customerNo;//客户号	
	private String szStockAccount;
	private String shStockAccount;
	private int accountType;
	private String tradeableTime;
	private String brokerName;
	private String szStockAccountStatus;
	private String shStockAccountStatus;
	

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
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
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
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public int getIdType() {
		return idType;
	}
	public void setIdType(int idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
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
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
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
