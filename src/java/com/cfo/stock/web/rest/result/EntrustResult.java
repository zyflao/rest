package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.stock.vo.Entrust;

public class EntrustResult {
	
	private List<Entrust> list = new ArrayList<Entrust>();

	public List<Entrust> getList() {
		return list;
	}

	public void setList(List<Entrust> list) {
		this.list = list;
	}
		
}
