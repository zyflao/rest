package com.cfo.stock.web.rest.captcha.utils;

/** 
 * 资源文件
 *
 */
public class Constant {
	
	// 验证码平台资源
	public static final String SESSION_VERIFYCODE = "verifycode-";
	public static final String VERIFYCODE_ID = "vc_id";
	public static final String COOKIE_DOMAIN = "stock.rest.cookieDomain";
	public static final String HEADER_REFERER = "Referer";
	
	
	// 邮箱验证的服务平台资源
	public static final String INTERFACE_SERVICE_EMAIL = "interface.service.email";
	public static final String INTERFACE_SERVICE_APS = "interface.service.aps";

	
	//日期格式，年份，例如：2004，2008
    public static final String DATE_FORMAT_YYYY = "yyyy";
    
    //日期格式，年份和月份，例如：200707，200808
    public static final String DATE_FORMAT_YYYYMM = "yyyyMM";
    
    //日期格式，年月日，例如：20050630，20080808
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    
    //日期格式，年月日，用横杠分开，例如：2006-12-25，2008-08-08
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    
    //日期格式，年月日时分秒，例如：20001230120000，20080808200808
    public static final String DATE_TIME_FORMAT_YYYYMMDDHHMISS = "yyyyMMddHHmmss";
    
    //日期格式，年月日时分秒，年月日用横杠分开，时分秒用冒号分开，
    //例如：2005-05-10 23：20：00，2008-08-08 20:08:08
    public static final String DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";
    public static final long MILLION_ONEDAY = 24 * 60 * 60 * 1000;
    
	// 邮箱发送平台模板的替换变量
	public static final String MAIL_USERNAME="SSO_USER_NAME";
	public static final String MAIL_USER_ID="MAIL_USER_ID";
	public static final String MAIL_USER_KEY="MAIL_USER_KEY";
	// 查询库中验证邮箱总数
	public static final long MAX_MAIL_COUNT=100;
}