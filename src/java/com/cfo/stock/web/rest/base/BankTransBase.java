package com.cfo.stock.web.rest.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.cfo.stock.web.rest.result.BankAccountInfoResult;
import com.cfo.stock.web.rest.result.BankTransferListResult;
import com.cfo.stock.web.rest.result.BankTransferVoListResult;
import com.cfo.stock.web.rest.result.HistoryBankTransferResult;
import com.cfo.stock.web.rest.result.PasswordFlagResult;
import com.cfo.stock.web.rest.result.TradeIframeResult;
import com.cfo.stock.web.rest.utils.EumUtils;
import com.cfo.stock.web.rest.utils.HttpHeaderUtils;
import com.cfo.stock.web.rest.utils.MapUtils;
import com.cfo.stock.web.rest.vo.BankTransferVo;
import com.cfo.stock.web.rest.vo.EntrustStatusVo;
import com.jrj.common.utils.DateUtil;
import com.jrj.common.utils.PropertyManager;
import com.jrj.stocktrade.api.banktrans.BankAccountQueryService;
import com.jrj.stocktrade.api.banktrans.BankHistoryQueryService;
import com.jrj.stocktrade.api.banktrans.BankTransferQueryService;
import com.jrj.stocktrade.api.banktrans.BankTransferService;
import com.jrj.stocktrade.api.banktrans.vo.BankAccountInfo;
import com.jrj.stocktrade.api.banktrans.vo.BankTransfer;
import com.jrj.stocktrade.api.banktrans.vo.HistoryBankTransfer;
import com.jrj.stocktrade.api.banktrans.vo.TransResp;
import com.jrj.stocktrade.api.common.MoneyType;
import com.jrj.stocktrade.api.common.TransferDirection;
import com.jrj.stocktrade.api.common.ValueableEnumUtil;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.iframe.TradeIframeService;
import com.jrj.stocktrade.api.iframe.vo.TradeIframe;
import com.jrj.stocktrade.api.pub.FlagQueryService;
import com.jrj.stocktrade.api.pub.PasswordFlagService;
import com.jrj.stocktrade.api.pub.vo.BankargQueryResp;
import com.jrj.stocktrade.api.pub.vo.PasswordFlag;

public class BankTransBase extends StockBaseRest{
	
	private static final int ps =30;
	
	@Autowired
	private BankAccountQueryService bankAccountQueryService;

	@Autowired
	private BankTransferService bankTransferService;

	@Autowired
	private BankHistoryQueryService historyBankTransService;

	@Autowired
	private BankTransferQueryService bankTransferQueryService;

	@Autowired
	private FlagQueryService flagQueryService;

	@Autowired
	private PasswordFlagService passwordFlagService;
	
	@Autowired
	private  TradeIframeService tradeIframeService;
	
