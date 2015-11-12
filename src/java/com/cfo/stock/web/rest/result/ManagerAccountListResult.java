package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

public class ManagerAccountListResult {
	
	private List<ManagerAccountResult>  list = new ArrayList<ManagerAccountResult>();

	public List<ManagerAccountResult> getList() {
		return list;
	}

	public void setList(List<ManagerAccountResult> list) {
		this.list = list;
	}
	
	
}
