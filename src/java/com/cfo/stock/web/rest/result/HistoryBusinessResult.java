package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.stock.vo.HistoryBusiness;

public class HistoryBusinessResult {
	List<HistoryBusiness> list = new ArrayList<HistoryBusiness>();

	public List<HistoryBusiness> getList() {
		return list;
	}

	public void setList(List<HistoryBusiness> list) {
		this.list = list;
	}
	
}
