package com.cfo.stock.web.rest.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.logger.MobileLoggor;
import com.cfo.stock.web.rest.session.AttributeKeys;
import com.cfo.stock.web.rest.utils.IPUtils;
import com.jrj.common.cache.memcached.MemcachedCache;
import com.jrj.common.utils.ListUtil;



/**
 * @author kecheng.li
 *
 */
@Component
@Provider
@ServerInterceptor
public class AppLogPostIntetceptor implements PostProcessInterceptor {

	protected Log log = LogFactory.getLog(this.getClass());

	@Context
	HttpServletRequest servletRequest;

	@Context
	HttpServletResponse servletResponse;

	@Context
	HttpHeaders headers;

	@Autowired
	protected MemcachedCache sessionMemCache;

	/**
	 * 需要记录在日志里的手机信息字段，通过header传递
	 */
	static final String[] MO_HEADERS = { "devid", "paltid", "appver", "model",
			"localizedModel", "systemName", "systemVersion", "productid",
			"apps","channelId"};

	@Override
	public void postProcess(ServerResponse serverResponse) {

		String sessionId = null;
		String userId = null;
		String content = null;
		String idNumber = null;
		try {
			JSONObject jsonObject = new JSONObject();
			String uri = servletRequest.getRequestURI();
			String ip = IPUtils.getRemoteIpAdress(servletRequest);
			jsonObject.put("path", uri);
			jsonObject.put("ip", ip);

			String method = servletRequest.getMethod();
			String name = serverResponse.getResourceMethod().getName();
			// 如果是get请求方式 从url参数中直接获取sessionId
			if ("GET".equalsIgnoreCase(method)) {
				sessionId = servletRequest.getParameter("sessionId");
				if (StringUtils.isNotBlank(sessionId)) {
					userId = getUserId(sessionId);
				}
			}
			// 如果是Post请求方式
			// 注册登录时从返回结果中获取sessionId
			// 非注册登录时从Post请求体中获取
			if ("POST".equalsIgnoreCase(method)) {
				if ("login".equalsIgnoreCase(name)
						|| "mobileRegist".equalsIgnoreCase(name)) {
					String result = serverResponse.getEntity().toString();
					if (StringUtils.isNotBlank(result)) {
						JSONObject json = JSONObject.parseObject(result);
						int retcode = json.getInteger("retcode");
						if (retcode == 0) {
							if(json.getJSONObject("data")!= null){
								sessionId = json.getJSONObject("data").getString(
										"sessionId");
								if(StringUtils.isNotEmpty(sessionId)){
									userId = getUserId(sessionId);
								}							
							}
							
						}
					}
				} else {
					content = (String) servletRequest.getAttribute("content");
					if (StringUtils.isNotEmpty(content)) {
						JSONObject json = JSONObject.parseObject(content);
						sessionId = json.getString("sessionId");
						if (StringUtils.isNotEmpty(sessionId)) {
							userId = getUserId(sessionId);
						}
						// 以下针对找回密码 因为找回密码之前是非登陆状态无sessionId
						idNumber = json.getString("idNumber");
						if (StringUtils.isNotBlank(idNumber)) {
							JSONObject j = getUserInfo(idNumber);
							if(j != null){
								userId = j.getString("userid");
							}							
						}
					}
				}
			}
			if (StringUtils.isNotBlank(userId)) {
				jsonObject.put("userId", userId);
			}
			logHeaders(jsonObject);
		} catch (Exception e) {
			log.error("拦截器获取日志信息异常---" + e);
		}

	}

	/**
	 * 获取存储在memcache中的userId
	 * 
	 * @param sessionId
	 * @return
	 */
	private String getUserId(String sessionId) {
		if(StringUtils.isBlank(sessionId)){
			return null;
		}
		String key = sessionId + "_" + AttributeKeys.MO_USERID;
		String userId = (String) sessionMemCache.get(key);
		return userId;
	}

	/**
	 * 获取http请求header中的参数不
	 * 
	 * @param jsonObject
	 */
	protected void logHeaders(JSONObject jsonObject) {
		if (headers != null) {
			for (String headerName : MO_HEADERS) {
				List<String> drivers = headers.getRequestHeader(headerName);
				if (CollectionUtils.isNotEmpty(drivers)) {
					jsonObject.put(headerName, ListUtil.join(drivers, ","));
				}
			}
			if (jsonObject != null && jsonObject.size() > 0) {
				MobileLoggor.info(jsonObject.toJSONString());
			}
		}
	}
	
	private  JSONObject getUserInfo(String idNumber) {
		if (StringUtils.isBlank(idNumber)) {
			return new JSONObject();
		}
		String key = AttributeKeys.MO_FINDPWD_SESSION_KEY_PRE + "_" + idNumber;
		JSONObject json = (JSONObject) sessionMemCache.get(key);
		return json;
	}

}
