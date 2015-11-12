/**
 * 
 */
package com.cfo.stock.web.rest.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.common.UserNameType;
import com.cfo.stock.web.rest.exception.StockServiceException;
import com.cfo.stock.web.rest.utils.ValidateUtil;

/**
 * 登录登出业务处理Servic类
 */
@Service
public class LoginOutService extends AbstractStockService {

	/**
	 * @param username
	 * @param password
	 * @param clientip
	 * @param clientinfo
	 * @param cccode
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject userLoginParamAll(String username, String password,
			String clientip, String clientinfo, String cccode)
			throws StockServiceException {

		// 以下为正式
		int type = 0;
		String result = null;
		if (ValidateUtil.isIdNumber(username)) {
			type = UserNameType.IDCARD.type;
		} else if (ValidateUtil.isUserName(username)) {
			type = UserNameType.USERNAME.type;
		} else if (ValidateUtil.isMobile(username)) {
			type = UserNameType.MOBILE.type;
		} else if (ValidateUtil.isEmail(username)) {
			type = UserNameType.EMAIL.type;
		}
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return new JSONObject();
		}
		if(type==0){
			JSONObject json = new JSONObject();
			json.put("retcode", 101113);
			json.put("msg", "用户名或密码有误");
			result = json.toString();
		}else{
			result = userStockApi.userLoginParamAll(username, type,
					password, clientip, clientinfo, cccode);
		}	
		
		if (StringUtils.isBlank(result)) {
			return new JSONObject();
		}
		if (log.isDebugEnabled()) {
			log.debug("====登录结果！==" + result);
		}
		return JSONObject.parseObject(result);
	}

}
