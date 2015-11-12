package com.cfo.stock.web.rest.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

import com.jrj.common.utils.ListUtil;

public class ListUtils extends ListUtil{
	
	public static final String separator = ",";
	
	/**
	 * @param str String 字符串 
	 * @param separator  分隔符
	 * @return
	 */
	public static List<String> stringToList(String str, String separator){	
		List<String> list = new ArrayList<String>();
		String[] strList = null;
		if(StringUtils.isBlank(separator)){
			separator = ",";
		}
		if(StringUtils.isNotBlank(str)){
			if(str.contains(separator)){
				strList = str.trim().split(separator);
			}else{
				strList = new String[]{str.trim()};
			}
			list = Arrays.asList(strList);
		}	
		return list;
	}
	
	public static void main(String[] args) {
		String str = "  ";
//		String str = "HTZQ ";
//		String str = "HTZQ,";
		List<String> list = stringToList(str, null);
		System.out.println(list);
	}

}
