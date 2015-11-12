package com.cfo.stock.web.rest.captcha.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证码所有参数的集合
 *
 */
public class VerifyCodeBeans {

	private List<VerifyCodeBean> list;
	
	public VerifyCodeBeans(){
		list = new ArrayList<VerifyCodeBean>();
	}

	public List<VerifyCodeBean> getList() {
		return list;
	}

	public void setList(List<VerifyCodeBean> list) {
		this.list = list;
	}
	
	public void addPermit(VerifyCodeBean bean){
		list.add(bean);
	}
}
