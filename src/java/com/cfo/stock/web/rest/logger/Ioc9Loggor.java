package com.cfo.stock.web.rest.logger;

import org.apache.log4j.Logger;


/**   
*      
* @className：MobileLoggor   
* @classDescription：   
	 移动端客户信息日志
* @author：kecheng.Li
  
* @dateTime：2014年4月19日 下午6:12:54          
*/ 
public class Ioc9Loggor {
	protected static final Logger ioc9Log=Logger.getLogger("ioc9");
	
	public static void info(String msg){
		ioc9Log.info(msg);
	}

	public static void error(String msg) {
		ioc9Log.error(msg);
	}
}
