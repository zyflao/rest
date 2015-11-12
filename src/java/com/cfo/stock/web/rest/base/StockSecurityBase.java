package com.cfo.stock.web.rest.base;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.cfo.stock.web.rest.result.HistoryFundStockExResult;
import com.cfo.stock.web.rest.result.PositionResult;
import com.cfo.stock.web.rest.result.StockAccountResult;
import com.cfo.stock.web.rest.result.TradeIframeResult;
import com.cfo.stock.web.rest.utils.MapUtils;
import com.cfo.stock.web.rest.utils.ValidateUtil;
import com.jrj.common.utils.PropertyManager;
import com.jrj.stocktrade.api.account.UserAccountService;
import com.jrj.stocktrade.api.account.vo.Broker;
import com.jrj.stocktrade.api.account.vo.UserAccount;
import com.jrj.stocktrade.api.common.BrokerId;
import com.jrj.stocktrade.api.common.BrokerType;
import com.jrj.stocktrade.api.common.EntrustBs;
import com.jrj.stocktrade.api.common.EntrustProp;
import com.jrj.stocktrade.api.common.ExchangeType;
import com.jrj.stocktrade.api.common.ValueableEnumUtil;
import com.jrj.stocktrade.api.deposit.FundService;
import com.jrj.stocktrade.api.deposit.vo.FundInfo;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.iframe.TradeIframeService;
import com.jrj.stocktrade.api.iframe.vo.TradeIframe;
import com.jrj.stocktrade.api.stock.SecurityCodeService;
import com.jrj.stocktrade.api.stock.SecurityStockService;
import com.jrj.stocktrade.api.stock.StockAccountQueryService;
import com.jrj.stocktrade.api.stock.StockHistoryQueryService;
import com.jrj.stocktrade.api.stock.StockQueryService;
import com.jrj.stocktrade.api.stock.vo.BuyLimit;
import com.jrj.stocktrade.api.stock.vo.EntrustResp;
import com.jrj.stocktrade.api.stock.vo.HistoryFundStockEx;
import com.jrj.stocktrade.api.stock.vo.SecurityCodeConfirm;
import com.jrj.stocktrade.api.stock.vo.StockAccount;
import com.jrj.stocktrade.api.stock.vo.StockInfo;

public class StockSecurityBase extends StockBaseRest {

	@Autowired
	private StockHistoryQueryService historyQueryService;

	@Autowired
	private SecurityCodeService codeService;

	@Autowired
	private SecurityStockService securityStockService;

	@Autowired
	private StockQueryService stockQueryService;

	@Autowired
	private StockAccountQueryService stockAccountQueryService;

	@Autowired
	private TradeIframeService tradeIframeService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private FundService fundService;

