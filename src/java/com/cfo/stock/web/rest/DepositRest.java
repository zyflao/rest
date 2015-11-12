package com.cfo.stock.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.cfo.stock.web.rest.base.DepositBase;
/**
 * 
 * @className：DepositRest
 * @classDescription：
 * 
 * @author：kecheng.Li
 * 
 * @dateTime：2014年4月19日 下午1:21:39
 */
@Path(StockBaseRest.baseuri+"/v2"+"/deposit")
@Controller
public class DepositRest extends DepositBase {

	/**资金股份 - 资金查询
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param brokerStr
	 * @return
	 */
	@Path("/client/fund")
	@GET
	@Produces("application/json;charset=utf-8")
	public String clientFund(
			@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId")long accountId) {
		return clientFundBase(userId, sessionId, accountId);
	}
 
	/**查询银行卡余额
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/bank/amount")
	@POST
	@Produces("application/json;charset=utf-8")
	public String fundAmount(String content) {
		if (StringUtils.isBlank(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		return fundAmountBase(content);
	}
	
	/**查询银行卡余额  
	 * 将查询银行余额返回流水号接口和通过银证转账流水查询余额两个接口合并
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/bank/balance")
	@POST
	@Produces("application/json;charset=utf-8")
	public String queryBankAmount(String content) {
		if (StringUtils.isBlank(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		return queryBankAmountBase(content);
	}
}
