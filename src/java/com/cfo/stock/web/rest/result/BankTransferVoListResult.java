package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.cfo.stock.web.rest.vo.BankTransferVo;

public class BankTransferVoListResult {
	
	List<BankTransferVo> list = new ArrayList<BankTransferVo>();

	public List<BankTransferVo> getList() {
		return list;
	}

	public void setList(List<BankTransferVo> list) {
		this.list = list;
	}
}
