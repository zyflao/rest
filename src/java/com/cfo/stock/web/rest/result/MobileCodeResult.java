package com.cfo.stock.web.rest.result;

public class MobileCodeResult{
	private Long expiredtime;
	
	private String verifyCode;
	private String mobileNo;
	
	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Long getExpiredtime() {
		return expiredtime;
	}

	public void setExpiredtime(Long expiredtime) {
		this.expiredtime = expiredtime;
	}
	
}