	/**
	 * 查询资金流水
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param broker
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	protected String queryHistoryFundStockBase(String userId, String sessionId,
			long accountId, String startDate, String endDate) {
		log.info("-->userId:" + userId + "-->sessionId:" + sessionId
				+ "-->accountId:" + accountId + "-->startDate:" + startDate
				+ "-->endDate:" + endDate);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(startDate)
				|| StringUtils.isBlank(endDate)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}

		if (checkSessionId(userId, sessionId)) {
			List<HistoryFundStockEx> historyFundStockExList = new ArrayList<HistoryFundStockEx>();
			try {
				historyFundStockExList = historyQueryService
						.historyFundStockAllQuery(userId, accountId,
								Integer.parseInt(startDate),
								Integer.parseInt(endDate));
				// 倒序排列返回的list
				if (CollectionUtils.isNotEmpty(historyFundStockExList)) {
					reverseHistoryFundStockExList(historyFundStockExList);
				}
				HistoryFundStockExResult fundStockExResult = new HistoryFundStockExResult();
				fundStockExResult.setList(historyFundStockExList);
				return OpenResult.ok().add("data", fundStockExResult)
						.buildJson();
			} catch (NumberFormatException e) {
				log.error(
						"query fund list NumberFormatException"
								+ e.getMessage(), e);
				return OpenResult.parameterError("数据格式异常").buildJson();
			} catch (ServiceException e) {
				log.error("query fund list ServiceException" + e.getMessage(),
						e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
			// catch (Exception e) {
			// log.error("query fund list Exception"+e.getMessage(),e);
			// return OpenResult.unknown("未知异常").buildJson();
			// }

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	/**
	 * 股票代码确认
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	protected String securityCodeEnterBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String stockcode = json.getString("stockcode");
		long accountId = json.getLongValue("accountId");
		String propStr = json.getString("prop");
		EntrustProp prop = (EntrustProp) ValueableEnumUtil.getEnum(propStr,
				EntrustProp.class);
		String exchangeTypeStr = json.getString("exchangeType");
		ExchangeType exchangeType = ExchangeType
				.getExchangeType(exchangeTypeStr);

		log.info("-->userId:" + userId + "-->sessionId:" + sessionId
				+ "-->stockcode:" + stockcode + "-->propStr:" + propStr
				+ "-->accountId:" + accountId + "-->exchangeTypeStr:"
				+ exchangeTypeStr);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(stockcode) || prop == null
				|| accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			SecurityCodeConfirm codeConfirm = null;
			String stAccount = "******";
			StockInfo stockInfo = null;
			try {
				codeConfirm = codeService.securityCodeEnter(userId, accountId,
						prop, exchangeType, stockcode);
				log.info("#codeConfirm:" + JSONObject.toJSONString(codeConfirm));
				UserAccount userAccount = userAccountService.queryAccount(accountId);
				String brokerId = null;
				if (userAccount != null) {
					brokerId = userAccount.getBrokerId();
				}
				Broker broker = brokerHelper.getBroker(brokerId);
				try {
					// 中山证券和ITN类型的券商 股票最大卖出量从持股列表里取
					if (BrokerId.ZSZQ_SECURITIES.equals(brokerId)
							|| BrokerType.ITN == BrokerType.getType(broker.getBrokerType())) {
						List<StockInfo> stockInfoList = stockQueryService.securityStockFastQuery(userId, accountId,stockcode);
						log.info("#securityCodeEnterBase stockInfoList:"+JSONObject.toJSONString(stockInfoList));
						if (CollectionUtils.isNotEmpty(stockInfoList)) {
							stockInfo = stockInfoList.get(0);
							log.info("#securityCodeEnterBase stockInfo:"+JSONObject.toJSONString(stockInfo));
						}
						if (stockInfo != null) {
							BigDecimal enableAmount = stockInfo.getEnableAmount();
							codeConfirm.setEnableAmount(enableAmount);
						}
					}

				} catch (Exception e) {
					log.error("#codeConfirm Exception" + e.getMessage(), e);
					codeConfirm.setEnableAmount(BigDecimal.ZERO);
				}
				//股东账户
				try {
					if (BrokerType.ITN == BrokerType.getType(broker.getBrokerType())) {
						// ITN类型的券商如海通 股票代码确认接口不返回股东账户 stockAccount字段 从其他接口获取
						if (StringUtils.isEmpty(codeConfirm.getStockAccount())) {
							List<StockAccount> stockList = stockAccountQueryService
									.clientStockAccountQuery(userId, accountId);
							if (CollectionUtils.isNotEmpty(stockList)) {
								for (StockAccount stockAccount : stockList) {
									if (StringUtils.equals(ExchangeType.SH.getValue(), stockAccount
											.getExchangeType().getValue())) {
										stAccount = stockAccount.getStockAccount();
										log.info("#ExchangeType.SH-->"+ stAccount);
										codeConfirm.setStockAccount(stAccount);
									}
									if (StringUtils.equals(ExchangeType.SZ.getValue(), stockAccount
											.getExchangeType().getValue())) {
										stAccount = stockAccount.getStockAccount();
										log.info("#ExchangeType.SZ-->"+ stAccount);
										codeConfirm.setStockAccount(stAccount);
									}
								}
							}
						}
					}
				} catch (Exception e) {
					codeConfirm.setStockAccount(stAccount);
				}
				// ITN类型的券商enableBalance可用资金 从资金信息里取
				FundInfo fundInfo = fundService.clientFundFastQuery(userId, accountId);
				if (fundInfo != null) {
					BigDecimal enableBalance = fundInfo.getEnableBalance();
					codeConfirm.setEnableBalance(enableBalance);
				}
				if (BrokerId.CGWS_SECURITIES.equals(brokerId)) {
					codeConfirm.setExchangeType(exchangeType);
				}
				return OpenResult.ok().add("data", codeConfirm).buildJson();
			} catch (ServiceException e) {
				log.error(
						"make sure stock code ServiceException"
								+ e.getMessage(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	/**
	 * 股票买入限制
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	public String EntrustBuyLimitBase(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String stockcode = json.getString("stockcode");
		long accountId = json.getLongValue("accountId");
		String propStr = json.getString("prop");
		EntrustProp prop = (EntrustProp) ValueableEnumUtil.getEnum(propStr,
				EntrustProp.class);
		BigDecimal entrustPrice = json.getObject("entrustPrice",
				BigDecimal.class);
		String exchangeTypeStr = json.getString("exchangeType");
		ExchangeType exchangeType = ExchangeType
				.getExchangeType(exchangeTypeStr);

		log.info("-->userId:" + userId + "-->sessionId:" + sessionId
				+ "-->stockcode:" + stockcode + "-->propStr:" + propStr
				+ "-->entrustPrice:" + entrustPrice + "-->accountId:"
				+ accountId + "-->exchangeTypeStr:" + exchangeTypeStr);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(stockcode) || prop == null
				|| entrustPrice == null || exchangeType == null) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			BuyLimit buyLimit;
			try {
				if (entrustPrice.compareTo(BigDecimal.ZERO) == 0) {
					return OpenResult.serviceError(-1, "委托价格不能为0").buildJson();
				}
				buyLimit = codeService.securityEntrustBuyLimit(userId,
						accountId, prop, exchangeType, stockcode, entrustPrice);
				return OpenResult.ok().add("data", buyLimit).buildJson();
			} catch (ServiceException e) {
				log.error(
						"lmiit of buy stock ServiceException" + e.getMessage(),
						e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	/**
	 * 股票买入或者卖出 中信模式
	 * 
	 * @param content
	 * @return
	 */
	public String dealBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String password = json.getString("password");
		long accountId = json.getLongValue("accountId");
		String propStr = json.getString("prop");
		EntrustProp prop = (EntrustProp) ValueableEnumUtil.getEnum(propStr,
				EntrustProp.class);
		String stockcode = json.getString("stockcode");
		BigDecimal entrustAmount = json.getObject("entrustAmount",
				BigDecimal.class);
		BigDecimal entrustPrice = json.getObject("entrustPrice",
				BigDecimal.class);
		String bs = json.getString("entrustBs");
		EntrustBs entrustBs = (EntrustBs) ValueableEnumUtil.getEnum(bs,
				EntrustBs.class);
		String exchangeTypeStr = json.getString("exchangeType");
		ExchangeType exchangeType = ExchangeType
				.getExchangeType(exchangeTypeStr);
		// 接入ITN券商时校验此字段
		String brokerType = json.getString("brokerType");
		String tradeWay = json.getString("tradeWay");// 交易类型
		log.info("-->userId:" + userId + "-->sessionId:" + sessionId
				+ "-->stockcode:" + stockcode + "-->password:" + password == null ? "交易密码为空"
				: "交易密码不为空" + "-->entrustAmount:" + entrustAmount
						+ "-->entrustPrice:" + entrustPrice + "-->propStr:"
						+ propStr + "-->entrustBs:" + entrustBs
						+ "-->accountId:" + accountId + "-->exchangeTypeStr:"
						+ exchangeTypeStr + "-->brokerType:" + brokerType
						+ "-->tradeWay:" + tradeWay);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(stockcode) || prop == null
				|| entrustPrice == null || entrustAmount == null
				|| entrustBs == null || exchangeType == null) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		// brokerType "0". 自接 "1". 恒生/ITN
		if (StringUtils.isEmpty(tradeWay)) { // 兼容老版本2.0.0之前的版本 tradeWay=null
												// tradeWay为空说明交易类型采用自接
			if (StringUtils.isBlank(password)) {
				return OpenResult.parameterError("请输入密码").buildJson();
			}
		} else {
			if (StringUtils.equals("0", tradeWay)) {// tradeWay
													// 不为空并且tradeWay==0交易类型也是采用自接
				if (StringUtils.isBlank(password)) {
					return OpenResult.parameterError("请输入密码").buildJson();
				}
			} else {
				password = "";
			}
		}
		if (checkSessionId(userId, sessionId)) {
			EntrustResp entrustResp;
			try {
				// 买入股票判断 购买数量
				if (EntrustBs.BUY.equals(entrustBs)) {
					MathContext mc = new MathContext(0, RoundingMode.HALF_DOWN);
					BigDecimal rs = entrustAmount.divide(new BigDecimal(100),
							mc);
					Boolean flag = ValidateUtil.isInteger(rs.toString());
					if (!flag) {
						return OpenResult.parameterError(-61,
								"购买数量必须大于0,并且是100的整数").buildJson();
					}
					BuyLimit buyLimit = codeService.securityEntrustBuyLimit(
							userId, accountId, prop, exchangeType, stockcode,
							entrustPrice);
					if (buyLimit != null) {
						BigDecimal buyAmount = buyLimit.getEnableBuyAmount();
						if (entrustAmount.compareTo(buyAmount) > 0) {
							return OpenResult.parameterError(-62, "超过最大买入量")
									.buildJson();
						}
					}
				}

				if (EntrustBs.SELL.equals(entrustBs)) {
					// 卖出数量必须大于0
					int compareRes = entrustAmount.compareTo(BigDecimal.ZERO);
					if (compareRes == -1 || compareRes == 0) {
						return OpenResult.parameterError(-61, "卖出数量必须大于0")
								.buildJson();
					}
					StockInfo stockInfo = null;
					List<StockInfo> stockInfoList = stockQueryService
							.securityStockFastQuery(userId, accountId,
									stockcode);
					if (CollectionUtils.isNotEmpty(stockInfoList)) {
						stockInfo = stockInfoList.get(0);
					}
					if (stockInfo != null) {
						BigDecimal enableAmount = stockInfo.getEnableAmount();
						if (entrustAmount.compareTo(enableAmount) > 0) {
							return OpenResult.parameterError(-62, "超过最大卖出量")
									.buildJson();

						}
					}
				}

				entrustResp = securityStockService.securityEntrust(userId,
						accountId, password, prop, stockcode, entrustAmount,
						entrustPrice, entrustBs, exchangeType);
				return OpenResult.ok().add("data", entrustResp).buildJson();
			} catch (ServiceException e) {
				log.error("stock deal ServiceException" + e.getMessage(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();

			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	/**
	 * 股票买入或者卖出 中山模式 第一步
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	public String entrustBusinessBase(String content) {

		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		String propStr = json.getString("prop");
		EntrustProp prop = (EntrustProp) ValueableEnumUtil.getEnum(propStr,
				EntrustProp.class);

		String stockcode = json.getString("stockcode");
		BigDecimal entrustAmount = json.getObject("entrustAmount",
				BigDecimal.class);
		BigDecimal entrustPrice = json.getObject("entrustPrice",
				BigDecimal.class);
		String bs = json.getString("entrustBs");
		EntrustBs entrustBs = (EntrustBs) ValueableEnumUtil.getEnum(bs,
				EntrustBs.class);
		String brokerId = json.getString("broker");
		String exchangeTypeStr = json.getString("exchangeType");
		ExchangeType exchangeType = ExchangeType
				.getExchangeType(exchangeTypeStr);

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(stockcode) || prop == null
				|| entrustPrice == null || entrustAmount == null
				|| entrustBs == null || exchangeType == null) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		String returnUrl = PropertyManager.getString("buysell_callBack_url")
				.replaceAll("\\$accountId", String.valueOf(accountId));
		// 兼容老接口股票交易
		if (StringUtils.isNotBlank(brokerId)) {
			returnUrl = PropertyManager.getString("old_buysell_callBack_url")
					.replaceAll("\\$brokerId", brokerId);
		}
		log.info("<--userId:" + userId + "<--sessionId:" + sessionId
				+ "<--accountId:" + accountId + "<--prop:" + prop
				+ "<--stockcode:" + stockcode + "<--entrustAmount:"
				+ entrustAmount + "<--entrustPrice:" + entrustPrice + "<--bs:"
				+ entrustBs + "<--exchangeType:" + exchangeType
				+ "<--returnUrl:" + returnUrl);
		if (checkSessionId(userId, sessionId)) {
			try {
				// 买入股票判断 购买数量
				if (EntrustBs.BUY.equals(entrustBs)) {
					MathContext mc = new MathContext(0, RoundingMode.HALF_DOWN);
					BigDecimal rs = entrustAmount.divide(new BigDecimal(100),
							mc);
					Boolean flag = ValidateUtil.isInteger(rs.toString());
					if (!flag) {
						return OpenResult.parameterError(-61,
								"购买数量必须大于0,并且是100的整数").buildJson();
					}
					BuyLimit buyLimit = codeService.securityEntrustBuyLimit(
							userId, accountId, prop, exchangeType, stockcode,
							entrustPrice);
					if (buyLimit != null) {
						BigDecimal buyAmount = buyLimit.getEnableBuyAmount();
						if (entrustAmount.compareTo(buyAmount) > 0) {
							return OpenResult.parameterError(-62, "超过最大买入量")
									.buildJson();
						}
					}
				}

				if (EntrustBs.SELL.equals(entrustBs)) {
					// 卖出数量必须大于0
					int compareRes = entrustAmount.compareTo(BigDecimal.ZERO);
					if (compareRes == -1 || compareRes == 0) {
						return OpenResult.parameterError(-61, "卖出数量必须大于0")
								.buildJson();
					}
					StockInfo stockInfo = null;
					List<StockInfo> stockInfoList = stockQueryService
							.securityStockFastQuery(userId, accountId,
									stockcode);
					if (CollectionUtils.isNotEmpty(stockInfoList)) {
						stockInfo = stockInfoList.get(0);
					}
					if (stockInfo != null) {
						BigDecimal enableAmount = stockInfo.getEnableAmount();
						if (entrustAmount.compareTo(enableAmount) > 0) {
							return OpenResult.parameterError(-62, "超过最大卖出量")
									.buildJson();

						}
					}
				}

				String url = "";
				String data = "";
				String sign = "";
				String param = "";
				TradeIframe tradeIframe = tradeIframeService.securityEntrust(
						userId, accountId, prop, stockcode, entrustAmount,
						entrustPrice, entrustBs, exchangeType, returnUrl);
				if (tradeIframe != null) {
					url = tradeIframe.getUrl();
					if (StringUtils.isNotBlank(url)) {
						url = url.trim();
					}
					Map<String, String> map = tradeIframe.getParam();
					if (map != null && map.size() > 0) {
						data = map.get("data");
						sign = map.get("sign");
						param = MapUtils.mapToString(map);
					}
				}

				TradeIframeResult result = new TradeIframeResult(url, data,
						sign);
				result.setCallUrl(returnUrl);
				result.setParam(param);
				result.setMethod("get");
				return OpenResult.ok().add("data", result).buildJson();
			} catch (ServiceException e) {
				log.error("stock deal ServiceException" + e.getMessage(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();

			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	/**
	 * 股票买入或者卖出 中山模式 第二步
	 * 
	 * @param content
	 * @return
	 */
	protected String receiveParamBase(String content) {
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		String data = json.getString("data");
		String sign = json.getString("sign");
		log.info("<--userId:" + userId + "<--sessionId:" + sessionId
				+ "<--accountId:" + accountId + "<--data:" + data + "<--sign:"
				+ sign);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| accountId == 0L || StringUtils.isBlank(data)
				|| StringUtils.isBlank(sign)) {
			return OpenResult.parameterError("参数不正确").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("data", data);
				param.put("sign", sign);
				EntrustResp entrustResp = tradeIframeService
						.securityEntrustReceive(userId, accountId, param);
				return OpenResult.ok().add("data", entrustResp).buildJson();
			} catch (ServiceException e) {
				log.error(
						"stock deal receiveParam ServiceException"
								+ e.getMessage(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	protected String queryClientPositionBase(String userId, String sessionId,
			long accountId, String stockcode, String positionStr, int size) {
		log.info("<--userId:" + userId + "<--sessionId:" + sessionId
				+ "<--accountId:" + accountId + "<--stockcode:" + stockcode
				+ "<--positionStr:" + positionStr + "<--size:" + size);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			List<StockInfo> stockInfoList = new ArrayList<StockInfo>();
			try {
				PositionResult positionResult = new PositionResult();
				// 查询指定股票持仓
				if (StringUtils.isNotEmpty(stockcode)) {
					stockInfoList = stockQueryService.securityStockFastQuery(
							userId, accountId, stockcode);
					positionResult.setStockInfoList(stockInfoList);
					return OpenResult.ok().add("data", positionResult)
							.buildJson();
				} else if (size != 0) {
					if (StringUtils.isBlank(positionStr)) {
						positionStr = "1";
					}
					stockInfoList = stockQueryService.securityStockFastQuery(
							userId, accountId, positionStr, size);
					positionResult.setStockInfoList(stockInfoList);
					return OpenResult.ok().add("data", positionResult)
							.buildJson();
				} else {
					stockInfoList = stockQueryService.securityStockFastQuery(
							userId, accountId);
					positionResult.setStockInfoList(stockInfoList);
					return OpenResult.ok().add("data", positionResult)
							.buildJson();
				}
			} catch (ServiceException e) {
				log.error("query client position Exception" + e.getMessage(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
			// catch (Exception e) {
			// log.error("query client position Exception"+e.getMessage(),e);
			// return OpenResult
			// .serviceError("-1", e.getMessage())
			// .buildJson();
			// }
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	/**
	 * 查询证券股东帐户
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @param broker
	 * @param positionStr
	 * @param size
	 * @return
	 */
	protected String clientStockAccountBase(String userId, String sessionId,
			long accountId, String positionStr, int size) {
		log.info("<--userId:" + userId + "<--sessionId:" + sessionId
				+ "<--accountId:" + accountId + "<--positionStr:" + positionStr
				+ "<--size:" + size);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		List<StockAccount> stockAccounts = null;
		if (checkSessionId(userId, sessionId)) {
			StockAccountResult accountResult = new StockAccountResult();
			try {
				if (size != 0) {
					if (StringUtils.isEmpty(positionStr)) {
						positionStr = "1";
					}
					stockAccounts = stockAccountQueryService
							.clientStockAccountQuery(userId, accountId,
									positionStr, size);
				} else {
					stockAccounts = stockAccountQueryService
							.clientStockAccountQuery(userId, accountId);
				}
				accountResult.setStockAccountList(stockAccounts);
				return OpenResult.ok().add("data", accountResult).buildJson();
			} catch (ServiceException e) {
				log.error(
						"query client Stock  Account ServiceException"
								+ e.getErrorInfo(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
			// catch (Exception e) {
			// log.error("query client Stock  Account Exception"+e.getMessage(),e);
			// return OpenResult
			// .serviceError("-1", e.getMessage())
			// .buildJson();
			// }
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
}
