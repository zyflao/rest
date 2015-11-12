package com.cfo.stock.web.rest.base;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.itn.ItnAuthService;
import com.jrj.stocktrade.api.itn.vo.ItnBindResp;

/**
 * 接入ITN相关功能基础类
 * 
 * @author Kecheng.Li
 * 
 */
public class ITNAccessBase extends StockBaseRest {

	@Autowired
	private ItnAuthService itnAuthService;

	public String bindITNBrokerBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String fundAccount = json.getString("fundAccount");
		String password = json.getString("password");
		String brokerId = json.getString("brokerId");
		String txPassword = json.getString("txPassword");
		String tradeWay = json.getString("tradeWay");
		log.info("-->userId:" + userId + "-->sessionId:" + sessionId+ 
				"-->fundAccount:" + fundAccount + "-->password:" + password+ 
				"-->brokerId:" + brokerId + "-->txPassword:" + txPassword+
				"-->tradeWay:" + tradeWay);
		if(StringUtils.isBlank(userId)||StringUtils.isBlank(sessionId)||
		   StringUtils.isBlank(fundAccount)||StringUtils.isBlank(password)){
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				ItnBindResp itnBindResp = null;
				JSONObject j = getUserBaseInfo(userId);
				if(StringUtils.equals("1", tradeWay)){
					if(!StringUtils.contains(brokerId, "ITN")){
						brokerId = "ITN_".concat(brokerId);
					}
				}
				if (j != null) {
					String mobileNo = j.getString("mobileNo");
					String realName = j.getString("realName");
					itnBindResp = itnAuthService.bindItn(userId, realName,
							mobileNo, fundAccount, password, brokerId,
							txPassword);
				}
				log.info("itnBindResp-->"+JSONObject.toJSONString(itnBindResp));
				if (itnBindResp != null) {
					int retcode = itnBindResp.getErrorNo();
					String msg = itnBindResp.getErrorInfo();
					if (retcode != 0) {
						return OpenResult.serviceError(retcode, msg).buildJson();
					}
				}
				return OpenResult.ok().buildJson();
			} catch (ServiceException e) {
				log.error("itn bind ServiceException "+e.getMessage(),e);
				return OpenResult.serviceError(-1, e.getMessage()).buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

}
