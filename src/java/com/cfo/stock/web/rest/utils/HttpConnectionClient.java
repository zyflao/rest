/**
 * 
 */
package com.cfo.stock.web.rest.utils;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.jrj.common.net.HttpClientExecuter;
import com.jrj.common.net.KeyValue;
import com.jrj.common.net.NetException;

/**
 * Http链接取数据工具
 * 
 * @author coldwater
 * 
 */
public class HttpConnectionClient {
	/**
	 * 链接超时
	 */
	private static int DEFAULT_CONNECTION_TIMEOUT = 1000 * 3;
	/**
	 * 传输超时
	 */
	private static int DEFAULT_SO_TIMEOUT = 1000 * 3;
	/**
	 * 最大连接数
	 */
	private static int DEFAULT_CONNECTIONS_MAX_TOTAL = 200;
	/**
	 * 每host最大连接数
	 */
	private static int DEFAULT_CONNECTIONS_MAX_PERHOST = 50;
	
	// 初始化用到的同步锁
	private final ReentrantLock lock = new ReentrantLock();

	// 注意：这里不能static，因为有用多个httpConnectionClient的情况
	private MultiThreadedHttpConnectionManager connectionManager = null;
	private HttpClient httpClient = null;
	private Map<String,String> headParams=new HashMap<String,String>();
	
	private int connectionTimeOut = DEFAULT_CONNECTION_TIMEOUT;
	private int soTimeOut = DEFAULT_SO_TIMEOUT;
	private int connectionMaxTotal = DEFAULT_CONNECTIONS_MAX_TOTAL;
	private int connectionMaxPerHost = DEFAULT_CONNECTIONS_MAX_PERHOST;
	/**
	 * 返回字符集
	 */
	private String codeing = "GBK";
	/**
	 * 请求字符集
	 */
	private String charset = null;
	
	private String getCharset(){
		if (charset==null){
			return codeing;
		}else{
			return charset;
		}
	}
	private HttpClient getHttpClient() {
		lock.lock();
		try {
			if (connectionManager == null) {
				System.out
						.println("=====new MultiThreadedHttpConnectionManager()");
				connectionManager = new MultiThreadedHttpConnectionManager();
				configure();
			}
			if (httpClient == null) {
				httpClient = new HttpClient(connectionManager);
			}

		} finally {
			lock.unlock();
		}
		return httpClient;
	}

	/**
	 * 配置connectionmanager
	 */
	private void configure() {
		HttpConnectionManagerParams params = connectionManager.getParams();
		params.setConnectionTimeout(connectionTimeOut);
		params.setMaxTotalConnections(connectionMaxTotal);
		params.setDefaultMaxConnectionsPerHost(connectionMaxPerHost);
		params.setSoTimeout(soTimeOut);
	}

	/**
	 * 返回http网页内容，使用Get方法提交
	 * 
	 * @param url
	 * @return
	 */
	public String getContextByGetMethod(String url) {
		HttpClient client = getHttpClient();
		// 设置编码
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				getCharset());
		GetMethod gm = new GetMethod(url);