	/**
	 * 客户银行帐户查询
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param broker
	 * @return
	 */
	public String queryAccountBase(String userId,
			String sessionId,long accountId) {
		log.info("userId-->"+userId+"<--sessionId:"+sessionId+"<--accountId:"+accountId);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				||accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {			
			List<BankAccountInfo> bankAccountInfoList;
			BankAccountInfoResult result = new BankAccountInfoResult();
			try {
				bankAccountInfoList = bankAccountQueryService.bankAccountQuery(
						userId, accountId);
				if(CollectionUtils.isNotEmpty(bankAccountInfoList)){
					for(BankAccountInfo b:bankAccountInfoList){
						MoneyType moneyType = b.getMoneyType();
						if(moneyType==null){
							moneyType = EumUtils.getMoneyType("CNY");
							b.setMoneyType(moneyType);
						}
					}
				}
				result.setBankAccountInfoList(bankAccountInfoList);
				return OpenResult.ok().add("data", result)
						.buildJson();
			} catch (ServiceException e) {				
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	/**
	 * 银证转账 调整为一个接口 根据 direct参数来查询 1 代表 银行转证券 2代表 证券转银行
	 *  2014-06-04  调整
	 * @param headers
	 * @param content
	 * @return
	 */
	public String bankTransferBase(String content) {
		
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String password = json.getString("password");
		
		String moneyTypeStr = json.getString("moneyType");		
		MoneyType moneyType = EumUtils.getMoneyType(moneyTypeStr);
		
		String bankNo = json.getString("bankNo");
		BigDecimal occurBalance = json.getObject("occurBalance",
				BigDecimal.class);

		int directInt = json.getIntValue("direct");
		TransferDirection direct = (TransferDirection) ValueableEnumUtil
				.getEnum(String.valueOf(directInt), TransferDirection.class);

		String fundPassword = json.getString("fundPassword");
		String bankPassword = json.getString("bankPassword");
		long accountId = json.getLongValue("accountId");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->password:"+password+
				"-->moneyType:"+moneyType+"-->bankNo:"+bankNo+"-->occurBalance:"+occurBalance+
				"-->direct:"+direct+"-->fundPassword:"+fundPassword+"-->bankPassword:"+bankPassword);		
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)|| moneyType == null 
				|| StringUtils.isBlank(bankNo)||accountId == 0L
				|| occurBalance == null || directInt == 0) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			TransResp transResp;
			try {
//				中信证券
				transResp = bankTransferService.bankTransfer(userId, accountId, password, moneyType, bankNo,
						occurBalance, direct, bankPassword, fundPassword);
				return OpenResult.ok().add("data", transResp).buildJson();
			} catch (ServiceException e) {
				log.error("bankTrans ServiceException-->"+e.getMessage(),e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	
	/**
	 * 银证转账 调整为一个接口 根据 direct参数来查询 1 代表 银行转证券 2代表 证券转银行
	 *  2014-09-15  调整  中山模式
	 * @param headers
	 * @param content
	 * @return
	 */
	public String bankSecuritiesTransferBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		int directInt = json.getIntValue("direct");
		TransferDirection direct = (TransferDirection) ValueableEnumUtil
				.getEnum(String.valueOf(directInt), TransferDirection.class);
		long accountId = json.getLongValue("accountId");
		String brokerId = json.getString("broker");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+
				"-->accountId:"+accountId+"-->brokerId:"+brokerId+"-->direct:"+direct);	
		if (StringUtils.isBlank(userId)||StringUtils.isBlank(sessionId)||
				directInt == 0||accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				String url = "";
				String data = "";
				String sign = "";
				String param = "";
				String returnUrl = PropertyManager.getString("transfer_callBack_url")
						.replaceAll("\\$accountId",String.valueOf(accountId));
				if(StringUtils.isNotEmpty(brokerId)){
					returnUrl = PropertyManager.getString("old_transfer_callBack_url")
							.replaceAll("\\$brokerId",brokerId);
				}
				TradeIframe tradeIframe = tradeIframeService.bankTransfer(userId,accountId,direct,returnUrl);
				if(tradeIframe != null){
					url = tradeIframe.getUrl();
					Map<String, String> map = tradeIframe.getParam();
					if(map != null && map.size()>0){
						data = map.get("data");
						sign =  map.get("sign");
						param = MapUtils.mapToString(map);
					}
				}
				TradeIframeResult result  = new TradeIframeResult(url, data, sign);
				result.setMethod("get");
				result.setParam(param);
				result.setCallUrl(returnUrl);
				return OpenResult.ok().add("data", result).buildJson();
			} catch (ServiceException e) {
				log.error("bankTrans ServiceException-->"+e.getMessage(),e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	/**
	 * 银证转账 调整为一个接口 根据 direct参数来查询 1 代表 银行转证券 2代表 证券转银行
	 *  2014-09-15  调整  中山模式
	 * @param headers
	 * @param content
	 * @return
	 */
	public String bankSecuritiesTransferReceiveBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		String data = json.getString("data");
		String sign = json.getString("sign");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+
				"-->accountId:"+accountId+"-->data:"+data+"-->sign:"+sign);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)||
				StringUtils.isBlank(data)||StringUtils.isBlank(sign)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("data",data);
				param.put("sign", sign);
				TransResp transResp = tradeIframeService.bankTransferReceive(userId, accountId, param);
				return OpenResult.ok().add("data", transResp).buildJson();
			} catch (ServiceException e) {
				log.error("bankTrans Receive ServiceException-->"+e.getMessage(),e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	/** 银证转账 - 转账流水记录 及转账流水历史
	 * @param headers
	 * @param content
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryBankTransferRecordBase(String content,
			HttpHeaders headers) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		String password = json.getString("password");
		Long serialNo = json.getLong("serialNo");
		String startDate = json.getString("startDate");
		String endDate = json.getString("endDate");
		String appver = HttpHeaderUtils.getHeaderValue(headers, "appver");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->accountId:"+accountId+
				"-->password:"+password+"-->serialNo:"+serialNo+"-->startDate:"+startDate+
				"-->endDate:"+endDate);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)			
			||accountId == 0L ) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}

		if (checkSessionId(userId, sessionId)) {
			List<HistoryBankTransfer> historyBankTransferList;
			List<BankTransfer> bankTransferList;
			List<BankTransferVo> bankTransferVoList = new ArrayList<BankTransferVo>();
			BankTransfer bankTransfer;
			try {
				// 查询转账流水历史
				if (StringUtils.isNotEmpty(startDate)&& StringUtils.isNotEmpty(endDate)) {
					HistoryBankTransferResult historyResult = new HistoryBankTransferResult();
					historyBankTransferList = historyBankTransService
								.bankTransferHistoryQuery(userId,accountId, password,
										Integer.parseInt(startDate),
										Integer.parseInt(endDate));
					
					historyBankTransferList = filterBussinessType(historyBankTransferList, HistoryBankTransfer.class);			
					historyResult.setList(historyBankTransferList);
					return OpenResult.ok().add("data", historyResult).buildJson();
				} else {
					if(serialNo!=null && serialNo!=0){
						// 根据流水查询余额					
						bankTransfer = bankTransferQueryService.bankTransferQuery(userId, accountId, password, serialNo);
						log.info("debug查询余额bankTransfer-->"+JSONObject.toJSONString(bankTransfer));
						if(bankTransfer != null){
							String b = bankTransfer.getBusinessType().toString();
							if("CGSFQR".equalsIgnoreCase(b)){
								bankTransfer = null;
							}
						}
						return OpenResult.ok().add("data", bankTransfer).buildJson();
					}else{
						// 查询转账流水
						bankTransferList = bankTransferQueryService.unsafeBankTransferQuery(userId, accountId, password,  1, ps);
						log.info("debug转账流水bankTransferList-->"+JSONObject.toJSONString(bankTransferList));
						bankTransferList = filterBussinessType(bankTransferList, BankTransfer.class);
						int result = compareVersion(appver, "1.1.0");
//						中信证券 长城证券       银证转账--转账查询 APP 1.1.0版本之前用 EntrustStatus 之后用BankTransferStatus
						if(result<=0){
							bankTransferVoList = setBankTransferVoList(bankTransferList);
							BankTransferVoListResult voListResult = new BankTransferVoListResult();
							log.info("debug转账流水--bankTransferVoList-->"+JSONObject.toJSONString(bankTransferVoList));
							voListResult.setList(bankTransferVoList);
							return OpenResult.ok().add("data", voListResult).buildJson();
						}else{
							if(CollectionUtils.isNotEmpty(bankTransferList)){
//								遍历转账记录 如果发生日期initDate为空 设置为当前时间
								for(BankTransfer bt:bankTransferList){
									long initDate = bt.getInitDate();
									if(initDate == 0){
										String strInitDate = DateUtil.format(new Date(), DateUtil.DateFormat_yyyyMMdd);
										initDate = Long.parseLong(strInitDate);
										bt.setInitDate(initDate);
									}
								}
							}
							BankTransferListResult bankTransferResult = new BankTransferListResult();						
							bankTransferResult.setList(bankTransferList);
							return OpenResult.ok().add("data", bankTransferResult).buildJson();
						}
						
					}			
				}
			} catch (NumberFormatException e) {
				log.error("查询转账流水异常："+e);
				return OpenResult.parameterError("数据转换异常").buildJson();
			} catch (ServiceException e) {
				log.error("查询转账流水异常："+e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	
	/**
	 * 查询银行参数
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param accountId
	 * @param bankNo
	 * @return
	 */
	
	public String getbankInfoBase(String userId,String sessionId,
			long accountId,String bankNo) {
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->accountId:"+accountId+"-->bankNo:"+bankNo);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				BankargQueryResp bankargQueryResp = flagQueryService
						.bankargQuery(userId, accountId, bankNo);
				return OpenResult.ok().add("data", bankargQueryResp)
						.buildJson();
			} catch (ServiceException e) {
				log.error("查询银行参数异常："+e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	/** 密码配置
	 * @param content
	 * @return
	 */

	public String getPasswordFlagBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		String bankNo = json.getString("bankNo");
		int typebanktobroker = json.getIntValue("typebanktobroker");
		int typebrokertobank = json.getIntValue("typebrokertobank");
		int typeamount = json.getIntValue("typeamount");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->accountId:"+accountId+
				"-->bankNo:"+bankNo+"-->typebanktobroker:"+typebanktobroker+
				"-->typebrokertobank:"+typebrokertobank+"-->typeamount:"+typeamount);		
//		typebanktobroker
//		typebrokertobank
//		typeamount
		
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| accountId == 0L ) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		List<PasswordFlag> list=new ArrayList<PasswordFlag>();	
		if (checkSessionId(userId, sessionId)) {
			try {
				if(typebanktobroker==1){
					PasswordFlag typeBankToBrokerPwdFlag = passwordFlagService.passwordFlagQuery(userId, 
							accountId, bankNo, 1);
					list.add(typeBankToBrokerPwdFlag);
					
				}
				if(typebrokertobank == 1){
					PasswordFlag typeBrokerToBankPwdFlag = passwordFlagService.passwordFlagQuery(userId, 
							accountId, bankNo, 2);
					list.add(typeBrokerToBankPwdFlag);
					
				}
				if(typeamount ==1){
					PasswordFlag typeAmountPwdFlag = passwordFlagService.passwordFlagQuery(userId, 
							accountId, bankNo, 3);
					list.add(typeAmountPwdFlag);
					
				}
				PasswordFlagResult passwordFlagResult = new PasswordFlagResult();
				passwordFlagResult.setList(list);				
				return OpenResult.ok().add("data", passwordFlagResult).buildJson();
			} catch (ServiceException e) {
				log.error("密码配置异常："+e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	private List<BankTransferVo> setBankTransferVoList(List<BankTransfer> bankTransferList){
		List<BankTransferVo> bankTransferVoList = new ArrayList<BankTransferVo>();
		if(CollectionUtils.isNotEmpty(bankTransferList)){
			for (BankTransfer b : bankTransferList) {
				BankTransferVo bankTransferVo = new BankTransferVo();
				
				bankTransferVo.setAssetProp(b.getAssetProp());
				bankTransferVo.setBranchNo(b.getBranchNo());
				bankTransferVo.setBankNo(b.getBankNo());
				bankTransferVo.setBankName(b.getBankName());
				bankTransferVo.setTransName(b.getTransName());
				bankTransferVo.setEntrustNo(b.getEntrustNo());
				bankTransferVo.setBusinessType(b.getBusinessType());
				bankTransferVo.setSourceFlag(b.getSourceFlag());
				bankTransferVo.setMoneyType(b.getMoneyType());
				bankTransferVo.setOccurBalance(b.getOccurBalance());
				bankTransferVo.setEntrustTime(b.getEntrustTime());
				bankTransferVo.setErrorNo(b.getErrorNo());
				bankTransferVo.setCancelInfo(b.getCancelInfo());
				bankTransferVo.setBankErrorInfo(b.getBankErrorInfo());
				bankTransferVo.setPositionStr(b.getPositionStr());
				bankTransferVo.setRemark(b.getRemark());
				bankTransferVo.setPostBalance(b.getPostBalance());
				bankTransferVo.setInitDate(b.getInitDate());
//				将委托状态 转为 转账状态
				String statusName = b.getEntrustStatus().toString();
				if("SUCCESS".equalsIgnoreCase(statusName)){
					statusName = "REPORTED";
				}
				EntrustStatusVo entrustStatusVo = EumUtils.getEntrustStatusVo(statusName);
				bankTransferVo.setEntrustStatus(entrustStatusVo);
				
				bankTransferVoList.add(bankTransferVo);							
			}
		}
		
		return bankTransferVoList;
	}
}
