package com.cfo.stock.web.rest.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jrj.common.net.NetException;
import com.jrj.common.net.SimpleRestClient.HttpMethod;

/**
 * simple client api for restful web service request( get or post or put or
 * delete) 改写为使用Myjrj-common提供底层支持
 * 
 * @author wangxm
 * @author 冷水
 * @author 陈延民
 * 
 */
public class SimpleRestClientUtil {

	static Log log = LogFactory.getLog(SimpleRestClientUtil.class.getName());
	private static HttpConnectionClient connClient = null;
	private static Map<String, String> headParams = new HashMap<String, String>();
	public static HttpConnectionClient getHttpConnectionClient() {
		if (connClient == null) {
			// 使用默认配置
			connClient = new HttpConnectionClient();
			connClient.setConnectionTimeOut(20000);
			connClient.setConnectionMaxTotal(300);
			connClient.setConnectionMaxPerHost(80);
			connClient.setSoTimeOut(Integer.MAX_VALUE);
			connClient.setCodeing("UTF-8");
			connClient.setCharset("UTF-8");
			connClient.setHeadParams(headParams);
			
		}
		return connClient;
	}

	/**
	 * 执行get方法，返回状态大于等于400时会在后台打印异常，不抛出
	 * 
	 * @param uri
	 * @param httpConnectionClient
	 * @return
	 */
	public static String doGet(String uri) {
		long start = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("doGet->uri=" + uri);
		}
		String result = null;
		if (null == uri)
			return null;
		try {
			result = getHttpConnectionClient().getContextByGetMethod(uri);
		} catch (Exception e) {
			log.error("uri="+uri+" get请求错误："+e.getMessage());
			e.printStackTrace();
			if (e instanceof NetException){
				throw (NetException)e;
			}
		}
		
		long time = System.currentTimeMillis() - start;
		if(time > 500){
			log.info("get请求耗时  url="+uri+ ", time=" + time);
		}

