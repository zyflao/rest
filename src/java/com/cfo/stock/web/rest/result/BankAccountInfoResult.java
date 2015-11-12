package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.banktrans.vo.BankAccountInfo;

public class BankAccountInfoResult {
	List<BankAccountInfo> bankAccountInfoList = new ArrayList<BankAccountInfo>();

	public List<BankAccountInfo> getBankAccountInfoList() {
		return bankAccountInfoList;
	}

	public void setBankAccountInfoList(List<BankAccountInfo> bankAccountInfoList) {
		this.bankAccountInfoList = bankAccountInfoList;
	}
	
}
