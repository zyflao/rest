package com.cfo.stock.web.rest.base;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.jrj.stocktrade.api.account.UserAuthService;
import com.jrj.stocktrade.api.account.vo.UserAccAuth;
import com.jrj.stocktrade.api.common.ProcAuthorizeType;
import com.jrj.stocktrade.api.exception.ServiceException;

public class UserAuthBase extends StockBaseRest {

	@Autowired
	private UserAuthService userAuthService;

	public String processUserAuthBase(String content) {

		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		String procAuthorizeType = json.getString("procAuthorizeType");
		log.info("-->userId:" + userId + "-->sessionId:" + sessionId
				+ "-->accountId:" + accountId + "-->procAuthorizeType:"
				+ procAuthorizeType);
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)
				|| accountId == 0L || StringUtils.isEmpty(procAuthorizeType)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		ProcAuthorizeType type = ProcAuthorizeType.valueOf(procAuthorizeType);
		if (checkSessionId(userId, sessionId)) {
			try {
				UserAccAuth userAccAuth = userAuthService.procAuthorize(userId,
						accountId, type);
				log.info("process user authorize userAccAuth--->"
						+ JSONObject.toJSONString(userAccAuth));
				return OpenResult.ok().add("data", userAccAuth).buildJson();
			} catch (ServiceException e) {
				log.error(
						"process user authorize ServiceException"
								+ e.getErrorInfo(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	
	public String setDefaultBrokerBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->accountId:"+accountId);
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)
			|| accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if(checkSessionId(userId, sessionId)){
			try {
				userAuthService.changeDefault(userId, accountId);
				return OpenResult.ok().buildJson();
			} catch (ServiceException e) {
				log.error("set current account aefault ServiceException"
								+ e.getErrorInfo(), e);
				return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		}else{
			return OpenResult.noAccess("未授权").buildJson();
		}
		
	}
}
