package com.cfo.stock.web.rest.result;

public class ModelConfigResult {
	
	private String buysell; //买卖
	
	private String transfer;//银证转账
	
	private String withdraw;//撤单
	
	private String entrust;//委托查询
	
	private String business;//成交查询
	
	private String fund;  //资金流水
	
	private String balance;
	
	private String tradeFlag;
	
	private String domainUrl;
	
	private String tradePassword;
	
	private String tradeJyPassword;
	
	private String tradeTxPassword;
	
	private String isInitialize;
	
	private String initPassword;
	
	private String initJyPassword;
	
	private String initTxPassword;
	
	private String reLoginAuthType;
	
	private ConfigPassWordResult configPassWordResult;

	
	public ModelConfigResult() {
		// TODO Auto-generated constructor stub
	}
	

	public ConfigPassWordResult getConfigPassWordResult() {
		return configPassWordResult;
	}


	public void setConfigPassWordResult(ConfigPassWordResult configPassWordResult) {
		this.configPassWordResult = configPassWordResult;
	}


	public String getIsInitialize() {
		return isInitialize;
	}

	public void setIsInitialize(String isInitialize) {
		this.isInitialize = isInitialize;
	}

	public String getTradePassword() {
		return tradePassword;
	}

	public void setTradePassword(String tradePassword) {
		this.tradePassword = tradePassword;
	}


	public String getTradeJyPassword() {
		return tradeJyPassword;
	}


	public void setTradeJyPassword(String tradeJyPassword) {
		this.tradeJyPassword = tradeJyPassword;
	}


	public String getTradeTxPassword() {
		return tradeTxPassword;
	}


	public void setTradeTxPassword(String tradeTxPassword) {
		this.tradeTxPassword = tradeTxPassword;
	}


	public String getInitPassword() {
		return initPassword;
	}


	public void setInitPassword(String initPassword) {
		this.initPassword = initPassword;
	}


	public String getInitJyPassword() {
		return initJyPassword;
	}

	public void setInitJyPassword(String initJyPassword) {
		this.initJyPassword = initJyPassword;
	}

	public String getInitTxPassword() {
		return initTxPassword;
	}

	public void setInitTxPassword(String initTxPassword) {
		this.initTxPassword = initTxPassword;
	}

	public String getReLoginAuthType() {
		return reLoginAuthType;
	}

	public void setReLoginAuthType(String reLoginAuthType) {
		this.reLoginAuthType = reLoginAuthType;
	}




	public ModelConfigResult(String buysell, String transfer, String withdraw,
			String entrust, String business, String fund,String balance) {
		super();
		this.buysell = buysell;
		this.transfer = transfer;
		this.withdraw = withdraw;
		this.entrust = entrust;
		this.business = business;
		this.fund = fund;
		this.balance = balance;
	}
		
	public String getDomainUrl() {
		return domainUrl;
	}

	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}

	public String getTradeFlag() {
		return tradeFlag;
	}

	public void setTradeFlag(String tradeFlag) {
		this.tradeFlag = tradeFlag;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getBuysell() {
		return buysell;
	}

	public void setBuysell(String buysell) {
		this.buysell = buysell;
	}

	public String getTransfer() {
		return transfer;
	}

	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}

	public String getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(String withdraw) {
		this.withdraw = withdraw;
	}

	public String getEntrust() {
		return entrust;
	}

	public void setEntrust(String entrust) {
		this.entrust = entrust;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getFund() {
		return fund;
	}

	public void setFund(String fund) {
		this.fund = fund;
	}
	
	
	
}
