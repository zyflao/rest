package com.cfo.stock.web.rest.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

@Provider 
public class StockExceptionHandler implements ExceptionMapper<Exception>{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Context
	HttpServletRequest servletRequest;
	
	@Context
	HttpServletResponse servletResponse;
	
	/*第一种:UnAvailableException 某一家券商停服    retcode -201
	 * 
	     第二种:NeedLoginException  包括A和B两种情况 统一返回给app  retcode -101
	     A.恒泰密码失效  accessToken无效或者找不到
	       ERROR_INVALID="-101";//access token invalid 
	       ERROR_NOTFUND="-102";//token not found
	     B.ITN类型的券商 
	       ERROR_INVALID="10001";//交易会话过期
	 	   ERROR_NOTFUND="10002";//交易会话无效
		   ERROR_FAIL="12";//会话失败
		   
	      第三种:NeedTradePwdException ITN类券商只能查询不能交易   retocde -301
	  	   ERROR_LIMIT="10006";//交易会话权限受限
	  	   
	      第四种:RpcException Dubbo调用超时                retocde -501
	     
	       第五种:TimeoutException Dubbo调用超时    retocde -501
	      
	      第六种:其他种类                                                                                retocde -1
	 */
	@Override
	public Response toResponse(Exception ex) {
		JSONObject json = new JSONObject();
		log.error("-->Exception:"+ex+"-->msg:"+ex.getMessage());
		ex.printStackTrace();
		Exception old = ex; 
		if(ex.getCause() != null && ex.getCause() instanceof Exception){
			ex = (Exception) ex.getCause();
		}
		log.info("old--->" + old.getClass());
		log.info("ex----> " + ex.getClass());	
		if(ex instanceof com.jrj.stocktrade.api.exception.UnAvailableException){//券商停服
			json.put("retcode",-201);
			json.put("msg", "券商停服");
			log.info("ex--->UnAvailableException");
			return Response.status(200).entity(json.toJSONString()).build();
		}else if(ex instanceof com.jrj.stocktrade.api.exception.NeedLoginException){
			json.put("retcode",-101);
			json.put("msg", "会话过期需重新登录认证");
			log.info("ex--->NeedLoginException");
			return Response.status(200).entity(json.toJSONString()).build();
		}else if(ex instanceof com.jrj.stocktrade.api.exception.NeedTradePwdException){
			json.put("retcode",-301);
			json.put("msg", "权限受限需重新登录认证");
			log.info("ex--->NeedTradePwdException");
			return Response.status(200).entity(json.toJSONString()).build();
		}else if(ex instanceof com.alibaba.dubbo.rpc.RpcException){
			json.put("retcode",-501);
			json.put("msg", "网络请求超时");	
			log.info("ex--->RpcException");
			return Response.status(200).entity(json.toJSONString()).build();
		}else if(ex instanceof com.alibaba.dubbo.remoting.TimeoutException){
			json.put("retcode",-501);
			json.put("msg", "网络请求超时");
			log.info("ex--->TimeoutException");
			return Response.status(200).entity(json.toJSONString()).build();
		}else{
			json.put("retcode", -1);
			json.put("msg", "服务器正忙，稍后再试");
			log.error("ex--->"+ex.getMessage(),ex);
			return Response.status(200).entity(json.toJSONString()).build();
		}				
	}

}
