package com.cfo.stock.web.rest.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BigDecimalUtils {
	
	protected static Log log = LogFactory.getLog(BigDecimalUtils.class);
	
	/** c = a/b
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal computerPercent(BigDecimal a ,BigDecimal b){
		BigDecimal c = BigDecimal.ZERO;
		
		if(a == null ||b == null){
			return c;
		}
//		-1表示小于，0是等于，1是大于
		int m = a.compareTo(c);
		int n = b.compareTo(c);
		if(m == 0 || n == 0){
			return c;
		}		
		c = a.divide(b, 2, BigDecimal.ROUND_HALF_UP);

		return c;
	}
	
	public static BigDecimal processDecimalFraction(BigDecimal  percent){
		BigDecimal c = BigDecimal.ZERO;
//		percent 为 null
		if(percent == null){
			percent = c;
//		percent 为  0	
		}else{
//			-1表示小于，0是等于，1是大于
			int m = percent.compareTo(c);
			if(m==0){
				percent = c;
			}
		}
// 其他情况统一处理
		BigDecimal p = percent.divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP);
		
		return p ; 
	}
	
	public static BigDecimal roundBigDecimal(BigDecimal decimal){
		String format = "#.00";
		DecimalFormat df = new DecimalFormat(format);
		if(decimal == null){
			decimal = BigDecimal.ZERO;
		}
		String str = df.format(decimal);
		decimal = new BigDecimal(str);
		log.info("decimal-->"+decimal);
		return decimal;
	}
	
	public static void main(String[] args) {
		BigDecimal a = 
//				null;
				BigDecimal.ZERO;
//		new BigDecimal(8.4567);
//		BigDecimal p = a.divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP);
//		BigDecimal p = processDecimalFraction(a);
//		System.out.println(p);
		
		roundBigDecimal(a);
		
	}
	
}
