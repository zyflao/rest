package com.cfo.stock.web.rest.captcha.vo;

import com.cfo.stock.web.rest.captcha.enums.CaptchaType;

public class VerifyCodeBean {
	private String id;
	private String name;
	private String url;
	private String verifyCode;
	private CaptchaType verifyCodeEnum = CaptchaType.COMPLEX;
	private String wordDict;
	private Integer textMinLength;
	private Integer textMaxLength;
	private String textColor;
	private Integer bgWidth;
	private Integer bgHeight;
	private String backcolor;
	private Integer fontMinSize;
	private Integer fontMaxSize;
	private String fonttype;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @return the wordDict
	 */
	public String getWordDict() {
		return wordDict;
	}
	/**
	 * @return the textMinLength
	 */
	public Integer getTextMinLength() {
		return textMinLength;
	}
	/**
	 * @return the textMaxLength
	 */
	public Integer getTextMaxLength() {
		return textMaxLength;
	}
	/**
	 * @return the textColor
	 */
	public String getTextColor() {
		return textColor;
	}
	/**
	 * @return the bgWidth
	 */
	public Integer getBgWidth() {
		return bgWidth;
	}
	/**
	 * @return the bgHeight
	 */
	public Integer getBgHeight() {
		return bgHeight;
	}
	/**
	 * @return the backcolor
	 */
	public String getBackcolor() {
		return backcolor;
	}
	/**
	 * @return the fontMinSize
	 */
	public Integer getFontMinSize() {
		return fontMinSize;
	}
	/**
	 * @return the fontMaxSize
	 */
	public Integer getFontMaxSize() {
		return fontMaxSize;
	}
	/**
	 * @return the fonttype
	 */
	public String getFonttype() {
		return fonttype;
	}
	/**
	 * @return the verifyCode
	 */
	public String getVerifyCode() {
		return verifyCode;
	}
	/**
	 * @return the verifyCodeEnum
	 */
	public CaptchaType getVerifyCodeEnum() {
		return verifyCodeEnum;
	}
	/**
	 * @param verifyCode the verifyCode to set
	 */
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
    	try {
    		verifyCodeEnum = CaptchaType.valueOf(verifyCode);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param verifyCodeEnum the verifyCodeEnum to set
	 */
	public void setVerifyCodeEnum(CaptchaType verifyCodeEnum) {
		this.verifyCodeEnum = verifyCodeEnum;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @param wordDict the wordDict to set
	 */
	public void setWordDict(String wordDict) {
		this.wordDict = wordDict;
	}
	/**
	 * @param textMinLength the textMinLength to set
	 */
	public void setTextMinLength(Integer textMinLength) {
		this.textMinLength = textMinLength;
	}
	/**
	 * @param textMaxLength the textMaxLength to set
	 */
	public void setTextMaxLength(Integer textMaxLength) {
		this.textMaxLength = textMaxLength;
	}
	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}
	/**
	 * @param bgWidth the bgWidth to set
	 */
	public void setBgWidth(Integer bgWidth) {
		this.bgWidth = bgWidth;
	}
	/**
	 * @param bgHeight the bgHeight to set
	 */
	public void setBgHeight(Integer bgHeight) {
		this.bgHeight = bgHeight;
	}
	/**
	 * @param backcolor the backcolor to set
	 */
	public void setBackcolor(String backcolor) {
		this.backcolor = backcolor;
	}
	/**
	 * @param fontMinSize the fontMinSize to set
	 */
	public void setFontMinSize(Integer fontMinSize) {
		this.fontMinSize = fontMinSize;
	}
	/**
	 * @param fontMaxSize the fontMaxSize to set
	 */
	public void setFontMaxSize(Integer fontMaxSize) {
		this.fontMaxSize = fontMaxSize;
	}
	/**
	 * @param fonttype the fonttype to set
	 */
	public void setFonttype(String fonttype) {
		this.fonttype = fonttype;
	}

}
