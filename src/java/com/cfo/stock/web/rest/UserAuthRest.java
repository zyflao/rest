package com.cfo.stock.web.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.cfo.stock.web.rest.base.UserAuthBase;
import com.jrj.stocktrade.api.account.UserAuthService;




@Path(StockBaseRest.baseuri+"/v2"+"/auth")
@Controller
public class UserAuthRest extends UserAuthBase {

	@Autowired
	private UserAuthService userAuthService;
		
	/**处理授权 接受 拒绝 取消(删除)
	 * procAuthorizeType 为以下参数代表意思
	 * 	ACCEPT,//接受
		REFUSE,//拒绝
		CANCEL //取消
		DELETE //删除
	 * @param content
	 * @return
	 */
	@Path("/process/userauth")
	@POST
	@Produces("application/json;charset=utf-8")
	public String processUserAuth(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		return processUserAuthBase(content);
	}

	/** 将当前账户设置为默认账户
	 * @param content
	 * @return
	 */
	@Path("/set/default/account")
	@POST
	@Produces("application/json;charset=utf-8")
	public String setDefaultBroker(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		return setDefaultBrokerBase(content);
	}
	
	

}
