package com.cfo.stock.web.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.cfo.stock.web.rest.base.OpenAccountBase;


/** 开户记录相关功能
 * @author Kecheng.Li
 *
 */
@Path(StockBaseRest.baseuri + "/v2" + "/open")
@Controller
public class OpenAccountRest extends OpenAccountBase{
	
	
	/**标记删除开户记录
	 * @param content
	 * @return
	 */
	@Path("/del/record")
	@POST
	@Produces("application/json;charset=utf-8")
	public String deleteOpenRecord(String content) {
		if (StringUtils.isBlank(content)) {
			OpenResult.parameterError("无参数").buildJson();
		}
		return deleteOpenRecordBase(content);
	}
	
}
