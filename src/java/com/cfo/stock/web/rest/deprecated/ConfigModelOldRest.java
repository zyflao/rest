package com.cfo.stock.web.rest.deprecated;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.base.ConfigModeBase;
import com.cfo.stock.web.rest.interceptors.SwitchAccountIdInterceptor.ChangeAccountId;
import com.cfo.stock.web.rest.StockBaseRest;

@Path(StockBaseRest.baseuri + "/config")
@Controller
public class ConfigModelOldRest extends ConfigModeBase{


	@Path("/get/model")
	@GET
	@Produces("application/json;charset=utf-8")
	public String getModel(@QueryParam("brokerId") String brokerId,@Context HttpHeaders headers) {
		if (StringUtils.isBlank(brokerId)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		return getModelBase(brokerId,headers);
	}

	@Path("/query/type")
	@GET
	@Produces("application/json;charset=utf-8")
	@ChangeAccountId
	public String queryByTypeOld(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("brokerId") String brokerId,
			@QueryParam("type") String type,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate,
			@HeaderParam("accountId")long accountId) {
		
		return queryByTypeBase(userId, sessionId, accountId, type, startDate, endDate);
	}

}
