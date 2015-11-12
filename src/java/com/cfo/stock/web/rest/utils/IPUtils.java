package com.cfo.stock.web.rest.utils;

import javax.servlet.http.HttpServletRequest;


/**   
*  获取客户端IP   
* @className：IPUtils   
* @classDescription：   
	
* @author：kecheng.Li
  
* @dateTime：2014年4月22日 下午8:22:20          
*/ 
public class IPUtils {
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
