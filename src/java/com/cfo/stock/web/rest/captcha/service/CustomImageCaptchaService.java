package com.cfo.stock.web.rest.captcha.service;

import java.awt.image.BufferedImage;
import java.util.Locale;

import com.octo.captcha.Captcha;
import com.octo.captcha.service.multitype.MultiTypeCaptchaService;

/** 
 * 重写MultiTypeCaptchaService接口
 *
 */
public interface CustomImageCaptchaService extends MultiTypeCaptchaService {
	/** 
	 * 获取Captcha对象
	 * @param ID
	 * @return
	 */
	public Captcha getCaptchaForID(String ID);
	
	/** 
	 * 获取Captcha对象
	 * @param ID
	 * @param locale
	 * @return
	 */
	public Captcha getCaptchaForID(String ID, Locale locale);
	
	/** 
	 * 获得图片文件
	 * @param captcha
	 * @return
	 */
	public BufferedImage getImageChallengeForID(Captcha captcha);
}
