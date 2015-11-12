package com.cfo.stock.web.rest.service.api.param;



/**   
*    获取验证码  
* @className：ValidCodeGetParam   
* @classDescription：   
	
* @author：kecheng.Li
  
* @dateTime：2014年4月22日 下午7:02:55          
*/ 
public class ValidCodeGetParam extends Param{
	private String mobileno;
	private String codetype;
	
	public ValidCodeGetParam(String mobileno,String codetype) {
		this.mobileno = mobileno;
		this.codetype = codetype;
	}
	public ValidCodeGetParam() {
		// TODO Auto-generated constructor stub
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
}
