package com.cfo.stock.web.rest.utils;

import org.apache.commons.lang.StringUtils;

public class BusinessUtils {
	
	public static String getBusinessByProductId(String productid){
		
		String ITOUGUFlag = "ITOUGU";
		String JRJCGBBFlag = "JRJCGBB";
		
		if(StringUtils.isBlank(productid)){
			return JRJCGBBFlag;
		}		
		//炒股必备
		if("1019012".equals(productid)||"1019025".equals(productid)){
			return JRJCGBBFlag;
		//爱投顾	
		}else if("1020021".equals(productid)||"1020025".equals(productid)){
			return  ITOUGUFlag;
		}else{
			return  JRJCGBBFlag;
		}
		
	}
}
