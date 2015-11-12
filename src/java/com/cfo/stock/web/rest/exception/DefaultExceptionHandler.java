package com.cfo.stock.web.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Resteasy默认异常处理（打印到日志）
 * @history  
 * <PRE>  
 * ---------------------------------------------------------  
 * VERSION       DATE            BY       CHANGE/COMMENT  
 * ---------------------------------------------------------  
 * v1.0         2014-4-29    		iriyadays     create  
 * ---------------------------------------------------------  
 * </PRE> 
 *
 */
public class DefaultExceptionHandler implements ExceptionMapper<Exception> {
	Log log = LogFactory.getLog(getClass());

	@Override
	public Response toResponse(Exception e) {
		log.error("exception", e);
		return Response.serverError().entity("Internal Server Error.").build();
	}

}
