package com.cfo.stock.web.rest.deprecated;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.base.TradeBase;
import com.cfo.stock.web.rest.interceptors.SwitchAccountIdInterceptor.ChangeAccountId;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
/**
 * 
 * @className：TradeRest
 * @classDescription： 用户 当日/历史成交 当日/历史委托
 * @author：kecheng.Li
 * 
 * @dateTime：2014年4月20日 下午11:09:23
 */
@Path(StockBaseRest.baseuri + "/trade")
@Controller
public class TradeOldRest extends TradeBase {

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
	@ChangeAccountId
	public String queryEntrustListOld(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("broker") String brokerStr,
			@QueryParam("checkType") int checkType,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate,
			@QueryParam("positionStr") String positionStr,
			@QueryParam("size") Integer size,
			@HeaderParam("accountId") long accountId){
		
		return queryEntrustListBase(userId, sessionId, accountId, checkType, startDate, endDate, positionStr, size);

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
	@ChangeAccountId
	public String queryBusinessListOld(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("broker") String broker,
			@QueryParam("checkType") int checkType,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate,
			@QueryParam("positionStr") String positionStr,
			@QueryParam("size") Integer size,
			@HeaderParam("accountId") long accountId) {
		return queryBusinessListBase(userId, sessionId, accountId, checkType, startDate, endDate, positionStr, size);

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
	@ChangeAccountId
	public String CancelableEntrustListOld(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("broker") String brokerStr,
			@HeaderParam("accountId") long accountId) {
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
	@ChangeAccountId
	public String withdrawOld(String content) {

		if (StringUtils.isBlank(content)) {
			OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String password = json.getString("password");
		long entrustNo = json.getLongValue("entrustNo");
		long accountId = json.getLongValue("accountId");

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(password) || accountId == 0L
				|| entrustNo == 0L) {
			return OpenResult.parameterError("参数不正确").buildJson();
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
	@ChangeAccountId
	public String withdrawStockOld(String content) {

		if (StringUtils.isBlank(content)) {
			OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				||accountId == 0L) {
			return OpenResult.parameterError("参数不正确").buildJson();
		}
		return withdrawStockBase(content);
			
	}

}
