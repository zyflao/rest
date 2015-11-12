package com.cfo.stock.web.rest.service.api.param;


/**
 * 验证码参数类
 *
 */
public class ValidCodeCheckParam extends Param {
	private String mobileno;
	private String codetype;
	private String validcode;
	
	public ValidCodeCheckParam() {
		super();
	}


	public ValidCodeCheckParam(String mobileno, String codetype, String validcode) {
		super();
		this.mobileno = mobileno;
		this.codetype = codetype;
		this.validcode = validcode;
	}


	public String getMobileno() {
		return mobileno;
	}


	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}


	public String getCodetype() {
		return codetype;
	}


	public void setCodetype(String codetype) {
		this.codetype = codetype;
	}


	public String getValidcode() {
		return validcode;
	}


	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}

	
}

