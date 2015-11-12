package com.cfo.stock.web.rest.result;

public class CurrentPostionResult{
	
	private String brokerName ;
	private String brokerLogo;
	private String realName;
	private String fundId;
	private String assetBalance;
	private String todayGenLose;
	private String percent;
	private String brokerId;
	
	public CurrentPostionResult() {
		// TODO Auto-generated constructor stub
	}
	
	public String getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getBrokerLogo() {
		return brokerLogo;
	}
	public void setBrokerLogo(String brokerLogo) {
		this.brokerLogo = brokerLogo;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getAssetBalance() {
		return assetBalance;
	}
	public void setAssetBalance(String assetBalance) {
		this.assetBalance = assetBalance;
	}
	public String getTodayGenLose() {
		return todayGenLose;
	}
	public void setTodayGenLose(String todayGenLose) {
		this.todayGenLose = todayGenLose;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	
}
