package com.cfo.stock.web.rest.service.api.param;


/**   
*  用户唯一性参数    
* @className：UserUniqueParam   
* @classDescription：   
	
* @author：kecheng.Li
  
* @dateTime：2014年4月22日 下午7:28:27          
*/ 
public class UserUniqueParam extends Param{
	
	private String email;
	private String username;
	private String mobileno;
	private String idnumber;
	
	public UserUniqueParam(String email, String username, String mobileno,
			String idnumber) {
		super();
		this.email = email;
		this.username = username;
		this.mobileno = mobileno;
		this.idnumber = idnumber;
	}
	public UserUniqueParam() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getIdnumber() {
		return idnumber;
	}
	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}
	
	
}
