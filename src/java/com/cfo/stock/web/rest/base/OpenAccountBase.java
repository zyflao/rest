package com.cfo.stock.web.rest.base;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.opstatus.StockAccountStatusService;

public class OpenAccountBase extends StockBaseRest {

	@Autowired
	private StockAccountStatusService stockAccountStatusService;

	public String deleteOpenRecordBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String brokerId = json.getString("brokerId");
		String sessionId = json.getString("sessionId");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->brokerId:"+brokerId);
		if(StringUtils.isBlank(userId)||StringUtils.isBlank(brokerId)
				||StringUtils.isBlank(sessionId)){
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		try {
			// 未开户未绑定 判断
			boolean openbind = openOrBind(brokerId, userId);
			boolean checkFlag = checkSessionId(userId, sessionId);
			if (openbind || checkFlag) {
//				当从开户状态表查询状态为-2时 调用此功能
				int row = stockAccountStatusService.delAccountOpen(userId,brokerId);
				if (row != 1) {
					return OpenResult.serviceError(-1, "删除有误").buildJson();
				}
				return OpenResult.ok().buildJson();
			} else {
				return OpenResult.noAccess("未授权").buildJson();
			}
		} catch (ServiceException e) {
			log.error("delete open account ServiceException-->" + e.getErrorInfo(), e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
		}
	}
}
