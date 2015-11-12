package com.cfo.stock.web.rest.utils;

import org.apache.commons.lang.StringUtils;

import com.cfo.stock.web.rest.vo.EntrustStatusVo;
import com.jrj.stocktrade.api.common.ExchangeType;
import com.jrj.stocktrade.api.common.MoneyType;

public class EumUtils {
	
	/** 根据name 来获取枚举类型
	 * @param ename
	 * @return
	 */
	public static ExchangeType getExchangeType(String ename) {
		for (ExchangeType exchangeType : ExchangeType.values()) {
			if (exchangeType.name().equalsIgnoreCase(ename)) {
				return exchangeType;
			}
		}
		return null;
	}
	
	/** 根据name 来获取枚举类型
	 * @param ename
	 * @return
	 */
	public static MoneyType getMoneyType(String ename) {
		if(StringUtils.isNotEmpty(ename)){
			for (MoneyType moneyType : MoneyType.values()) {
				if (moneyType.name().equalsIgnoreCase(ename)) {
					return moneyType;
				}
			}
		}		
		return null;
	}
	public static EntrustStatusVo getEntrustStatusVo(String ename) {
		if(StringUtils.isNotEmpty(ename)){
			for (EntrustStatusVo entrustStatusVo : EntrustStatusVo.values()) {
				if (entrustStatusVo.name().equalsIgnoreCase(ename)) {
					return entrustStatusVo;
				}
			}
		}		
		return null;
	}
	
  public static void main(String[] args){
	  String ename = "CNY";
	  MoneyType getMoneyType = getMoneyType(ename);
	  String ss = getMoneyType.toString();
	  System.out.println(getMoneyType.getName()+"---"+getMoneyType.getValue());
  }
}
