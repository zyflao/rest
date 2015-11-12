package com.cfo.stock.web.rest.result;

import org.springframework.beans.BeanUtils;

public class BindInfoResult  extends ModelConfigResult{
	
	//资金账号
	private String fundAccount;
	//绑定状态
	private String bindStauts;	
	//发生日期
	private long initDate;

	//客户编号
	private String clientId;
	//客户姓名
	private String clientName;
	
	private String dafaultBroker ;
	
	private String brokerId;

	public BindInfoResult() {
		
	}
	
	public BindInfoResult(ModelConfigResult modelConfigResult) {
		BeanUtils.copyProperties(modelConfigResult, this);
	}
	
	public String getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}
	public String getDafaultBroker() {
		return dafaultBroker;
	}
	public void setDafaultBroker(String dafaultBroker) {
		this.dafaultBroker = dafaultBroker;
	}

	public String getFundAccount() {
		return fundAccount;
	}
	public void setFundAccount(String fundAccount) {
		this.fundAccount = fundAccount;
	}
	public long getInitDate() {
		return initDate;
	}
	public void setInitDate(long initDate) {
		this.initDate = initDate;
	}
	public String getBindStauts() {
		return bindStauts;
	}
	public void setBindStauts(String bindStauts) {
		this.bindStauts = bindStauts;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	
}
