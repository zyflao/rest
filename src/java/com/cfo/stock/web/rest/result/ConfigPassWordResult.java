package com.cfo.stock.web.rest.result;


/** 交易时 交易密码 通讯密码配置
 *  超时或者过期重新登录初始化认证时  交易密码 通讯密码配置
 * @author Kecheng.Li
 *
 */
public class ConfigPassWordResult {
	
	private String tradePassword="true"; //默认交易需要密码
	private String tradeJyPassword="true";  //默认交易需要交易密码
	private String tradeTxPassword="false"; //默认交易不需要通讯密码
	
	private String isInitialize="false"; //是否需要重新初始化
	private String initJyPassword="true"; //如果需要初始化 默认需要交易密码
	private String initTxPassword="false";//如果需要初始化 默认不需要通讯密码
	
	private String reLoginAuthType = "";
	
	public ConfigPassWordResult() {
		// TODO Auto-generated constructor stub
	}

	public String getReLoginAuthType() {
		return reLoginAuthType;
	}

	public void setReLoginAuthType(String reLoginAuthType) {
		this.reLoginAuthType = reLoginAuthType;
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
	public String getIsInitialize() {
		return isInitialize;
	}
	public void setIsInitialize(String isInitialize) {
		this.isInitialize = isInitialize;
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
	
	

}
