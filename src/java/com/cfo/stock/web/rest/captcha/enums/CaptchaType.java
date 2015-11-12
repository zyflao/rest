package com.cfo.stock.web.rest.captcha.enums;

/** 
 * 验证码的类型
 * 
 */
public enum CaptchaType {
	SIMPLE,
	COMPLEX,
	NUMBER;
	
    public static CaptchaType valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }
    
}
