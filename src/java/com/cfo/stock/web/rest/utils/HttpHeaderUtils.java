package com.cfo.stock.web.rest.utils;


import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.jrj.common.utils.ListUtil;

public class HttpHeaderUtils {
	
	public static String getHeaderValue(HttpHeaders headers,String headerName){		
		String headerValue = "";
		if(StringUtils.isEmpty(headerName)){
			return headerValue;
		}
		List<String > drivers = headers.getRequestHeader(headerName);
		if(CollectionUtils.isNotEmpty(drivers)){
			headerValue = ListUtil.join(drivers, ",");
		}		
		return headerValue;
	}
}
