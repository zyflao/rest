package com.cfo.stock.web.rest.result;

public class TradeIframeResult {
	
	private String url;
	private String data;
	private String sign;
	private String method;
	private String callUrl;
	private String submitUrl;
	private String param;
	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public TradeIframeResult() {
		// TODO Auto-generated constructor stub
	}
		
	public TradeIframeResult(String url, String data, String sign) {
		super();
		this.url = url;
		this.data = data;
		this.sign = sign;
	}

	public TradeIframeResult(String url, String data, String sign,
			String method, String callUrl, String submitUrl) {
		super();
		this.url = url;
		this.data = data;
		this.sign = sign;
		this.method = method;
		this.callUrl = callUrl;
		this.submitUrl = submitUrl;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	

	
	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}


	public String getCallUrl() {
		return callUrl;
	}


	public void setCallUrl(String callUrl) {
		this.callUrl = callUrl;
	}


	public String getSubmitUrl() {
		return submitUrl;
	}


	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}



}
