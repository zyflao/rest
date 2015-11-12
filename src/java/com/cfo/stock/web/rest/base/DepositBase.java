package com.cfo.stock.web.rest.base;

import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.cfo.stock.web.rest.common.ConstantsError;
import com.cfo.stock.web.rest.result.BankTransferResult;
import com.cfo.stock.web.rest.utils.BigDecimalUtils;
import com.cfo.stock.web.rest.utils.EumUtils;
import com.cfo.stock.web.rest.vo.FundInfoVo;
import com.jrj.stocktrade.api.banktrans.BankTransferQueryService;
import com.jrj.stocktrade.api.banktrans.vo.BankTransfer;
import com.jrj.stocktrade.api.common.MoneyType;
import com.jrj.stocktrade.api.deposit.BankFundService;
import com.jrj.stocktrade.api.deposit.FundService;
import com.jrj.stocktrade.api.deposit.vo.FundAmtQueryResp;
import com.jrj.stocktrade.api.deposit.vo.FundInfo;
import com.jrj.stocktrade.api.deposit.vo.FundInfoEx;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.stock.StockQueryService;
import com.jrj.stocktrade.api.stock.vo.StockInfo;

public class DepositBase extends StockBaseRest {

	@Autowired
	private FundService fundService;

	@Autowired
	private BankFundService bankFundService;
	
	@Autowired
	private StockQueryService stockQueryService;
	
	@Autowired
	private BankTransferQueryService bankTransferQueryService;
	
