package com.cfo.stock.web.rest.result;


import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.jrj.stocktrade.api.account.vo.Broker;

@SuppressWarnings("serial")
public class BrokerEx extends Broker implements Serializable,Cloneable {
	
	private String mark; //券商小i提示
	private String tip = "";  //券商额外信息
	private String open; //券商是否 有开户功能
	private String transfer ; //券商是否有转户功能
	private String bind; //券商是否有绑定功能

	private String brokerLogo; //券商LOGO
	private String selfBindFlag;
	private Integer order; //券商排序
	private int recommend = 0;
	private String newStr;
	private String bindOther;
	private String bindSelf;
	private ConfigPassWordResult configPassWordResult;
	private String ZQTProtocol="";
	private String ZXZQProtocol="";
	
	
	public BrokerEx() {
		// TODO Auto-generated constructor stub
	}
	
	public BrokerEx(String mark, String tip, String open, String transfer,
			String bind,Broker broker) {
		this.mark = mark;
		this.tip = tip;
		this.open = open;
		this.transfer = transfer;
		this.bind = bind;
		BeanUtils.copyProperties(broker, this);
	}	
	
	public String getZQTProtocol() {
		return ZQTProtocol;
	}

	public void setZQTProtocol(String zQTProtocol) {
		ZQTProtocol = zQTProtocol;
	}

	public String getZXZQProtocol() {
		return ZXZQProtocol;
	}

	public void setZXZQProtocol(String zXZQProtocol) {
		ZXZQProtocol = zXZQProtocol;
	}

	public ConfigPassWordResult getConfigPassWordResult() {
		return configPassWordResult;
	}

	public void setConfigPassWordResult(ConfigPassWordResult configPassWordResult) {
		this.configPassWordResult = configPassWordResult;
	}

	public String getBindSelf() {
		return bindSelf;
	}

	public void setBindSelf(String bindSelf) {
		this.bindSelf = bindSelf;
	}

	public String getBindOther() {
		return bindOther;
	}

	public void setBindOther(String bindOther) {
		this.bindOther = bindOther;
	}

	public String getNewStr() {
		return newStr;
	}

	public void setNewStr(String newStr) {
		this.newStr = newStr;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getSelfBindFlag() {
		return selfBindFlag;
	}

	public void setSelfBindFlag(String selfBindFlag) {
		this.selfBindFlag = selfBindFlag;
	}

	public String getBrokerLogo() {
		return brokerLogo;
	}

	public void setBrokerLogo(String brokerLogo) {
		this.brokerLogo = brokerLogo;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getTip() {
		if(StringUtils.isBlank(tip)){
			tip = "";
		}
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getTransfer() {
		return transfer;
	}

	public void setTransfer(String transfer) {
		this.transfer = transfer;
	}

	public String getBind() {
		return bind;
	}

	public void setBind(String bind) {
		this.bind = bind;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
}
