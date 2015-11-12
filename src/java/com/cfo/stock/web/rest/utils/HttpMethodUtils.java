package com.cfo.stock.web.rest.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

 /**
 * @author Richard
 * 处理POST请求
 */
public class HttpMethodUtils {
	
	protected static Log log = LogFactory.getLog(HttpMethodUtils.class);
	/**
	 * 获取post请求的参数 因为post请求体式 json格式的 要把Io流解析成 json格式的字符串
	 * 
	 * @param inputStream
	 * @return
	 */
	public static String getPostMethodParam(InputStream inputStream) {
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
}
