package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

public class BrokersResult {
	
	private List<BrokerEx> brokerList = new ArrayList<BrokerEx>();

	public List<BrokerEx> getBrokerList() {
		return brokerList;
	}

	public void setBrokerList(List<BrokerEx> brokerList) {
		this.brokerList = brokerList;
	}
	
	
}
