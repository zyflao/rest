package com.cfo.stock.web.rest.captcha.sigleton;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jrj.common.cache.memcached.MemcachedCache;

@Component
public class CaptchaMemcacheSigleton {
	private static Logger logger=Logger.getLogger(CaptchaMemcacheSigleton.class);
	private static CaptchaMemcacheSigleton captchaMemcacheSigleton;
	@Autowired
	private MemcachedCache sessionMemCache;
	
	public static MemcachedCache getInstance() {
		if (captchaMemcacheSigleton == null) {
			logger.error("[httpSessionService] need initialize in spring config");
			throw new RuntimeException("[httpSessionService] have not initialized!");
		}
		return captchaMemcacheSigleton.sessionMemCache;
	}
	
	public void setCaptchaMemcacheSigleton(
			CaptchaMemcacheSigleton captchaMemcacheSigleton) {
		CaptchaMemcacheSigleton.captchaMemcacheSigleton = captchaMemcacheSigleton;
	}
	public void setSessionMemCache(MemcachedCache sessionMemCache) {
		this.sessionMemCache = sessionMemCache;
	}
	
}