		String result = "";
		try {
			addReuestHeader(gm);
			client.executeMethod(gm);

			if (gm.getStatusCode() >= 400) {
				throw new HttpException("Connection Error!return Status :"
						+ gm.getStatusCode());
			}
			result = new String(gm.getResponseBody(),codeing);
		} catch (Exception e) {
			throw new NetException("HttpClient catch!", e);
		} finally {
			gm.releaseConnection();
		}
		return result;
	}

	/**
	 * 返回http网页内容，使用Post方法提交
	 * 
	 * @param url
	 *            访问地址
	 * @param param
	 *            参数，键值对列表
	 * @return
	 */
	public String getContextByPostMethod(String url, List<KeyValue> params) {
		HttpClient client = getHttpClient();
		// 设置编码
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				getCharset());
		PostMethod post = null;
		String result = "";
		try {
			// 设置提交地址
			URL u = new URL(url);
			client.getHostConfiguration().setHost(u.getHost(),
					u.getPort() == -1 ? u.getDefaultPort() : u.getPort(),
					u.getProtocol());
			post = new PostMethod(u.getPath());
			post.setQueryString(u.getQuery());
			// 拼键值对
			NameValuePair[] nvps = new NameValuePair[params.size()];
			int i = 0;
			for (KeyValue kv : params) {
				nvps[i] = new NameValuePair(kv.getKey(), kv.getValue());
				i++;
			}
			addReuestHeader(post);
			// 提交数据
			post.setRequestBody(nvps);

			client.executeMethod(post);
			result = new String(post.getResponseBody(),codeing);
		} catch (Exception e) {
			throw new NetException("HttpClient catch!", e);
		} finally {
			if (post != null)
				post.releaseConnection();
		}
		return result;
	}

	/**
	 * 返回http网页内容，使用POST方法提交
	 * 
	 * @param url
	 *            访问地址
	 * @param contentType
	 *            提交数据内容的类型
	 * @param requestCodeing
	 *            提交数据的字符编码
	 * @param body
	 *            提交的数据
	 * @return
	 */
	public String getContextByPostMethod(String url, String contentType,
			String requestCodeing, String body) {
		return this.getContextByMethod(url, "POST", contentType,
				requestCodeing, body);
	}

	/**
	 * 返回http网页内容，使用PUT方法提交
	 * 
	 * @param url
	 *            访问地址
	 * @param contentType
	 *            提交数据内容的类型
	 * @param requestCodeing
	 *            提交数据的字符编码
	 * @param body
	 *            提交的数据
	 * @return
	 */
	public String getContextByPutMethod(String url, String contentType,
			String requestCodeing, String body) {
		return this.getContextByMethod(url, "PUT", contentType, requestCodeing,
				body);
	}

	/**
	 * 返回http网页内容，使用DELETE方法提交
	 * 
	 * @param url
	 *            访问地址
	 * @param contentType
	 *            提交数据内容的类型
	 * @param requestCodeing
	 *            提交数据的字符编码
	 * @param body
	 *            提交的数据
	 * @return
	 */
	public String getContextByDeleteMethod(String url, String contentType,
			String requestCodeing, String body) {
		return this.getContextByMethod(url, "DELETE", contentType,
				requestCodeing, body);
	}

	/**
	 * 返回http网页内容，使用提交方法，支持POST\PUT\DELETE
	 * 
	 * @param url
	 *            访问地址
	 * @param method
	 *            执行的方法，支持POST\PUT\DELETE
	 * @param contentType
	 *            提交数据内容的类型
	 * @param requestCodeing
	 *            提交数据的字符编码
	 * @param body
	 *            提交的数据
	 * @return
	 */
	public String getContextByMethod(String url, String method,
			String contentType, String requestCodeing, String body) {
		HttpClient client = getHttpClient();
		//设置编码
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				getCharset());
		HttpMethod mt = null;
		String result = "";
		try {
			// 设置提交地址
			URL u = new URL(url);
			client.getHostConfiguration().setHost(u.getHost(),
					u.getPort() == -1 ? u.getDefaultPort() : u.getPort(),
					u.getProtocol());
			if ("POST".equalsIgnoreCase(method)) {
				mt = new PostMethod(u.getPath());
			} else if ("PUT".equalsIgnoreCase(method)) {
				mt = new PutMethod(u.getPath());
			} else if ("DELETE".equalsIgnoreCase(method)) {
				mt = new DeleteMethod(u.getPath());
			} else {
				throw new NetException("Nonsupport this method: " + method);
			}
			addReuestHeader(mt);
			mt.setQueryString(u.getQuery());

			// 提交数据
			// mt.addRequestHeader("content-type", contentType);
			StringRequestEntity entity = new StringRequestEntity(body,
					contentType, requestCodeing);
			((EntityEnclosingMethod) mt).setRequestEntity(entity);
			client.executeMethod(mt);
			if (mt.getStatusCode() >= 400) {
				throw new HttpException("Connection Error!return Status :"
						+ mt.getStatusCode());
			}
			byte[] resultBytes = mt.getResponseBody();
			result = new String(resultBytes, codeing);
		} catch (Exception e) {
			throw new NetException("HttpClient catch!", e);
		} finally {
			if (mt != null)
				mt.releaseConnection();
		}
		return result;
	}

	/**
	 * 返回http请求返回状态码，使用POST方法提交
	 * 
	 * @param url
	 *            访问地址
	 * @param contentType
	 *            提交数据内容的类型
	 * @param requestCodeing
	 *            提交数据的字符编码
	 * @param body
	 *            提交的数据
	 * @return
	 */
	public int getStatusByPostMethod(String url, String contentType,
			String requestCodeing, String body) {
		return this.getStatusByMethod(url, "POST", contentType, requestCodeing,
				body);
	}

	/**
	 * 返回http请求返回状态码，使用PUT方法提交
	 * 
	 * @param url
	 *            访问地址
	 * @param contentType
	 *            提交数据内容的类型
	 * @param requestCodeing
	 *            提交数据的字符编码
	 * @param body
	 *            提交的数据
	 * @return
	 */
	public int getStatusByPutMethod(String url, String contentType,
			String requestCodeing, String body) {
		return this.getStatusByMethod(url, "PUT", contentType, requestCodeing,
				body);
	}

	/**
	 * 返回http请求返回状态码，使用DELETE方法提交
	 * 
	 * @param url
	 *            访问地址
	 * @param contentType
	 *            提交数据内容的类型
	 * @param requestCodeing
	 *            提交数据的字符编码
	 * @param body
	 *            提交的数据
	 * @return
	 */
	public int getStatusByDeleteMethod(String url, String contentType,
			String requestCodeing, String body) {
		return this.getStatusByMethod(url, "DELETE", contentType,
				requestCodeing, body);
	}

	/**
	 * 返回http请求返回状态码，使用提交方法，支持POST\PUT\DELETE
	 * 
	 * @param url
	 *            访问地址
	 * @param method
	 *            执行的方法，支持POST\PUT\DELETE
	 * @param contentType
	 *            提交数据内容的类型
	 * @param requestCodeing
	 *            提交数据的字符编码
	 * @param body
	 *            提交的数据
	 * @return
	 */
	public int getStatusByMethod(String url, String method, String contentType,
			String requestCodeing, String body) {
		HttpClient client = getHttpClient();
		// 设置编码
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				getCharset());
		HttpMethod mt = null;
		int result = -1;
		try {
			// 设置提交地址
			URL u = new URL(url);
			client.getHostConfiguration().setHost(u.getHost(),
					u.getPort() == -1 ? u.getDefaultPort() : u.getPort(),
					u.getProtocol());
			if ("POST".equalsIgnoreCase(method)) {
				mt = new PostMethod(u.getPath());
			} else if ("PUT".equalsIgnoreCase(method)) {
				mt = new PutMethod(u.getPath());
			} else if ("DELETE".equalsIgnoreCase(method)) {
				mt = new DeleteMethod(u.getPath());
			} else {
				throw new NetException("Nonsupport this method: " + method);
			}
			addReuestHeader(mt);
			mt.setQueryString(u.getQuery());
			// 提交数据
			// mt.addRequestHeader("content-type", contentType);
			StringRequestEntity entity = new StringRequestEntity(body,
					contentType, requestCodeing);
			((EntityEnclosingMethod) mt).setRequestEntity(entity);
			client.executeMethod(mt);
			result = mt.getStatusCode();

		} catch (Exception e) {
			throw new NetException("HttpClient catch!", e);
		} finally {
			if (mt != null)
				mt.releaseConnection();
		}
		return result;
	}

	private void addReuestHeader(HttpMethod mt){
		Set<String> keys=headParams.keySet();
		for(String key:keys){
			mt.addRequestHeader(key, headParams.get(key));
		}
	}
	/**
	 * 执行一个自定义指令，返回自定义内容
	 * @param executer 执行器
	 * @return	自定义内容
	 */
	public Object execute(HttpClientExecuter executer){
		return executer.execute(getHttpClient());
	}
	public int getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(int connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public int getConnectionMaxTotal() {
		return connectionMaxTotal;
	}

	public void setConnectionMaxTotal(int connectionMaxTotal) {
		this.connectionMaxTotal = connectionMaxTotal;
	}

	public int getConnectionMaxPerHost() {
		return connectionMaxPerHost;
	}

	public void setConnectionMaxPerHost(int connectionMaxPerHost) {
		this.connectionMaxPerHost = connectionMaxPerHost;
	}

	/**
	 * @param codeing
	 *            the codeing to set
	 */
	public void setCodeing(String codeing) {
		this.codeing = codeing;
	}

	/**
	 * @return the codeing
	 */
	public String getCodeing() {
		return codeing;
	}

	public void setSoTimeOut(int soTimeOut) {
		this.soTimeOut = soTimeOut;
	}

	public int getSoTimeOut() {
		return soTimeOut;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public Map<String, String> getHeadParams() {
		return headParams;
	}
	public void setHeadParams(Map<String, String> headParams) {
		this.headParams = headParams;
	}
	
}
