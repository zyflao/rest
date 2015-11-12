package com.cfo.stock.web.rest.captcha.service;

import java.awt.image.BufferedImage;
import java.util.Locale;

import com.octo.captcha.Captcha;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaStore;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;

/** 
 * 重写GenericManageableCaptchaService的方法
 *
 */
public class CustomGenericManageableCaptchaService extends
		GenericManageableCaptchaService implements CustomImageCaptchaService {

	public CustomGenericManageableCaptchaService(CaptchaEngine captchaEngine,
			int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize,
			int captchaStoreLoadBeforeGarbageCollection) {
		super(captchaEngine, minGuarantedStorageDelayInSeconds,
				maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
	}

	public CustomGenericManageableCaptchaService(CaptchaStore captchaStore,
			CaptchaEngine captchaEngine, int minGuarantedStorageDelayInSeconds,
			int maxCaptchaStoreSize, int captchaStoreLoadBeforeGarbageCollection) {
		super(captchaStore, captchaEngine, minGuarantedStorageDelayInSeconds,
				maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
	}

	public Captcha getCaptchaForID(String ID) throws CaptchaServiceException {
		return getCaptchaForID(ID, Locale.getDefault());
	}

	public Captcha getCaptchaForID(String ID, Locale locale)
			throws CaptchaServiceException {
		Captcha captcha;
		if (!store.hasCaptcha(ID)) {
			captcha = generateAndStoreCaptcha(locale, ID);
		} else {
			captcha = store.getCaptcha(ID);
			if (captcha == null)
				captcha = generateAndStoreCaptcha(locale, ID);
			else if (captcha.hasGetChalengeBeenCalled().booleanValue())
				captcha = generateAndStoreCaptcha(locale, ID);
		}
		return captcha;
	}
	
	public BufferedImage getImageChallengeForID(Captcha captcha) {
        Object challenge = super.getChallengeClone(captcha);
        captcha.disposeChallenge();
        return  (BufferedImage)challenge;
	}

}
