package com.cfo.stock.web.rest.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.cfo.stock.web.rest.service.api.UserStockServiceAPI;
import com.jrj.common.service.AbstractBaseService;


/**   
*      
* @className：AbstractStockService   
* @classDescription：   
	股票业务基类
* @author：kecheng.Li
  
* @dateTime：2014年4月19日 下午2:10:45          
*/ 
public abstract class AbstractStockService extends AbstractBaseService {
	
		//用户模块接口
		@Autowired
		protected UserStockServiceAPI userStockApi;
		
		//股票交易 委托 成交查询模块接口
//		@Autowired
//		protected StockSecurityServiceAPI  securityServiceApi;
//				
//		//股票账户 绑定券商模块接口
//		@Autowired
//		protected AccountStockServiceAPI stockServiceApi ;
//		
//		//银证转账模块接口
//		@Autowired
//		protected BankTransServiceAPI bankTransServiceApi;
		
		
		
		
}
