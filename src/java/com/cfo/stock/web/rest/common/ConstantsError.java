package com.cfo.stock.web.rest.common;

public class ConstantsError {
	public static final int BANKTRANS_SERIALNO_ERROR = 1000005;
	public static final int BANKTRANS_RESULT_ERROR = 1000006;
	public static final int BANKTRANS_NO_RESULT = 1000007;
	
	public static final String BANKTRANS_SERIALNO_MSG="查询失败，请稍后再查";	
	public static final String BANKTRANS_RESULT_MSG="由于银行处理延时，您可在转账查询中查看余额";	
	public static final String BANKTRANS_NO__MSG="查询异常，请稍后再查";
	
	public static final int CALL_PHONE_ERROR = -300;
	public static final String CALL_PHONE_MSG = "您填写的身份证已注册，请联系客服 400-166-1188";
	
}
