package com.cfo.stock.web.rest.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MapUtils {
	
	protected static Log log = LogFactory.getLog(MapUtils.class);
	
	public static String mapToString(Map<String, String> param) {
		StringBuffer params = new StringBuffer();
		String str = "";
		if(param != null && param.size()>0){
			for (Map.Entry<String, String> entry : param.entrySet()) {
				params.append(entry.getKey())
				.append("=")
				.append(entry.getValue())
				.append("&");
			}
			String orgin = params.toString();
			if(orgin.length()>1){
				str = StringUtils.substring(orgin, 0, orgin.length()-1);
			}			
		}else{
			log.error("Method --> Param() is null or size is 0");
		}		
		return str;		
	}
	
	public static Map<String, String> encodeMap(Map<String, String> param) throws UnsupportedEncodingException {
		String data = "";
		String sign = "";
		if(!param.isEmpty()){
			data = param.get("data");
			sign = param.get("sign");
		}
		if(StringUtils.isNotEmpty(data)){
			data = URLEncoder.encode(data, "UTF-8");
		}
		if(StringUtils.isNotEmpty(sign)){
			sign = URLEncoder.encode(sign, "UTF-8");
		}				
		Map<String, String> encodeMap = new HashMap<String, String>();
		encodeMap.put("data", data);
		encodeMap.put("sign", sign);
		
		return encodeMap;
	}
	
	public static void main(String[] args) {
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("data", "dddddd");
//		param.put("sign", "ssss");
////		param.put("query", "qqqq");
//		String s = mapToString(param);
		String tt = "";
		String data;
		try {
			data = URLEncoder.encode(null, "UTF-8");
			System.out.println("---------"+data);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
