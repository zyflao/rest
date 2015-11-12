package com.cfo.stock.web.rest.globals;

import com.jrj.common.utils.PropertyManager;
/**   
*      
* @className：StockGlobals   
* @classDescription：   
	股票业务全局参数
* @author：kecheng.Li
  
* @dateTime：2014年4月19日 下午3:31:37          
*/ 
public class StockGlobals {
	public static final String STOCK_LOGIN_OK="ok";
	//股票封装服务
	public static final String STOCK_REST_USER_CENTER_SERVICE = PropertyManager.getString("stock_rest_user_center_service");
	public static final String STOCK_REST_USER_CENTER_INVITATION = PropertyManager.getString("stock_rest_user_center_invitation");
	public static final String SESSION_EXPIRED=PropertyManager.getString("stock.rest.sessiontime");
	public static final long expire_time=Long.parseLong(SESSION_EXPIRED)*60*1000;
	public static final String STOCK_WEB_DOMAIN=PropertyManager.getString("stock.rest.domain");
	public static final String BIND_BROKER_INIT=PropertyManager.getString("bind_broker_init");
	public static final String BIND_CALLBACK_URL=PropertyManager.getString("bind_callBack_url");
	
}
