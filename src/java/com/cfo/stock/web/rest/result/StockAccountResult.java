package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.stock.vo.StockAccount;

public class StockAccountResult {
	private List<StockAccount> stockAccountList = new ArrayList<StockAccount>();

	public List<StockAccount> getStockAccountList() {
		return stockAccountList;
	}

	public void setStockAccountList(List<StockAccount> stockAccountList) {
		this.stockAccountList = stockAccountList;
	}

	
}
