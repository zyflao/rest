package com.cfo.stock.web.rest.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.cfo.stock.web.rest.result.BusinessResult;
import com.cfo.stock.web.rest.result.CancentrustResult;
import com.cfo.stock.web.rest.result.EntrustResult;
import com.cfo.stock.web.rest.result.HistoryBusinessResult;
import com.cfo.stock.web.rest.result.HistoryEntrustResult;
import com.cfo.stock.web.rest.result.TradeIframeResult;
import com.cfo.stock.web.rest.utils.MapUtils;
import com.jrj.stocktrade.api.account.UserAccountService;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.iframe.TradeIframeService;
import com.jrj.stocktrade.api.iframe.vo.TradeIframe;
import com.jrj.stocktrade.api.stock.SecurityQueryService;
import com.jrj.stocktrade.api.stock.SecurityStockService;
import com.jrj.stocktrade.api.stock.StockHistoryQueryService;
import com.jrj.stocktrade.api.stock.vo.Business;
import com.jrj.stocktrade.api.stock.vo.CancelableEntrust;
import com.jrj.stocktrade.api.stock.vo.Entrust;
import com.jrj.stocktrade.api.stock.vo.EntrustWithdrawResp;
import com.jrj.stocktrade.api.stock.vo.HistoryBusiness;
import com.jrj.stocktrade.api.stock.vo.HistoryEntrust;

public class TradeBase extends StockBaseRest{
	
	@Autowired
	private StockHistoryQueryService historyQueryService;

	@Autowired
	private SecurityQueryService securityQueryService;

	@Autowired
	private SecurityStockService securityStockService;

	@Autowired
	private TradeIframeService tradeIframeService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	/**
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param brokerStr
	 * @param checkType
	 *            当 checkType=1 为当日委托 当 checkType=2 为历史委托
	 * @param startDate
	 * @param endDate
	 * @param positionStr
	 * @param size
	 * @return
	 */
	public String queryEntrustListBase(String userId,String sessionId,
			long accountId,int checkType,String startDate,
			String endDate,String positionStr,Integer size) {
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+
				"-->accountId:"+accountId+"-->checkType:"+checkType+
				"-->startDate:"+startDate+"-->endDate:"+endDate+
				"-->positionStr:"+positionStr+"-->size:"+size);	
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				||accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		// 当checkType没设置时设置checkType的默认值
		if (checkType < 1 || checkType > 2) {
			checkType = 1;// 默认查当日委托
		}		
		EntrustResult result = new EntrustResult();
		HistoryEntrustResult historyEntrustResult = new HistoryEntrustResult();
		if (checkSessionId(userId, sessionId)) {
			try {
				// 当日委托
				if (checkType == 1) {
					List<Entrust> list = new ArrayList<Entrust>();
					// 分页
					if (size != null && size > 0) {
						if (StringUtils.isBlank(positionStr)) {
							positionStr = "1";
						}
						list = securityQueryService.securityEntrustQuery(
								userId, accountId, positionStr, size);
					} else {
						list = securityQueryService.securityEntrustQuery(
								userId, accountId);
					}
					// 不分页
					result.setList(list);
					return OpenResult.ok().add("data", result).buildJson();

				} else if (checkType == 2) {
					List<HistoryEntrust> list;
					// 历史委托
					if (StringUtils.isNotEmpty(startDate)
							&& StringUtils.isNotEmpty(endDate)) {
						// 分页
						if (size != null && size > 0) {
							if (StringUtils.isBlank(positionStr)) {
								positionStr = "1";
							}
							list = historyQueryService.historyEntrustQuery(
									userId, accountId,
									Integer.parseInt(startDate),
									Integer.parseInt(endDate), positionStr,
									size);
						} else {
							// 不分页
							list = historyQueryService.historyEntrustQuery(
									userId, accountId,
									Integer.parseInt(startDate),
									Integer.parseInt(endDate));
						}
						historyEntrustResult.setList(list);
						return OpenResult.ok()
								.add("data", historyEntrustResult).buildJson();
					}
				}
			} catch (ServiceException e) {
				log.error("query entrust ServiceException" + e.getMessage(),e);
				return OpenResult 
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		}
		return OpenResult.noAccess("未授权").buildJson();
	}
	
