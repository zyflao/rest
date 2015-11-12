package com.cfo.stock.web.rest.captcha.processor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cfo.stock.web.rest.captcha.enums.CaptchaType;
import com.cfo.stock.web.rest.captcha.sigleton.CaptchaMemcacheSigleton;
import com.cfo.stock.web.rest.captcha.utils.Constant;
import com.jrj.common.utils.HttpUitl;
import com.octo.captcha.Captcha;

/** 
 * 验证码验证方法
 *
 */
public class ImageCaptchaValidator {
	private static Logger log = Logger.getLogger("VerifycodeLog");

	/**
	 * 验证码的验证
	 * @param uid
	 * @param userCaptchaResponse
	 * @return
	 */
	public static boolean validateResponse(String uid, String userCaptchaResponse,boolean isAjax) {
		if(StringUtils.isBlank(userCaptchaResponse))return false;
		String type = uid.substring(uid.lastIndexOf("-")+1,uid.length());
		// 判断验证码的类型
		if(CaptchaType.COMPLEX.ordinal() == Integer.parseInt(type)) {
			userCaptchaResponse = userCaptchaResponse.toUpperCase();
		} else if(CaptchaType.SIMPLE.ordinal() == Integer.parseInt(type)) {
			userCaptchaResponse = userCaptchaResponse.toLowerCase();
		} 
		boolean validated = validateFromTT(uid,userCaptchaResponse);
		if(!isAjax){
			deleteTT(uid);
		}	
		log.info("check code is：" + userCaptchaResponse);
		log.info("check code：" + validated);
		return validated;
	}
	
	/**
	 * 验证码的验证
	 * @param request
	 * @param userCaptchaResponse
	 * @return
	 */
	public static boolean validateResponse(HttpServletRequest request, String userCaptchaResponse ,boolean isAjax){
		Cookie cookie=HttpUitl.getCookie(request, Constant.VERIFYCODE_ID);
		if(cookie==null||"".equals(cookie.getValue())){
			log.info("captcha cookie is null");
			return false;
		}else{
			return ImageCaptchaValidator.validateResponse(cookie.getValue(), userCaptchaResponse,isAjax);
		}
	}
	/**
	 * 验证码的验证
	 * @param request
	 * @param userCaptchaResponse
	 * @return
	 */
	public static boolean validateResponse(HttpServletRequest request, String userCaptchaResponse){
		Cookie cookie=HttpUitl.getCookie(request, Constant.VERIFYCODE_ID);
		if(cookie==null||"".equals(cookie.getValue())){
			log.info("captcha cookie is null");
			return false;
		}else{
			return ImageCaptchaValidator.validateResponse(cookie.getValue(), userCaptchaResponse,false);
		}
	}
	
	/**
	 * 验证码的验证
	 * @param uuID
	 * @param userCaptchaResponse
	 * @return
	 */
	public static boolean validateResponse(String uuID, String userCaptchaResponse){
		boolean flag = ImageCaptchaValidator.validateResponse(uuID, userCaptchaResponse,false);
			return flag;
	}
	
	/** 
	 * 从TT中取得用户的ip进行验证
	 * @param request
	 * @param userCaptchaResponse
	 * @return
	 */
	private static boolean validateFromTT(String uid,String userCaptchaResponse) {
		
		if(CaptchaMemcacheSigleton.getInstance().keyExists(Constant.SESSION_VERIFYCODE + uid)) {
			log.info("ttkey:" + Constant.SESSION_VERIFYCODE + uid);
			Captcha captcha = (Captcha)CaptchaMemcacheSigleton.getInstance().get(Constant.SESSION_VERIFYCODE + uid);
			boolean validate = captcha.validateResponse(userCaptchaResponse);
			log.info("validate:" + validate);
			return validate;
		}
		return false;
	}
	
	private static void deleteTT(String sessionID) {
		CaptchaMemcacheSigleton.getInstance().remove(Constant.SESSION_VERIFYCODE + sessionID);
	}

}
