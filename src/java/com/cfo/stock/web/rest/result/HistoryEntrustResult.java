package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.stock.vo.HistoryEntrust;

public class HistoryEntrustResult {
	private List<HistoryEntrust> list = new ArrayList<HistoryEntrust>();

	public List<HistoryEntrust> getList() {
		return list;
	}

	public void setList(List<HistoryEntrust> list) {
		this.list = list;
	}
	
}