	/**
	 * 成交查询
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param broker
	 * @param checkType
	 *            1当日成交 2历史成交
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String queryBusinessListBase(String userId,
			String sessionId,
			long accountId,
			int checkType,
			String startDate,
			String endDate,
			String positionStr,
			Integer size) {
		
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+
				"-->accountId:"+accountId+"-->checkType:"+checkType+
				"-->startDate:"+startDate+"-->endDate:"+endDate+
				"-->positionStr:"+positionStr+"-->size:"+size);	
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				||accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}

		// 当checkType没设置时设置checkType的默认值
		if (checkType < 1 || checkType > 2) {
			checkType = 1;// 默认查当日委托
		}
		List<HistoryBusiness> historyBusinessList = null;
		List<Business> businessList = null;
		if (checkSessionId(userId, sessionId)) {
			try {
				// 当日成交
				if (checkType == 1) {
					BusinessResult businessResult = new BusinessResult();
					if (size != null && size > 0) {
						if (StringUtils.isBlank(positionStr)) {
							positionStr = "1";
						}
						businessList = securityQueryService
								.securityRealtimeQuery(userId, accountId,
										positionStr, size);
					} else {
						businessList = securityQueryService
								.securityRealtimeQuery(userId, accountId);
					}

					businessResult.setList(businessList);
					return OpenResult.ok().add("data", businessResult)
							.buildJson();
				} else if (checkType == 2) {
					HistoryBusinessResult historyBusinessResult = new HistoryBusinessResult();
					// 历史成交
					if (StringUtils.isNotEmpty(startDate)
							&& StringUtils.isNotEmpty(endDate)) {
						// 分页
						if (size != null && size > 0) {
							if (StringUtils.isBlank(positionStr)) {
								positionStr = "1";
							}
							historyBusinessList = historyQueryService
									.historyBusinessQuery(userId, accountId,
											Integer.parseInt(startDate),
											Integer.parseInt(endDate),
											positionStr, size);
						} else {
							// 不分页
							historyBusinessList = historyQueryService
									.historyBusinessQuery(userId, accountId,
											Integer.parseInt(startDate),
											Integer.parseInt(endDate));
						}
						// 将historyBusinessList 按照时间倒序排列
						if (CollectionUtils.isNotEmpty(historyBusinessList)) {
							reverseHistoryBussiness(historyBusinessList);
						}
						historyBusinessResult.setList(historyBusinessList);
						return OpenResult.ok()
								.add("data", historyBusinessResult).buildJson();
					} else {
						return OpenResult.parameterError("参数错误").buildJson();
					}

				}
			} catch (ServiceException e) {
				log.error("query business ServiceException" + e.getErrorInfo(),e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}

		}
		return OpenResult.noAccess("未授权").buildJson();
	}
	
	
	/**
	 * 可撤单列表
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param broker
	 * @return
	 */
	public String cancelableEntrustListBase(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId") long accountId) {
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->accountId:"+accountId);	
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				||accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			List<CancelableEntrust> cancelableList;
			try {
				cancelableList = securityQueryService
						.securityCancelableEntrustQuery(userId, accountId);
				CancentrustResult cancetrustResult = new CancentrustResult();
				cancetrustResult.setCancelableList(cancelableList);
				return OpenResult.ok().add("data", cancetrustResult)
						.buildJson();
			} catch (ServiceException e) {
				log.error("query cancentrust ServiceException" + e.getMessage(),e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	
	/**
	 * 撤单 如果是ITN类型的券商不校验password
	 * @param headers
	 * @param content
	 * @return
	 */
	public String withdrawBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String password = json.getString("password");
		long accountId = json.getLongValue("accountId");
		String entrustNo = json.getString("entrustNo");
		String tradeWay = json.getString("tradeWay");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+
				"-->accountId:"+accountId+"-->entrustNo:"+entrustNo+
				"-->tradeWay:"+tradeWay+
				"-->password:"+password==null?"password为空":"password不为空");
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| accountId ==0L|| StringUtils.isBlank(entrustNo)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
//		brokerType "0". 自接     "1". 恒生/ITN
		if(StringUtils.isEmpty(tradeWay)){ //兼容老版本2.0.0之前的版本   tradeWay=null tradeWay为空说明是交易类型采用自接
			if(StringUtils.isBlank(password)){
				return OpenResult.parameterError("请输入密码").buildJson();
			}
		}else{
			if(StringUtils.equals("0", tradeWay)){//tradeWay 不为空并且tradeWay==0交易类型也是采用自接
				if(StringUtils.isBlank(password)){
					return OpenResult.parameterError("请输入密码").buildJson();
				}
			}else{
				password = "";
			}
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				EntrustWithdrawResp entrustWithdrawResp = securityStockService
						.securityEntrustWithdraw(userId,accountId, password,
								entrustNo);
				return OpenResult.ok().add("data", entrustWithdrawResp)
						.buildJson();
			} catch (ServiceException e) {
				log.error("撤单异常：" + e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	
	/**
	 * 撤单 中山模式
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	public String withdrawStockBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->accountId:"+accountId);	
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		String url, data,sign,param;
		url = "";data=""; sign=""; param="";
		if (checkSessionId(userId, sessionId)) {
			try {
				
				TradeIframe tradeIframe = tradeIframeService
						.securityEntrustWithdraw(userId, accountId);
				if (tradeIframe != null) {
					url = tradeIframe.getUrl();
					Map<String, String> map = tradeIframe.getParam();
					if(!map.isEmpty()){
						data = map.get("data");
						sign = map.get("sign");
						param = MapUtils.mapToString(map);
					}				
				}
				TradeIframeResult result = new TradeIframeResult(url, data,
						sign);
				result.setMethod("get");
				result.setParam(param);
				return OpenResult.ok().add("data", result).buildJson();
			} catch (ServiceException e) {
				log.error("gowithdraw ServiceException" + e.getMessage(),e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
}
