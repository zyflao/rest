package com.cfo.stock.web.rest.deprecated;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.cfo.stock.web.rest.base.DepositBase;
import com.cfo.stock.web.rest.interceptors.SwitchAccountIdInterceptor.ChangeAccountId;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
/**
 * 
 * @className：DepositRest
 * @classDescription：
 * 
 * @author：kecheng.Li
 * 
 * @dateTime：2014年4月19日 下午1:21:39
 */
@Path(StockBaseRest.baseuri + "/deposit")
@Controller
public class DepositOldRest extends DepositBase {

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
	@ChangeAccountId
	public String clientFundOld(
			@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("broker") String broker,
			@HeaderParam("accountId") long accountId) {
	
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(broker)) {
			return OpenResult.parameterError("参数不正确").buildJson();
		}
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
	@ChangeAccountId
	public String fundAmountOld(String content) {
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
	@ChangeAccountId
	public String queryBankAmountOld(String content) {
		if (StringUtils.isBlank(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		
		return queryBankAmountBase(content);
	}
	
}
