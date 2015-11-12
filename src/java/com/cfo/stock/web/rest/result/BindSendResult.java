package com.cfo.stock.web.rest.result;

public class BindSendResult {
	private String s;
	private String q;
	private String data;
	private String sign;
	private String url;
	private String param;
	private String callUrl;
	private String method;
	private String p;

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	// 错误代码
	private int errorNo;
	// 错误信息
	private String errorInfo;
	
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
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(int errorNo) {
		this.errorNo = errorNo;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	

}
