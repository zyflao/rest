package com.cfo.stock.web.rest.interceptors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jrj.common.cache.memcached.MemcachedCache;



/**
 * @author kecheng.li
 *
 */
@Component
@Provider
@ServerInterceptor
public class SecuritiesInfoInterceptor implements PreProcessInterceptor {

	protected Log log = LogFactory.getLog(this.getClass());
	private static final String MO_SECURITES_INFO = "MO_SECURITES_INFO";

	@Context
	HttpServletRequest servletRequest;
	@Context
	HttpServletResponse servletResponse;

	@Autowired
	protected MemcachedCache sessionMemCache;

	@Override
	public ServerResponse preProcess(HttpRequest request,
			ResourceMethod resourceMethod) throws Failure,
			WebApplicationException {
		/*String sessionId = null;
		String info = null;
		try{
			String methodName = resourceMethod.getMethod().getName();
			String method = request.getHttpMethod();
			// 处理POST请求
			if ("POST".equalsIgnoreCase(method)) {
				// 登陆注册接口拦截之前没有sessionId 不对登陆注册接口做拦截
				if (!"login".equalsIgnoreCase(methodName)
						&& (!"mobileRegist".equalsIgnoreCase(methodName))) {
					String content = getPostMethodParam(request.getInputStream());
					if (StringUtils.isNotEmpty(content)) {
						JSONObject json = JSONObject.parseObject(content);
						sessionId = json.getString("sessionId");
						if (StringUtils.isNotEmpty(sessionId)) {
							info = getSecuritiesInfo(sessionId);
							if (StringUtils.isNotBlank(info)) {
								JSONObject j = JSONObject.parseObject(info);
								String devId = j.getString("devId");
								String mobileNo = j.getString("mobileNo");
								HsRpcContext.setMobileDevid(devId);
								HsRpcContext.setMobile(mobileNo);
							}
						}

					}
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
							content.getBytes());
					request.setInputStream(byteArrayInputStream);
					request.setAttribute("content", content);
				}

			}
			// 处理GET请求
			if ("GET".equalsIgnoreCase(method)) {
				sessionId = servletRequest.getParameter("sessionId");
				if (StringUtils.isNotBlank(sessionId)) {
					info = getSecuritiesInfo(sessionId);
					if (info != null) {
						JSONObject j = JSONObject.parseObject(info);
						String devId = j.getString("devId");
						String mobileNo = j.getString("mobileNo");
						HsRpcContext.setMobileDevid(devId);
						HsRpcContext.setMobile(mobileNo);
					}
				}
			}
		}catch(Exception e){
			log.error("拦截器获取日志信息异常---" + e);
		}
		*/

		return null;
	}

	/**
	 * 获取post请求的参数 因为post请求体式 json格式的 要把Io流解析成 json格式的字符串
	 * 
	 * @param inputStream
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getPostMethodParam(InputStream inputStream) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		String conent = null;
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			conent = sb.toString();
		} catch (IOException e) {
			log.error("获取post参数Io异常--" + e.getMessage());
		} catch (Exception e) {
			log.error("获取post参数--" + e.getMessage());
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (IOException e) {
				log.error("关闭BufferedReader时异常---" + e.getMessage());
			}
		}
		return conent;
	}

	@SuppressWarnings("unused")
	private String getSecuritiesInfo(String sessionId) {
		if (StringUtils.isEmpty(sessionId)) {
			return "";
		}
		String key = sessionId + "_" + MO_SECURITES_INFO;
		String json = (String) sessionMemCache.get(key);
		return json;
	}
}
