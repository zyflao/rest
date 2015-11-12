package com.cfo.stock.web.rest.common;


/**   
*  开户状态    
* @className：OpenStatus   
* @classDescription：   
	
* @author：kecheng.Li
  
* @dateTime：2014年4月22日 下午8:03:28          
*/ 
public enum OpenStatus {
	OPEN(1),
	UNOPEN(0),
	WAIT(-1);
	
	public final int status;
	private OpenStatus(int status){
		this.status=status;
	}
}
