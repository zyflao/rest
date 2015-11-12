package com.cfo.stock.web.rest.captcha.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cfo.stock.web.rest.captcha.processor.VerifyCodeProcessor;
import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBean;
import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBeans;

/** 
 * 用来加载初始化数据
 *
 */
public class InitData {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger("VerifycodeLog");
	private static VerifyCodeProcessor verifyCodeprocess = null;
	private InitData() {	
	}
	static{
		InitData.initVerifyCode(InitData.class.getResource("/VerifyCodeConfig.xml").getPath());
	}
	
	public static void initVerifyCode(String path) {
		verifyCodeprocess = VerifyCodeProcessor.getInstance(path);
	}
	
	
	/** 
	 * 根据url获得当前使用的验证码形式bean
	 * @param url
	 * @return
	 */
	public static VerifyCodeBean getVerifyCodeBean(String url) {
		VerifyCodeBeans beans = verifyCodeprocess.getVerifyCodeBeans();
		if(StringUtils.isBlank(url)) {
			return beans.getList().get(0);//默认使用注册的验证码形式
		}
		for(VerifyCodeBean bean :beans.getList()) {
			if(StringUtils.equalsIgnoreCase(url, bean.getUrl())) {
				return bean;
			}
		}
		return beans.getList().get(0); ///默认使用注册的验证码形式
	}
	
	public static boolean resetVerifyCodeXml() {
		boolean ok = verifyCodeprocess.reset();
		return ok;
	}
	public static void main(String[] args) {
		System.out.println(InitData.getVerifyCodeBean("").getName());
	}
}
