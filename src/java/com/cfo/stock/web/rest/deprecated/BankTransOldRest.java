package com.cfo.stock.web.rest.deprecated;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;

import com.cfo.stock.web.rest.base.BankTransBase;
import com.cfo.stock.web.rest.interceptors.SwitchAccountIdInterceptor.ChangeAccountId;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;

/**
 * 
 * @className：BankTransRest
 * @classDescription： 银证转账 转账流水记录相关接口
 * @author：kecheng.Li
 * 
 * @dateTime：2014年4月19日 上午11:56:25
 */
@Path(StockBaseRest.baseuri + "/banktrans")
@Controller
public class BankTransOldRest extends BankTransBase {

	/**
	 * 客户银行帐户查询
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param broker
	 * @return
	 */
	@Path("/account")
	@GET
	@Produces("application/json;charset=utf-8")
	@ChangeAccountId
	public String queryAccountOld(
			@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("broker") String broker,
			@HeaderParam("accountId")long accountId) {
		return queryAccountBase(userId, sessionId, accountId);
	}
	
	/**
	 * 银证转账 调整为一个接口 根据 direct参数来查询 1 代表 银行转证券 2代表 证券转银行
	 *  2014-06-04  调整
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/bankbrokerTrans")
	@POST
	@Produces("application/json;charset=utf-8")
	@ChangeAccountId
	public String bankTransferOld(String content) {
		
		if (StringUtils.isBlank(content)) {
			OpenResult.parameterError("无参数").buildJson();
		}
		return bankTransferBase(content);
	}
	
	/**
	 * 银证转账 调整为一个接口 根据 direct参数来查询 1 代表 银行转证券 2代表 证券转银行
	 *  2014-09-15  调整  中山模式
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/banksecurities/transfer")
	@POST
	@Produces("application/json;charset=utf-8")
	@ChangeAccountId
	public String bankSecuritiesTransferOld(String content) {
		
		if (StringUtils.isBlank(content)) {
			OpenResult.parameterError("无参数").buildJson();
		}
		
		return bankSecuritiesTransferBase(content);
	}
	/**
	 * 银证转账 调整为一个接口 根据 direct参数来查询 1 代表 银行转证券 2代表 证券转银行
	 *  2014-09-15  调整  中山模式
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/send/btparam")
	@POST
	@Produces("application/json;charset=utf-8")
	@ChangeAccountId
	public String bankSecuritiesTransferReceiveOld(String content) {
		
		if (StringUtils.isBlank(content)) {
			OpenResult.parameterError("无参数").buildJson();
		}
		return bankSecuritiesTransferReceiveBase(content);
	}
	
	/** 银证转账 - 转账流水记录 及转账流水历史
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/transRecord")
	@POST
	@Produces("application/json;charset=utf-8")
	@ChangeAccountId
	public String queryBankTransferRecordOld(String content,
			@Context HttpHeaders headers) {
		if (StringUtils.isBlank(content)) {
			OpenResult.parameterError("无参数").buildJson();
		}
		return queryBankTransferRecordBase(content,headers);
	}

	/**
	 * 查询银行参数
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param brokerStr
	 * @param bankNo
	 * @return
	 */
	@Path("/bankarg")
	@GET
	@Produces("application/json;charset=utf-8")
	@ChangeAccountId
	public String getbankInfo(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("broker") String brokerStr,
			@QueryParam("bankNo") String bankNo,
			@HeaderParam("accountId") long accountId) {

		return getbankInfoBase(userId, sessionId, accountId, bankNo);
	}
	
	/** 密码配置
	 * @param content
	 * @return
	 */
	@Path("/pwdconfig")
	@POST
	@Produces("application/json;charset=utf-8")
	@ChangeAccountId
	public String getPasswordFlagOld(String content) {		
		return getPasswordFlagBase(content);
	}
}
