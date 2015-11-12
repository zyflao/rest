package com.cfo.stock.web.rest.captcha.sigleton;

import java.util.HashMap;
import com.cfo.stock.web.rest.captcha.engine.CustomGmailEngine;
import com.cfo.stock.web.rest.captcha.engine.CustomHotmailEngine;
import com.cfo.stock.web.rest.captcha.engine.CustomNumberEngine;
import com.cfo.stock.web.rest.captcha.enums.CaptchaType;
import com.cfo.stock.web.rest.captcha.service.CustomGenericManageableCaptchaService;
import com.cfo.stock.web.rest.captcha.service.CustomImageCaptchaService;
import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBean;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;

/** 
 * 单例类，只创建一次图片框架，加快图片的显示速度
 *
 */
public class CaptchaServiceSingleton {
	private static HashMap<CaptchaType,CustomImageCaptchaService> instances = new HashMap<CaptchaType,CustomImageCaptchaService>();

	public static CustomImageCaptchaService getInstance(VerifyCodeBean verifyCodeBean) {
		CaptchaType type = verifyCodeBean.getVerifyCodeEnum();
		if(instances.get(type)==null){
           synchronized (CaptchaServiceSingleton.class ){
               if(instances.get(type)==null){
            	   if(type == CaptchaType.SIMPLE) {
            		   instances.put(CaptchaType.SIMPLE, new CustomGenericManageableCaptchaService(new FastHashMapCaptchaStore(), new CustomGmailEngine(verifyCodeBean),180,100000,75000));
            	   } else if(type == CaptchaType.COMPLEX) {
            		   instances.put(CaptchaType.COMPLEX,new CustomGenericManageableCaptchaService(new FastHashMapCaptchaStore(),new CustomHotmailEngine(verifyCodeBean),180,100000,75000));
            	   } else if(type == CaptchaType.NUMBER) {
            		   instances.put(CaptchaType.NUMBER,new CustomGenericManageableCaptchaService(new FastHashMapCaptchaStore(),new CustomNumberEngine(verifyCodeBean),180,100000,75000));
            	   }
               }
           }
	   }
	   return instances.get(type);
	}
}
