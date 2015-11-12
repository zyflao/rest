package com.cfo.stock.web.rest.captcha.processor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBean;
import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBeans;


/**
 * 验证码xml处理解析过程
 */
public class VerifyCodeProcessor extends XmlCommonProcessor {

	private static final Logger logger = Logger.getLogger("VerifycodeLog");
	private VerifyCodeBeans verifyCodeBeans;
	/**
	 * @return the verifyCodeBeans
	 */
	public VerifyCodeBeans getVerifyCodeBeans() {
		return verifyCodeBeans;
	}
	private static VerifyCodeProcessor instance;
	
	private VerifyCodeProcessor(String path){
		if(super.init(path)) {
			logger.info("VerifyCodeProcessor " + path + " load success!");
		} else {
			logger.error("VerifyCodeProcessor " + path + " load failure!");
		}
	}
	
	public static VerifyCodeProcessor getInstance(String path){
		if(instance==null){
			synchronized (VerifyCodeProcessor.class) {
				if(instance==null){
					synchronized (VerifyCodeProcessor.class) {
						instance = new VerifyCodeProcessor(path);
					}
				}
			}
		}
		return instance;
	}
	
	/**
	 * 不重启直接加载xml的方法
	 * @return
	 */
	public boolean reset(){
		verifyCodeBeans = new VerifyCodeBeans();
		return super.reset();
	}
	
	/**
	 * 解析xml
	 * @param path
	 * @return
	 */
	protected boolean parseXML(String path){
		boolean bool = false;
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addRuleSet(new VerifyCodeRuleSet());
		
		File file = new File(path);
		try {
			verifyCodeBeans = (VerifyCodeBeans)digester.parse(file);
			bool = true;
		} catch (IOException e) {
			e.printStackTrace();
			bool = false;
		} catch (SAXException e) {
			e.printStackTrace();
			bool = false;
		}
		
		return bool;
	}
		
	
	public static final void main(String [] args){
		VerifyCodeProcessor v = VerifyCodeProcessor.getInstance("/home/leon/git/xjb_web/web/src/resource/VerifyCodeConfig.xml");
		for(VerifyCodeBean bean :v.getVerifyCodeBeans().getList()) {
			System.out.println(bean.getUrl());
			System.out.println(bean.getName());
			System.out.println(bean.getWordDict());
			System.out.println(bean.getFontMinSize());
			System.out.println(bean.getVerifyCode());
			System.out.println(bean.getVerifyCodeEnum());
		}
		
	}
}
