package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.pub.vo.PasswordFlag;

public class PasswordFlagResult {
	List<PasswordFlag> list=new ArrayList<PasswordFlag>();

	public List<PasswordFlag> getList() {
		return list;
	}

	public void setList(List<PasswordFlag> list) {
		this.list = list;
	}
	
}	
