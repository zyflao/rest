package com.cfo.stock.web.rest.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.springframework.stereotype.Component;

import com.cfo.stock.web.rest.utils.BusinessUtils;
import com.cfo.stock.web.rest.utils.HttpHeaderUtils;
import com.jrj.stocktrade.api.rpc.HsRpcContext;
@Component
@Provider
@ServerInterceptor
public class HsRpcInterceptor implements PreProcessInterceptor{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Context
	HttpServletRequest servletRequest;
	
	@Context
	HttpHeaders headers;
	
	@Override
	public ServerResponse preProcess(HttpRequest request, ResourceMethod resource)
			throws Failure, WebApplicationException {
		//注入用户来源
		HsRpcContext.setUserApps("stockrest");
		/*
			IOS
				  productid      1019012   炒股必备 
               				     1020021    爱投顾
			Android
                  productid      1019025 炒股必备  
                                 1020025 爱投顾			
		*/
		String headerValue = HttpHeaderUtils.getHeaderValue(headers, "productid");
		
		String businessValue = BusinessUtils.getBusinessByProductId(headerValue);
		
		log.info("businessValue---"+businessValue);
		
		HsRpcContext.setSubApp(businessValue);		
		//注入用户ip
		HsRpcContext.setUserIP(getRemoteIpAdress(servletRequest));
		
		return null;
	}
	/**
	 * 获得客户端IP
	 * 
	 * @return String
	 */
	public static String getRemoteIpAdress(HttpServletRequest request){
			// 如果通过反向代理访问的服务器,则先取x-forwarded-for的header
			String ip =request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("WL-Proxy-Client-IP");
				}
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getRemoteAddr();
				}
//				// 否则返回J2EE的地址
//				return request.getRemoteAddr();
			}			
				// 如果经过了多级代理,x-forwarded-for中有多个IP地址,则取第一个不为unknown的
				if (ip != null&&ip.indexOf(",") != -1) {
					String[] address = ip.split(",");
					ip = address[0];
					for (int i = 0; i < address.length; i++) {
						if (!"unknown".equalsIgnoreCase(address[i].trim())) {
							ip = address[i].trim();
							break;
						}
					}
				}
			return ip;
	}
	
}
