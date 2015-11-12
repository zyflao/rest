package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.stock.vo.Business;

public class BusinessResult {
	
	List<Business> list = new ArrayList<Business>();

	public List<Business> getList() {
		return list;
	}

	public void setList(List<Business> list) {
		this.list = list;
	}
	
}
