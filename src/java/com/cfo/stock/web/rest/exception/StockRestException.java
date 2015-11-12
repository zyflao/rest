/**
 * 
 */
package com.cfo.stock.web.rest.exception;

import com.jrj.common.exception.JrjBaseException;

/**
 * @author coldwater
 *
 */
@SuppressWarnings("serial")
public class StockRestException extends JrjBaseException {
	
	private int retcode;
	private String msg;
	
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

	public StockRestException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StockRestException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public StockRestException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public StockRestException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
