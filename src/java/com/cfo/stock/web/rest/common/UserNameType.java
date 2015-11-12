package com.cfo.stock.web.rest.common;
/**
 * 用户名类型
 * @author yuanlong.wang
 *
 */
public enum UserNameType {
	IDCARD(1),USERNAME(2),MOBILE(3),EMAIL(4);
	
	public final int type;

	private UserNameType(int type) {
		this.type = type;
	}
	
}
