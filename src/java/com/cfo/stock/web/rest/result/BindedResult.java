package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.account.vo.Broker;

public class BindedResult {
	private List<Broker> bindedList = new ArrayList<Broker>();
	
	private String branchName ;
	private String fundAccount ;
	private String realname ;
	public List<Broker> getBindedList() {
		return bindedList;
	}
	public void setBindedList(List<Broker> bindedList) {
		this.bindedList = bindedList;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getFundAccount() {
		return fundAccount;
	}
	public void setFundAccount(String fundAccount) {
		this.fundAccount = fundAccount;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	
}
