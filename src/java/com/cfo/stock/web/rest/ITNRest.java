package com.cfo.stock.web.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.cfo.stock.web.rest.base.ITNAccessBase;


/** 
 * @author Kecheng.Li
 *
 */
@Path(StockBaseRest.baseuri+ "/v2" + "/itn")
@Controller
public class ITNRest  extends ITNAccessBase{
	
	/** ITN券商绑定  只需一步即可完成 区别于自接券商绑定
	 * @param userId
	 * @param sessionId
	 * @param fundAccount
	 * @param password
	 * @param brokerId
	 * @param txPassword 非必填
	 * @return
	 */
	@Path("/bind/broker")
	@POST
	@Produces("application/json;charset=utf-8")
	public String bindITNBroker(String content){
		if(StringUtils.isEmpty(content)){
			return OpenResult.parameterError("无参数").buildJson();
		}
		return bindITNBrokerBase(content);
	}
}
