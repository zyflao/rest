/**
 * 
 */
package com.cfo.stock.web.rest.common;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author tongzhui.peng
 *
 */
public class InfoMasker {

	public static String masker(String str,int start,int length,String replace,int orientation){
		
		if(StringUtils.isEmpty(str)){
			return str;
		}
		if(length + start > str.length())
			return str;
		
		StringBuilder sb = new StringBuilder();
		int _start = 0;
		int _end = 0;
		if(orientation < 0){
			_start = str.length() - start - length;
			_end = _start + length;
		}else{
			_start = start;
			_end = start + length;
		}
		sb.append(str.substring(0, _start));
		for(int i = 0;i<length;i++){
			sb.append(replace);
		}
		sb.append(str.substring(_end));
		return sb.toString();
	}
	
	public static String masker(String str, int start, int length,
			String replace) {

		return masker(str,start,length,replace,1);
	}
	
	/**
	 * 0 正常
	 * 1 未满18岁
	 * 2 格式异常
	 * @param id
	 * @return
	 */
	public static int isOver18(String id){
		
		String yyyyMMdd = id.substring(6, 14);
		
		try {
			Date d = DateUtils.parseDate(yyyyMMdd, "yyyyMMdd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.YEAR, -18);
			if(calendar.getTime().before(d)){
				return 1;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return 2;
		}
		return 0;
	}

}
