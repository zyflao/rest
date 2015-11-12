package com.cfo.stock.web.rest.base;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.cfo.stock.web.rest.result.ConfigPassWordResult;
import com.cfo.stock.web.rest.result.ModelConfigResult;
import com.cfo.stock.web.rest.result.TradeIframeResult;
import com.cfo.stock.web.rest.utils.ConstantVariable;
import com.cfo.stock.web.rest.utils.HttpHeaderUtils;
import com.cfo.stock.web.rest.utils.ListUtils;
import com.cfo.stock.web.rest.utils.MapUtils;
import com.jrj.common.utils.DateUtil;
import com.jrj.common.utils.PropertyManager;
import com.jrj.stocktrade.api.common.BrokerId;
import com.jrj.stocktrade.api.common.SecurityQueryType;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.iframe.TradeIframeService;
import com.jrj.stocktrade.api.iframe.vo.TradeIframe;

public class ConfigModeBase extends StockBaseRest{
	
	@Autowired
	private TradeIframeService tradeIframeService;
	
	public String getModelBase(String brokerId,@Context HttpHeaders headers) {
		log.info("<--brokerId:"+brokerId);
		if (StringUtils.isBlank(brokerId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		try {
			String ZSZQFlag = BrokerId.ZSZQ_SECURITIES;
			String HTZQFlag = BrokerId.CNHT_SECURITIES;

			String buysell = null; // 买卖
			String transfer = null;// 银证转账
			String withdraw = null;// 撤单
			String entrust = null;// 委托查询
			String business = null;// 成交查询
			String fund = null; // 资金流水
			String balance = null;
			String tradeFlag = null;
			String domainUrl = "";			
			List<String> initList = ListUtils.stringToList(ConstantVariable.initBrokerId, ",");
			List<String> txPwdList = ListUtils.stringToList(ConstantVariable.txpwdBroker, ",");
			String paltid = HttpHeaderUtils.getHeaderValue(headers,"paltid");
			if(HTZQFlag.equalsIgnoreCase(brokerId)){
				domainUrl = brokerId+"_domainUrl";
				domainUrl = PropertyManager.getString(domainUrl);
				buysell = "3";
				transfer = "3";
				withdraw = "3";
				entrust = "3";
				business = "3";
				fund = "3";
				balance = "3";
				tradeFlag = "true";
			}else if (ZSZQFlag.equalsIgnoreCase(brokerId)) {
				buysell = "2";
				transfer = "2";
				withdraw = "2";
				entrust = "2";
				business = "2";
				fund = "2";
				balance = "2";
				tradeFlag = "true";
			}else{
				buysell = "1";
				transfer = "1";
				withdraw = "1";
				entrust = "1";
				business = "1";
				fund = "1";
				balance = "1";
				tradeFlag = "true";
			} 
			ModelConfigResult modelConfigResult = new ModelConfigResult(buysell, transfer,
					withdraw, entrust, business, fund,balance);
			modelConfigResult.setTradeFlag(tradeFlag);
			modelConfigResult.setDomainUrl(domainUrl);
			String reLoginAuthType = setReLoginAuthType(brokerId, initList, txPwdList);
			if("android".equalsIgnoreCase(paltid)){
				modelConfigResult.setReLoginAuthType(reLoginAuthType);
			}
			ConfigPassWordResult result = setPwdType(brokerId, initList, txPwdList);
//			设置密码配置
			modelConfigResult.setConfigPassWordResult(result);
			return OpenResult.ok().add("data", modelConfigResult).buildJson();
		} catch (Exception e) {
			log.error("get config Exception" + e.getMessage(), e);
			return OpenResult.serviceError(-1, e.getMessage()).buildJson();
		}
	}
	
	
	public String queryByTypeBase(String userId,String sessionId, 
			long accountId,String type,String startDate,String endDate) {	
		log.info("<--userId:"+userId+"<--sessionId:"+sessionId+"<--accountId:"+accountId
			+"<--type:"+type+"<--startDate:"+startDate+"<--endDate:"+endDate);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				||accountId == 0L|| StringUtils.isBlank(type)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		type = type.toUpperCase();
		SecurityQueryType securityQueryType = SecurityQueryType.valueOf(type);
		String url = "";
		String data = "";
		String sign = "";
		String param = "";
		if (checkSessionId(userId, sessionId)) {
			try {
				TradeIframe tradeIframe = tradeIframeService.securityQuery(
						userId, accountId, securityQueryType,
						DateUtil.parse(startDate, "yyyyMMdd"),
						DateUtil.parse(endDate, "yyyyMMdd"));
				if (tradeIframe != null) {
					url = tradeIframe.getUrl();
					Map<String, String> map = tradeIframe.getParam();
					if (!map.isEmpty()) {
						data = map.get("data");
						sign = map.get("sign");
						param = MapUtils.mapToString(map);
					}
				}
				TradeIframeResult result = new TradeIframeResult(url, data,sign);
				result.setMethod("get");
				result.setParam(param);
				return OpenResult.ok().add("data", result).buildJson();
			} catch (ServiceException e) {
				log.error("query data by type ServiceException" + e.getMessage(),
						e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

}
