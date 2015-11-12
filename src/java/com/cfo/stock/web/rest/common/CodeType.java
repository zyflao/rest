package com.cfo.stock.web.rest.common;


/**   
*   验证码类型   
* @className：CodeType   
* @classDescription：   
	
* @author：kecheng.Li
  
* @dateTime：2014年4月22日 下午6:53:18          
*/ 
public enum  CodeType {
	
//	注册验证码
	REGISTER("201"),
	
//	手机找回密码
	FINDPASSWORD("205"),
	
//	重置密码
	RESETPASSWORD("253");
	
	public final String type;
	
	private CodeType(String type){
		this.type = type;
	}
	
}
