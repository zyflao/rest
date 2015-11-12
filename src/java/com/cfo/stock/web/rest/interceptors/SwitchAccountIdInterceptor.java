package com.cfo.stock.web.rest.interceptors;

import java.io.ByteArrayInputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.utils.HttpMethodUtils;
import com.jrj.stocktrade.api.account.UserAccountService;
import com.jrj.stocktrade.api.account.vo.UserAccount;
import com.jrj.stocktrade.api.exception.ServiceException;

/**
 * @author kecheng.li
 * 
 *  采用拦截器将 userId brokerId 转换为 userId accountId
 */

@Component
@Provider
@ServerInterceptor
public class SwitchAccountIdInterceptor implements PreProcessInterceptor {

	protected Log log = LogFactory.getLog(this.getClass());

	@Context
	HttpServletRequest servletRequest; // 注入 HttpServletRequest

	@Context
	HttpServletResponse servletResponse; // 注入 HttpServletResponse

	@Autowired
	private UserAccountService userAccountService;

	@Override
	public ServerResponse preProcess(HttpRequest httpRequest,
			ResourceMethod resourceMethod) throws Failure,
			WebApplicationException {
		String methodName = httpRequest.getHttpMethod();//POST GET 
		Boolean flag = resourceMethod.getMethod().isAnnotationPresent(
				ChangeAccountId.class);
		/*Annotation[] annotations = resourceMethod.getMethod().getAnnotations();
		if(annotations != null && annotations.length>0){
			for (Annotation annotation :annotations) {
				log.info(annotation);
				log.info(annotation.toString());
			}
		}*/
		if (flag) {
				// 处理GET请求
				if ("GET".equalsIgnoreCase(methodName)) {
					String userId = servletRequest.getParameter("userId");
					long accountId = 0L;
//					由于命名不规范 部分接口 brokerId 部分接口是 broker 因此都需要获取
					String brokerId = servletRequest.getParameter("brokerId");
					String broker = servletRequest.getParameter("broker");
					accountId =getAccountId(methodName, brokerId, broker, userId);
					httpRequest.getHttpHeaders().getRequestHeaders().add("accountId",String.valueOf(accountId));
				}
			// 处理POST请求
			if ("POST".equalsIgnoreCase(methodName)) {
				String content = HttpMethodUtils.getPostMethodParam(httpRequest
						.getInputStream());
				if (StringUtils.isNotBlank(content)) {
					JSONObject json = JSONObject.parseObject(content);
					String userId = json.getString("userId");
					String brokerId = json.getString("brokerId");
					String broker = json.getString("broker");
					long accountId = getAccountId(methodName, brokerId, broker, userId);
					json.put("accountId", accountId);
					content = json.toJSONString();
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
							content.getBytes());
					httpRequest.setInputStream(byteArrayInputStream);					
					httpRequest.setAttribute("content",content);
					
				}
			}
		}
		return null;
	}

	private long getAccountId(String methodName, String brokerId, String broker,
			String userId) {
		long accountId = 0L;
		String brokerStr = "";
		try {
			if (StringUtils.isEmpty(brokerId)) {
				brokerStr = broker;
			} else {
				brokerStr = brokerId;
			}
			log.info("methodName is-->" + methodName
					+ "-->catch securitiesBroker is:" + brokerStr);
			if (brokerStr != null) {
				UserAccount userAccount = userAccountService.queryAccount(
						userId, brokerStr);
				log.info("methodName is-->" + methodName + "-->userAccount is:"
						+ JSONObject.toJSONString(userAccount));
				if (userAccount != null) {
					accountId = userAccount.getAccountId();
					log.info("methodName is-->" + methodName + "-->accountId is:"+accountId);
				}
			}
		} catch (ServiceException e) {
			log.error("methodName is-->" + methodName + "-->ServiceException"+ e.getErrorInfo(), e);
		} catch (Exception e) {
			log.error("methodName is-->" + methodName + "-->Exception "+ e.getMessage(), e);
		}
		return accountId;
	}

	// 加入转换ChangeAccountId注解
	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface ChangeAccountId {

	}
}
