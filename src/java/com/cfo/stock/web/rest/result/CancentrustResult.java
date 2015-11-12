package com.cfo.stock.web.rest.result;

import java.util.ArrayList;
import java.util.List;

import com.jrj.stocktrade.api.stock.vo.CancelableEntrust;

/**
 * 可撤单列表结果
 * @author wenqiang
 *
 */
public class CancentrustResult {

	private List<CancelableEntrust> cancelableList = new ArrayList<CancelableEntrust>();

	public List<CancelableEntrust> getCancelableList() {
		return cancelableList;
	}

	public void setCancelableList(List<CancelableEntrust> cancelableList) {
		this.cancelableList = cancelableList;
	}
	
	
}
