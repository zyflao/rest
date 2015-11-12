package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.stock.vo.StockInfo;

/**
 * 持股列表结果集
 * @author wenqiang
 *
 */
public class PositionResult {

	List<StockInfo> stockInfoList = new ArrayList<StockInfo>();

	public List<StockInfo> getStockInfoList() {
		return stockInfoList;
	}

	public void setStockInfoList(List<StockInfo> stockInfoList) {
		this.stockInfoList = stockInfoList;
	}
	
	
}
