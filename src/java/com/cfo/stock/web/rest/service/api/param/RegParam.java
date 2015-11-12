package com.cfo.stock.web.rest.service.api.param;


/**   
*  注册参数   
* @className：RegParam   
* @classDescription：   
	
* @author：kecheng.Li
  
* @dateTime：2014年4月22日 下午7:23:31          
*/ 
public class RegParam extends Param {
	
	private String bindid;
	private String username;
//	必传参数
	private String mobileno;
	private String passwd;	
	private String validcode;
	
//	非必传参数	
	private String clientip;
	private String clientinfo;
	private int usedefaulttemplate;
	private String smstemplate;
	private String cccode;
	
	
	public RegParam() {
		// TODO Auto-generated constructor stub
	}
		
	public RegParam(String bindid, String username, String mobileno,
			String passwd, String clientip, String clientinfo) {
		super();
		this.bindid = bindid;
		this.username = username;
		this.mobileno = mobileno;
		this.passwd = passwd;
		this.clientip = clientip;
		this.clientinfo = clientinfo;
	}



	public RegParam(String mobileno, String passwd, String clientip,
			String clientinfo) {
		super();
		this.mobileno = mobileno;
		this.passwd = passwd;
		this.clientip = clientip;
		this.clientinfo = clientinfo;
		
	}

	public RegParam(String mobileno, String passwd,String validcode) {
		super();
		this.mobileno = mobileno;
		this.passwd = passwd;
		this.validcode = validcode;
		
	}
	
	
	public RegParam(String mobileno, String passwd, String validcode,
			String clientip, String clientinfo, int usedefaulttemplate,
			String smstemplate, String cccode) {
		super();
		this.mobileno = mobileno;
		this.passwd = passwd;
		this.validcode = validcode;
		this.clientip = clientip;
		this.clientinfo = clientinfo;
		this.usedefaulttemplate = usedefaulttemplate;
		this.smstemplate = smstemplate;
		this.cccode = cccode;
	}

	public int getUsedefaulttemplate() {
		return usedefaulttemplate;
	}

	public void setUsedefaulttemplate(int usedefaulttemplate) {
		this.usedefaulttemplate = usedefaulttemplate;
	}

	public String getSmstemplate() {
		return smstemplate;
	}

	public void setSmstemplate(String smstemplate) {
		this.smstemplate = smstemplate;
	}

	public String getCccode() {
		return cccode;
	}

	public void setCccode(String cccode) {
		this.cccode = cccode;
	}

	public String getValidcode() {
		return validcode;
	}



	public void setValidcode(String validcode) {
		this.validcode = validcode;
	}



	public String getBindid() {
		return bindid;
	}

	public void setBindid(String bindid) {
		this.bindid = bindid;
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

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getClientip() {
		return clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}

	public String getClientinfo() {
		return clientinfo;
	}

	public void setClientinfo(String clientinfo) {
		this.clientinfo = clientinfo;
	}
	
}
