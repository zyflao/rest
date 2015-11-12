package com.cfo.stock.web.rest.utils;

import com.jrj.common.utils.PropertyManager;


public class ConstantVariable {
	
	public static final String initBrokerId = PropertyManager.getString("initialize_auth_brokerId_list");
	
	public static final String txpwdBroker = PropertyManager.getString("txpwd_broker_list");
	
	public static final String tradeWayOwn = PropertyManager.getString("bind_trade_way_own");
	
	public static final String tradeWayITN = PropertyManager.getString("bind_trade_way_itn");
	
	public static final String strBrokerId = PropertyManager.getString("filter_brokerId_list");
	
	public static final String appverStrBrokerId = PropertyManager.getString("appver_filter_brokerId_list");
	
	public static String brokerLogoUrl = PropertyManager.getString("prefix_broker_logo_url");

	public static String markUrl = PropertyManager.getString("prefix_mark_h5_url");
	
	public static String FILTER_TRADEABLE_BROKERS = PropertyManager.getString("tradable_filter_brokerId_list");
	
	public static String ONLINE_TRADEABLE_BROKERS = PropertyManager.getString("online_trable_brokerId_list");

}
