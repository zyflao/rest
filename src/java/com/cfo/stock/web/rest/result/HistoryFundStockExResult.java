package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.stock.vo.HistoryFundStockEx;

public class HistoryFundStockExResult {
	private List<HistoryFundStockEx> list = new ArrayList<HistoryFundStockEx>();

	public List<HistoryFundStockEx> getList() {
		return list;
	}

	public void setList(List<HistoryFundStockEx> list) {
		this.list = list;
	}
	
	
}