	/**资金股份 - 资金查询
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param brokerStr
	 * @return
	 */
	public String clientFundBase(String userId,
			String sessionId,long accountId) {	
		log.info("<--userId:"+userId+"<--sessionId:"+sessionId+"<--accountId:"+accountId);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)|| accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
//		incomeBalance盈亏                  assetPrice市值价
		BigDecimal deficitAmount=BigDecimal.ZERO;  //		亏损 
		BigDecimal profitAmount = BigDecimal.ZERO ; //		盈利
		BigDecimal fairAmount=BigDecimal.ZERO;  //		持平数量
		BigDecimal incomeBalance = BigDecimal.ZERO; 
		BigDecimal assetPrice = BigDecimal.ZERO;
		FundInfoVo fundInfoVo = new FundInfoVo();
		if (checkSessionId(userId, sessionId)) {
			
			
			BigDecimal totalIncomeBalance = BigDecimal.ZERO;
			BigDecimal totalAssetPrice = BigDecimal.ZERO; 
			try {
				FundInfo fundInfo=fundService.clientFundFastQuery(userId, accountId);
				FundInfoEx fundInfoEx=fundService.clientFundAllQuery(userId,accountId);
				
				if(fundInfo!=null){
					fundInfoVo.setCurrentBalance(fundInfo.getCurrentBalance());
					fundInfoVo.setEnableBalance(fundInfo.getEnableBalance());
					fundInfoVo.setFetchBalance(BigDecimalUtils.roundBigDecimal(fundInfo.getFetchBalance()));
					fundInfoVo.setFrozenBalance(fundInfo.getFrozenBalance());
					fundInfoVo.setUnfrozenBalance(fundInfo.getUnfrozenBalance());
					
				}
				if(fundInfoEx != null){
					fundInfoVo.setFundBalance(fundInfoEx.getAssetBalance());
					fundInfoVo.setAssetPrice(fundInfoEx.getMarketValue());
				}
				try {
					List<StockInfo> stockInfoList=stockQueryService.securityStockFastQuery(userId, accountId);
					if(CollectionUtils.isNotEmpty(stockInfoList)){
						for(StockInfo stockInfo:stockInfoList){
							incomeBalance = stockInfo.getIncomeBalance();
							assetPrice=stockInfo.getAssetPrice();
							
							totalIncomeBalance = totalIncomeBalance.add(incomeBalance);
							if(assetPrice == null){
								assetPrice = BigDecimal.ZERO;
							}
							totalAssetPrice = totalAssetPrice.add(assetPrice);
							
							if(incomeBalance.compareTo(BigDecimal.ZERO)==1){
								profitAmount = profitAmount.add(BigDecimal.ONE);
							}else if(incomeBalance.compareTo(BigDecimal.ZERO)==-1){
								deficitAmount = deficitAmount.add(BigDecimal.ONE);
							}else{
								fairAmount = fairAmount.add(BigDecimal.ONE);
							}
						}
						
					}
				} catch (Exception e) {
					log.error("获取持仓错误！",e);;
				}
				fundInfoVo.setIncomeBalance(totalIncomeBalance);
				fundInfoVo.setProfitAmount(profitAmount);
				fundInfoVo.setDeficitAmount(deficitAmount);
				fundInfoVo.setFairAmount(fairAmount);
				
				
//				今日盈亏比例
				try {
					BigDecimal todayGenLose = fundService.caculateTodayGenLoseRate(userId, accountId, fundInfoEx);
					String percent = BigDecimalUtils.processDecimalFraction(todayGenLose).toString();
					fundInfoVo.setPercent(percent+"%");
				} catch (Exception e) {
					log.error("获取今日盈亏错误！",e);;
				}
				
				return OpenResult.ok().add("data", fundInfoVo).buildJson();
			} 
			catch (ServiceException e) {
				log.error("query fund ServiceException-->"+e.getMessage(),e);
				return OpenResult.serviceError(e.getErrorNo(), OpenResult.SERVER_BUSY_MSG).buildJson();

			} catch(Exception e){
				log.error("async query Exception",e);
				return OpenResult.serviceError("-501", OpenResult.SERVER_BUSY_MSG).buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	
	/**查询银行卡余额
	 * @param headers
	 * @param content
	 * @return
	 */
	public String fundAmountBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String password = json.getString("password");
		String fundPassword = json.getString("fundPassword");
		String bankPassword = json.getString("bankPassword");
		String bankNo = json.getString("bankNo");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		String moneyTypeStr = json.getString("moneyType");
		MoneyType moneyType = EumUtils.getMoneyType(moneyTypeStr);
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->bankNo:"+bankNo+
				"-->fundPassword:"+fundPassword==null?"fundPassword为空":"fundPassword不为空"+
				"-->bankPassword:"+bankPassword==null?"bankPassword为空":"bankPassword不为空"+
				"-->accountId:"+accountId+"-->moneyType:"+moneyType);	
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| accountId == 0L|| StringUtils.isBlank(bankNo) || moneyType == null) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				FundAmtQueryResp fundAmtQueryResp = bankFundService.fundAmountQuery(userId,
						accountId, password, fundPassword, bankPassword, bankNo, moneyType);
				return OpenResult.ok().add("data", fundAmtQueryResp)
						.buildJson();
			} catch (ServiceException e) {
				log.error("查询银行卡余额异常："+e);
				return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo()).buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	
	/**查询银行卡余额  
	 * 将查询银行余额返回流水号接口和通过银证转账流水查询余额两个接口合并
	 * @param headers
	 * @param content
	 * @return
	 */
	public String queryBankAmountBase(String content) {
		if (StringUtils.isBlank(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String password = json.getString("password");
		String fundPassword = json.getString("fundPassword");
		String bankPassword = json.getString("bankPassword");
		String bankNo = json.getString("bankNo");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		String brokerStr = json.getString("broker");
		String moneyTypeStr = json.getString("moneyType");		
		MoneyType moneyType = EumUtils.getMoneyType(moneyTypeStr);
//		打印日志
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->bankNo:"+bankNo+
				"-->fundPassword:"+fundPassword==null?"fundPassword为空":"fundPassword不为空"+
				"-->bankPassword:"+bankPassword==null?"bankPassword为空":"bankPassword不为空"+
				"-->accountId:"+accountId+"-->moneyType:"+moneyType);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| brokerStr == null|| StringUtils.isBlank(bankNo) || moneyType == null) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				BankTransferResult  transferResult = new BankTransferResult();
				FundAmtQueryResp fundAmtQueryResp = bankFundService.fundAmountQuery(userId,accountId, password,
						fundPassword, bankPassword, bankNo, moneyType);
				if(fundAmtQueryResp!=null){
					// 根据流水查询余额					
					BankTransfer bankTransfer = bankTransferQueryService.bankTransferQuery(userId,accountId, password, 
							fundAmtQueryResp.getSerialNo());
					if(bankTransfer!=null){
						transferResult.setSerialNo(fundAmtQueryResp.getSerialNo());						
						if("4".equals(bankTransfer.getBusinessType().getValue())){
							transferResult.setOccurBalance(bankTransfer.getOccurBalance().toString());
						}else{
							return OpenResult.serviceError(ConstantsError.BANKTRANS_NO_RESULT, ConstantsError.BANKTRANS_NO__MSG).buildJson(); 
						}
						return OpenResult.ok().add("data", transferResult)
								.buildJson();
					}else{
						return OpenResult.serviceError(ConstantsError.BANKTRANS_RESULT_ERROR, ConstantsError.BANKTRANS_RESULT_MSG).buildJson(); 
					}
					
				}else{
					return OpenResult.serviceError(ConstantsError.BANKTRANS_SERIALNO_ERROR,ConstantsError.BANKTRANS_SERIALNO_MSG).buildJson();
				}
				
			} catch (ServiceException e) {
				log.error("query bank amount ServiceException"+e);
				return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo()).buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
}
