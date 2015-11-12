package com.cfo.stock.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.stereotype.Controller;

import com.cfo.stock.web.rest.base.ConfigModeBase;

@Path(StockBaseRest.baseuri+"/v2"+"/config")
@Controller
public class ConfigModelRest extends ConfigModeBase {

	/* 根据不同的券商获取不同的模式
	 * 
	 * 
	 * (non-Javadoc)
	 * @see com.cfo.stock.web.rest.base.ConfigModeBase#getModel(java.lang.String)
	 */
	@Path("/get/model")
	@GET
	@Produces("application/json;charset=utf-8")
	public String getModel(@QueryParam("brokerId") String brokerId,@Context HttpHeaders headers) {
		return getModelBase(brokerId,headers);
	}

	
	/** 中山模式不同功能请求不同参数
	 * @param userId
	 * @param sessionId
	 * @param accountId
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Path("/query/type")
	@GET
	@Produces("application/json;charset=utf-8")
	public String queryByType(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId") long accountId,
			@QueryParam("type") String type,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {
		return queryByTypeBase(userId, sessionId, accountId, 
				type, startDate, endDate);
	}

}
