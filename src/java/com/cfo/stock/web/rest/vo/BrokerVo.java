package com.cfo.stock.web.rest.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BrokerVo  implements Serializable{
	/**
	 * 券商名字
	 */
	private String brokerName;	
	/**
	 * 简写名字
	 */
	private String sname;
	/**
	 * 客服电话
	 */
	private String tailno;
	/**
	 * 简介
	 */
	private String intro;
	/**
	 * 券商主页
	 */
	private String href;
	/**
	 * 券商标识ID
	 */
	private String brokerId;
	/**
	 * 是否可用
	 */
	private int status;
	/**
	 * 券商枚举标识
	 */
	private String broker;
	/**
	 * 是否默认
	 */
	private boolean def;
	/**
	 * 资金账户
	 */
	private String fundAccount;
	/**
	 * 分支机构
	 */
	private String branchName;
	public String getBrokerName() {
		return brokerName;
	}
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getTailno() {
		return tailno;
	}
	public void setTailno(String tailno) {
		this.tailno = tailno;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isDef() {
		return def;
	}
	public void setDef(boolean def) {
		this.def = def;
	}
	public String getFundAccount() {
		return fundAccount;
	}
	public void setFundAccount(String fundAccount) {
		this.fundAccount = fundAccount;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBroker() {
		return broker;
	}
	public void setBroker(String broker) {
		this.broker = broker;
	}
	
}
