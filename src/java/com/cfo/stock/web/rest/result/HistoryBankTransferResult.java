package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.banktrans.vo.HistoryBankTransfer;

public class HistoryBankTransferResult {
	List<HistoryBankTransfer> list = new ArrayList<HistoryBankTransfer>();

	public List<HistoryBankTransfer> getList() {
		return list;
	}

	public void setList(List<HistoryBankTransfer> list) {
		this.list = list;
	}
	
	
}
