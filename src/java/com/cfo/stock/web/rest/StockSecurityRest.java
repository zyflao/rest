package com.cfo.stock.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.cfo.stock.web.rest.base.StockSecurityBase;


/**
 * 类名称：StocRest 类描述： 股票相关 创建人：kecheng.Li
 * 
 * 创建时间：2014年4月19日 上午11:49:28
 */
@Path(StockBaseRest.baseuri + "/v2"+"/stock")
@Controller
public class StockSecurityRest extends StockSecurityBase {

	/**
	 * 查询资金流水
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param broker
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Path("/list/fund")
	@GET
	@Produces("application/json;charset=utf-8")
	public String queryHistoryFundStock(
			@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId") long accountId,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {
		return queryHistoryFundStockBase(userId, sessionId, accountId, startDate, endDate);

	}

	/**
	 * 股票代码确认
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/code/enter")
	@POST
	@Produces("application/json;charset=utf-8")
	public String securityCodeEnter(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		return securityCodeEnterBase(content);
				

	}
	
	/**
	 * 股票买入限制
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/buy/amtlimit")
	@POST
	@Produces("application/json;charset=utf-8")
	public String EntrustBuyLimit(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		return EntrustBuyLimitBase(content);
	}
	
	/**
	 * 股票买入或者卖出  中信模式
	 * @param content
	 * @return
	 */
	@Path("/deal")
	@POST
	@Produces("application/json;charset=utf-8")
	public String deal(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		return dealBase(content);
	}
	
	/**
	 * 股票买入或者卖出  中山模式
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/business")
	@POST
	@Produces("application/json;charset=utf-8")
	public String entrustBusiness(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		return entrustBusinessBase(content);
	}
	
	/**
	 * 股票买入或者卖出  中山模式
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("send/businessparam")
	@POST
	@Produces("application/json;charset=utf-8")
	public String receiveParam(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		return receiveParamBase(content);
	}

	/**
	 * 查询持仓 查询指定股票持仓 分页查询持仓
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param broker
	 * @param stockcode
	 * @param positionStr
	 * @param size
	 * @return
	 */
	@Path("/client/position")
	@GET
	@Produces("application/json;charset=utf-8")
	public String clientPosition(
			@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId")long accountId,
			@QueryParam("stockcode") String stockcode,
			@QueryParam("positionStr") String positionStr,
			@QueryParam("size") int size) {
		return queryClientPositionBase(userId, sessionId, accountId, stockcode, positionStr, size);
	
	}
	

	/**
	 * 查询证券股东帐户
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param broker
	 * @param positionStr
	 * @param size
	 * @return
	 */
	@Path("/account")
	@GET
	@Produces("application/json;charset=utf-8")
	public String clientStockAccount(
			@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId") long accountId,
			@QueryParam("positionStr") String positionStr,
			@QueryParam("size") int size) {		
		return clientStockAccountBase(userId, sessionId, accountId, positionStr, size);
	}
	
}
