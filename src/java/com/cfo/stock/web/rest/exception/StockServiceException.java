package com.cfo.stock.web.rest.exception;

import com.jrj.common.exception.JrjBaseException;


/**   
*      
* @className：StockServiceException   
* @classDescription：   
	
* @author：kecheng.Li
  
* @dateTime：2014年4月19日 下午7:02:53          
*/ 
public class StockServiceException  extends JrjBaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int retcode;
	private String msg;
	
	public StockServiceException() {
		// TODO Auto-generated constructor stub
	}
	
	public StockServiceException(int retcode, String msg) {
		super(msg);
		this.retcode = retcode;
		this.msg = msg;
	}
	public int getRetcode() {
		return retcode;
	}
	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