		return result;
	}

	/**
	 * 兼容旧的调用
	 * @param uri
	 * @param requestBodyData
	 * @return
	 */
	public static String doPost(String uri, String requestBodyData) {
		return doMethodStatus(uri, requestBodyData, HttpMethod.POST, MediaType.APPLICATION_JSON_TYPE, "UTF-8");
	}

	/**
	 * post请求
	 * @param uri
	 * @param requestBodyData
	 * @param mediaType
	 * @param encodeType
	 * @param isEntity true返回实体内容。false返回状态码
	 * @return
	 */
	public static String doPost(String uri, String requestBodyData, MediaType mediaType, String encodeType, boolean isEntity) {
		if(isEntity){
			return doMethodEntity(uri, requestBodyData, HttpMethod.POST, mediaType, encodeType);
		}else{
			return doMethodStatus(uri, requestBodyData, HttpMethod.POST, mediaType, encodeType);
		}
	}

	/**
	 * 兼容旧的调用
	 * @param uri
	 * @param requestBodyData
	 * @return
	 */
	public static String doPut(String uri, String requestBodyData) {
		return doMethodStatus(uri, requestBodyData, HttpMethod.PUT, MediaType.APPLICATION_JSON_TYPE, "UTF-8");
	}
	
	/**
	 * put请求
	 * @param uri
	 * @param requestBodyData
	 * @param mediaType
	 * @param encodeType
	 * @param isEntity true返回实体内容。false返回状态码
	 * @return
	 */
	public static String doPut(String uri, String requestBodyData, MediaType mediaType, String encodeType, boolean isEntity) {
		if(isEntity){
			return doMethodEntity(uri, requestBodyData, HttpMethod.PUT, mediaType, encodeType);
		}else{
			return doMethodStatus(uri, requestBodyData, HttpMethod.PUT, mediaType, encodeType);
		}
	}

	/**
	 * 兼容旧的调用
	 * @param uri
	 * @param requestBodyData
	 * @return
	 */
	public static String doDelete(String uri, String requestBodyData) {
		return doMethodStatus(uri, requestBodyData, HttpMethod.DELETE, MediaType.APPLICATION_JSON_TYPE, "UTF-8");
	}

	/**
	 * delete请求
	 * @param uri
	 * @param requestBodyData
	 * @param mediaType
	 * @param encodeType
	 * @param isEntity true返回实体内容。false返回状态码
	 * @return
	 */
	public static String doDelete(String uri, String requestBodyData, MediaType mediaType, String encodeType, boolean isEntity) {
		if(isEntity){
			return doMethodEntity(uri, requestBodyData, HttpMethod.DELETE, mediaType, encodeType);
		}else{
			return doMethodStatus(uri, requestBodyData, HttpMethod.DELETE, mediaType, encodeType);
		}
	}
	
	/**
	 * 兼容旧的调用
	 * @param uri
	 * @param requestBodyData
	 * @param mediaType
	 * @return
	 */
	public static String doPostForm(String uri, String requestBodyData, MediaType mediaType) {
		return doMethodEntity(uri, requestBodyData, HttpMethod.POST, mediaType, "UTF-8");
	}

	/**
	 * 执行方法并返回状态码
	 * @param uri
	 * @param requestBodyData
	 * @param method
	 * @param mediaType
	 * @param encodeType
	 * @return
	 */
	private static String doMethodStatus(String uri, String requestBodyData,
			HttpMethod method, MediaType mediaType, String encodeType) {
		long startMillis = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("doMethod[" + method + "]->uri=" + uri
					+ ", requestBodyData=" + requestBodyData);
		}
		int httpStatusCode = -1;
		if (null == uri)
			return null;
		requestBodyData = requestBodyData == null ? "" : requestBodyData;
		try {
			switch (method) {
			case POST:
				httpStatusCode = getHttpConnectionClient() .getStatusByPostMethod(
						uri, mediaType.toString(), encodeType, requestBodyData);
				break;
			case PUT:
				httpStatusCode = getHttpConnectionClient() .getStatusByPutMethod(uri,
						mediaType.toString(), encodeType, requestBodyData);
				break;
			case DELETE:
				httpStatusCode = getHttpConnectionClient() .getStatusByDeleteMethod(
						uri, mediaType.toString(), encodeType, requestBodyData);
				break;
			default:
				// FIXME: 其他方法应不支持，抛出异常
				httpStatusCode = getHttpConnectionClient() .getStatusByPostMethod(
						uri, mediaType.toString(), encodeType, requestBodyData);
				break;
			}
		} catch (Exception e) {
			log.error("------url=="+uri);
			e.printStackTrace();
			if (e instanceof NetException){
				throw (NetException)e;
			}
		}finally{
			long time = System.currentTimeMillis() - startMillis;
			if(time>500){
				log.info(method.toString()+"请求耗时  url="+uri+ ", time=" + time);
			}
		}
		return String.valueOf(httpStatusCode);
	}
	
	/**
	 * 执行方法，并返回实体内容
	 * @param uri
	 * @param requestBodyData
	 * @param method
	 * @param mediaType
	 * @param encodeType
	 * @return
	 */
	private static String doMethodEntity(String uri, String requestBodyData,
			HttpMethod method, MediaType mediaType, String encodeType) {
		long startMillis = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("doMethod[" + method + "]->uri=" + uri
					+ ", requestBodyData=" + requestBodyData);
		}
		String entity = "";
		if (null == uri)
			return null;
		requestBodyData = requestBodyData == null ? "" : requestBodyData;
		try {
			switch (method) {
			case POST:
				entity = getHttpConnectionClient().getContextByPostMethod(
						uri, mediaType.toString(), encodeType, requestBodyData);
				break;
			case PUT:
				entity = getHttpConnectionClient().getContextByPutMethod(uri,
						mediaType.toString(), encodeType, requestBodyData);
				break;
			case DELETE:
				entity = getHttpConnectionClient().getContextByDeleteMethod(
						uri, mediaType.toString(), encodeType, requestBodyData);
				break;
			default:
				// FIXME: 其他方法应不支持，抛出异常
				entity = getHttpConnectionClient().getContextByPostMethod(
						uri, mediaType.toString(), encodeType, requestBodyData);
				break;
			}
		} catch (Exception e) {
			log.error(method.toString()+"请求异常  url="+uri+",error:"+e.getMessage() );
			e.printStackTrace();
			if (e instanceof NetException){
				throw (NetException)e;
			}
		}finally{
			long time = System.currentTimeMillis() - startMillis;
			if(time>500){
				log.info(method.toString()+"请求耗时  url="+uri+ ", time=" + time);
			}
		}
		return String.valueOf(entity);
	}
	
	public void setHeadParams(Map<String, String> headParams) {
		SimpleRestClientUtil.headParams = headParams;
	}
	public static void main(String[] args) {
		String url = "http://localhost:8181/commissions/jrj_100811010085555260/3/SIF";
		String body = "{\"accountId\":\"SIF_3\",\"commission_amount\":1,\"commission_price\":3281.4,\"commission_type\":\"1001\",\"money_type\":\"1\",\"stock_code\":\"IF1106\",\"stock_type\":\"10\",\"uuid\":\"jrj_100811010085555260\"}";
		System.out.println(doPost(url, body));
		
		
	}

}
