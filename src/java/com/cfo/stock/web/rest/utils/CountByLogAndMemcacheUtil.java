package com.cfo.stock.web.rest.utils;

import org.apache.commons.lang.StringUtils;

import com.cfo.stock.web.rest.logger.Ioc9Loggor;
import com.jrj.common.cache.memcached.client.MemCachedClient;
/**
 * 
 * @className:CountByLogAndMemcacheUtil.java
 * @classDescription:memcache 计数
 * @author： Administrator
 * @dateTime:2015年10月27日下午2:29:52
 */

public class CountByLogAndMemcacheUtil {
	
	private MemCachedClient memCachedClient;

	public void setMemCachedClient(MemCachedClient memCachedClient) {
		this.memCachedClient = memCachedClient;
	}

	private  String key = "REST";
	private  String IOSKEY = "IOS" + key;
	private  String IOS9KEY = "IOS9" + key;
	private  String androidKEY = "ANDROID" + key;
	/*初始化memcacheKey*/
	private String[] brokerId = { "PAZQ", "CJZQ" };

	/* 初始化平安和长江的memKey */
	public void init(String b) {
		System.out.println("CountByLogAndMemcacheUtil init()"+b);
		Ioc9Loggor.info("CountByLogAndMemcacheUtil init()"+b);
		
		
			String BrokId = b;
			System.out.println(BrokId);
			String BrokIdIOSKEY = BrokId + IOSKEY;
			String BrokIdIOS9KEY = BrokId + IOS9KEY;
			String BrokIdandroidKEY = BrokId + androidKEY;
			InitMemcachekey(BrokIdIOSKEY, BrokIdIOS9KEY, BrokIdandroidKEY);
			System.out.println("InitMemcachekey"+"//"+BrokIdandroidKEY+"//"+BrokIdIOS9KEY+"//"+BrokIdIOSKEY);
			Ioc9Loggor.info("InitMemcachekey"+"//"+BrokIdandroidKEY+"//"+BrokIdIOS9KEY+"//"+BrokIdIOSKEY);
	}

	public void InitMemcachekey(String BrokIdIOSKEY, String BrokIdIOS9KEY,
			String BrokIdandroidKEY) {
		Ioc9Loggor.info("InitMemcachekey"+(String) memCachedClient.get(BrokIdandroidKEY));
		System.out.println("InitMemcachekey"+(String) memCachedClient.get(BrokIdandroidKEY));
		if (StringUtils.equals(
				(String) memCachedClient.get(BrokIdandroidKEY), null)) {
			Ioc9Loggor.info("memcache"+BrokIdandroidKEY+" is null ,set original value");
			System.out.println("memcache"+BrokIdandroidKEY+" is null ,set original value");
			memCachedClient.add(BrokIdIOS9KEY, "0");
			memCachedClient.add(BrokIdIOSKEY, "0");
			memCachedClient.add(BrokIdandroidKEY, "0");
			Ioc9Loggor.info("===========================================================");
			Ioc9Loggor.info("androidKEY"+memCachedClient.get(BrokIdandroidKEY));
			Ioc9Loggor.info("IOSKEY"+memCachedClient.get(BrokIdIOSKEY));
			Ioc9Loggor.info("IOS9KEY"+memCachedClient.get(BrokIdIOS9KEY));
			Ioc9Loggor.info("===========================================================");
		}
	}

	/* 判断券商是否是长江和平安，是就计数 */
	public void JudgeBrokerId(String type, String systemVersionValue,
			String brokerId) {
			init(brokerId);
			Ioc9Loggor.info("Count start参数("+type+","+systemVersionValue+","+brokerId+")");
			System.out.println("Count start参数("+type+","+systemVersionValue+","+brokerId+")");
		if ("PAZQ".equals(brokerId)){
			Ioc9Loggor.info("will do Count("+type+","+systemVersionValue+","+brokerId+")");
	    	System.out.println("will do Count("+type+","+systemVersionValue+","+brokerId+")");
			Count(type, systemVersionValue, brokerId);
			
		}else
		if ("CJZQ".equals(brokerId)){
			Ioc9Loggor.info("will do Count("+type+","+systemVersionValue+","+brokerId+")");
	    	System.out.println("will do Count("+type+","+systemVersionValue+","+brokerId+")");
			Count(type, systemVersionValue, brokerId);}
		
	}

	private void Count(String type, String systemVersionValue, String BrokId) {
		Ioc9Loggor.info("Count inner start"+type+","+systemVersionValue+","+BrokId);
		System.out.println("Count inner start"+type+","+systemVersionValue+","+BrokId);
		String BrokIDIOSKEY = BrokId + IOSKEY;
		String BrokIDIOS9KEY = BrokId + IOS9KEY;
		String BrokIDandroidKEY = BrokId + androidKEY;
		if ("IOS".equals(type)) {
			if (systemVersionValue.startsWith("9")) {
				Ioc9Loggor.info(BrokId + "IOS9");
				memCachedClient.incr(BrokIDIOS9KEY, 1);
				return;
			} else {
				Ioc9Loggor.info(BrokId +"ios");
				memCachedClient.incr(BrokIDIOSKEY, 1);
				return;
			}
		}
		if ("android".equals(type)) {
			Ioc9Loggor.info(BrokId +"Android");
			memCachedClient.incr(BrokIDandroidKEY, 1);
			return;
		}

	}

}
