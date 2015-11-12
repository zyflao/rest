package com.cfo.stock.web.rest;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.cfo.stock.web.rest.base.TradeBase;

/**
 * 
 * @className：TradeRest
 * @classDescription： 用户 当日/历史成交 当日/历史委托
 * @author：kecheng.Li
 * 
 * @dateTime：2014年4月20日 下午11:09:23
 */
@Path(StockBaseRest.baseuri +"/v2"+"/trade")
@Controller
public class TradeRest extends TradeBase {

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
	@Path("/list/entrust")
	@GET
	@Produces("application/json;charset=utf-8")
	public String queryEntrustList(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId") long accountId,
			@QueryParam("checkType") int checkType,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate,
			@QueryParam("positionStr") String positionStr,
			@QueryParam("size") Integer size) {
		return queryEntrustListBase(userId, sessionId, accountId, checkType, 
				startDate, endDate, positionStr, size);

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
	@Path("/list/business")
	@GET
	@Produces("application/json;charset=utf-8")
	public String queryBusinessList(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId") long accountId,
			@QueryParam("checkType") int checkType,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate,
			@QueryParam("positionStr") String positionStr,
			@QueryParam("size") Integer size) {
		return queryBusinessListBase(userId, sessionId, accountId,
				checkType, startDate, endDate, positionStr, size);

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
	@Path("/list/cancentrust")
	@GET
	@Produces("application/json;charset=utf-8")
	public String cancelableEntrustList(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId") long accountId) {		
		return cancelableEntrustListBase(userId, sessionId, accountId);

	}

	/**
	 * 撤单
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/withdraw")
	@POST
	@Produces("application/json;charset=utf-8")
	public String withdraw(String content) {
		if (StringUtils.isBlank(content)) {
			OpenResult.parameterError("无参数").buildJson();
		}
		return withdrawBase(content);
	}

	/**
	 * 撤单 中山模式
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/gowithdraw")
	@POST
	@Produces("application/json;charset=utf-8")
	public String withdrawStock(String content) {
		if (StringUtils.isBlank(content)) {
			OpenResult.parameterError("无参数").buildJson();
		}
		return withdrawStockBase(content);
	}

}
