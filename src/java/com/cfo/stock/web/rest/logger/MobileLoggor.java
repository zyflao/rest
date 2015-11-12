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
public class MobileLoggor {
	protected static final Logger mologger=Logger.getLogger("moheader");
	
	public static void info(String msg){
		mologger.info(msg);
	}
}
