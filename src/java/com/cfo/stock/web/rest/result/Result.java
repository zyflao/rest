package com.cfo.stock.web.rest.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.cfo.stock.web.rest.vo.BaseVo;;

/**
 * 现金宝接口返回结果
 * @author yuanlong.wang
 *
 */
public class Result<T extends BaseVo>{
	protected int retcode;
	protected String msg;
	public Result() {
	}
	@JSONField(serialize = false) 
	public int getRetcode() {
		return retcode;
	}
	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}
	@JSONField(serialize = false) 
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public T parse(){
		return null;
	}; 
}
