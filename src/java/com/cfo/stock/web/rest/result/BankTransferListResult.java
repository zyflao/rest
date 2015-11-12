package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.banktrans.vo.BankTransfer;

public class BankTransferListResult {
	
	List<BankTransfer> list = new ArrayList<BankTransfer>();

	public List<BankTransfer> getList() {
		return list;
	}

	public void setList(List<BankTransfer> list) {
		this.list = list;
	}
	
	
}
