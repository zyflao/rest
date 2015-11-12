package com.cfo.stock.web.rest.vo;

import com.jrj.stocktrade.api.common.ValueableEnum;

public enum EntrustStatusVo implements ValueableEnum{
	
	NOT_REPORT("0", "未报"),
	WAIT_REPORT("1", "待报"),
	REPORTED("2", "已报"),
//	WAIT_WITHDRAW("3", "已报待撤"),
	PART_WAIT_WITHDRAW("40", "部成待撤"),
	PART_WITHDRAW("50", "部撤"),
//	WITHDRAWED("6", "已撤"),
	PART_CONCLUDED("7", "部成"),
	CONCLUDED("8", "已成"),
//	ABANDON("9", "废单"),
	
//	SUCCESS("2", "成功"),
	ABANDON("3", "作废"),
	WAIT_WITHDRAW("4", "待撤"),
	WITHDRAWED("5", "撤销"),
	WAIT_CORRECTION("7", "待冲正"),
	CORRECTIONED("8", "已冲正"),
	WAIT_REPORTED("A", "待报"),
	RE_REPORTED("B", "重发已报"),
	RE_TIMEOUT("C","重发超时"),
	CORRECTION_TIMEOUT("D","冲正超时"),
	CORRECTION_FAIL("E","冲正失败"),
	CORRECTION_WAITR("G","冲正待报"),
	REPORTING("P","正报"),
	CONFIRMED("Q","已确认"),
	WAIT_CONFIRM("x","待确认")
	;
	
	private final String value;
	private final String name;
	
	private EntrustStatusVo(String value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

}
