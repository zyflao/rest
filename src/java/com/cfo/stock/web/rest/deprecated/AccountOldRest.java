package com.cfo.stock.web.rest.deprecated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.cfo.stock.web.rest.base.AccountBase;
import com.cfo.stock.web.rest.exception.StockServiceException;
import com.cfo.stock.web.rest.globals.StockGlobals;
import com.cfo.stock.web.rest.interceptors.SwitchAccountIdInterceptor.ChangeAccountId;
import com.cfo.stock.web.rest.result.BindInfoResult;
import com.cfo.stock.web.rest.result.BindSendResult;
import com.cfo.stock.web.rest.result.BindedResult;
import com.cfo.stock.web.rest.result.BrokerEx;
import com.cfo.stock.web.rest.result.BrokersResult;
import com.cfo.stock.web.rest.result.ModelConfigResult;
import com.cfo.stock.web.rest.result.OpenAccountReqResult;
import com.cfo.stock.web.rest.result.YLBStatusResult;
import com.cfo.stock.web.rest.service.LoginOutService;
import com.cfo.stock.web.rest.service.PersonalService;
import com.cfo.stock.web.rest.utils.MapUtils;
import com.cfo.stock.web.rest.utils.ValidateUtil;
import com.jrj.common.utils.PropertyManager;
import com.jrj.stocktrade.api.account.AccountQueryService;
import com.jrj.stocktrade.api.account.AccountService;
import com.jrj.stocktrade.api.account.UserAccountService;
import com.jrj.stocktrade.api.account.UserAuthService;
import com.jrj.stocktrade.api.account.UserInfoService;
import com.jrj.stocktrade.api.account.vo.BindInfo;
import com.jrj.stocktrade.api.account.vo.BindReq;
import com.jrj.stocktrade.api.account.vo.Broker;
import com.jrj.stocktrade.api.account.vo.FundAccount;
import com.jrj.stocktrade.api.account.vo.UserAccAuth;
import com.jrj.stocktrade.api.account.vo.UserInfo;
import com.jrj.stocktrade.api.common.BrokerId;
import com.jrj.stocktrade.api.common.IdType;
import com.jrj.stocktrade.api.common.StockAccountType;
import com.jrj.stocktrade.api.common.UserStatus;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.opstatus.StockAccountStatusService;
import com.jrj.stocktrade.api.opstatus.vo.OpenAccountReq;
import com.jrj.stocktrade.api.pub.BranchQueryService;
import com.jrj.stocktrade.api.pub.vo.BranchQueryResp;
import com.jrj.stocktrade.api.stock.StockAccountQueryService;
/**
 * 
 * @className：AccountRest
 * @classDescription： 账户相关接口
 * @author：kecheng.Li
 * 
 * @dateTime：2014年4月19日 上午11:54:39
 */
@Path(StockBaseRest.baseuri + "/account")
@Controller
public class AccountOldRest extends AccountBase {

	// 券商账户相关接口
	@Autowired
	private AccountService accountService;

	@Autowired
	private PersonalService personalService;

	@Autowired
	private AccountQueryService accountQueryService;

	@Autowired
	private BranchQueryService branchQueryService;

	@Autowired
	private LoginOutService loginOutService;

	@Autowired
	private StockAccountStatusService stockAccountStatusService;

	@Autowired
	private StockAccountQueryService stockAccountQueryService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private UserAuthService userAuthService;
	
	@Autowired
	private UserInfoService userInfoService;

	/**
	 * 可绑券商列表
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @return
	 */
	@Path("/brokerlist/available")
	@GET
	@Produces("application/json;charset=utf-8")
	public String availableBrokerList(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId) {
		if (checkSessionId(userId, sessionId)) {
			List<Broker> brokers = new ArrayList<Broker>();
			List<BrokerEx> brokerExList = new ArrayList<BrokerEx>();
			try {
				brokers = accountService.queryBrokers(userId);
				BrokersResult brokersResult = new BrokersResult();
				for (Broker broker : brokers) {
					String brokerId = broker.getBrokerId();
					if (BrokerId.ZSZQ_SECURITIES.equals(brokerId)) {
						String mark = "";
						String tip = "为年轻一代理财的互联网券商";
						BrokerEx brokerEx = new BrokerEx(mark, tip, "1", "1",
								"1", broker);
						brokerEx.setOrder(2);
						brokerExList.add(brokerEx);
					}
					
					 if(BrokerId.CITIC_SECURITIES.equals(brokerId)){
						 String mark = ""; String tip="大品牌，佣金低，全国网点最多";
					 BrokerEx brokerEx = new BrokerEx(mark, tip,"1", "1",
							 	"-1",broker);
					 brokerEx.setOrder(1);
					 brokerExList.add(brokerEx);
					 }					 
				}
				orderAvailableBrokerList(brokerExList);
				brokersResult.setBrokerList(brokerExList);
				return OpenResult.ok().add("data", brokersResult).buildJson();
			} catch (ServiceException e) {
				log.error("查询可绑券商列表异常:" + e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	/**
	 * 查询用户已绑定券商
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @return
	 */
	@Path("/brokerlist/binded")
	@GET
	@Produces("application/json;charset=utf-8")
	public String bindedBrokerList(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId) {
				// 真实姓名 realName
				// 账户营业部 branchName
				// 资金账户 account
				long accountId = 0L;
				String brokerId = null;
				String fundId = "--";
				String branchName = "--";
				String realname = "";
				String idNumber = "";
				List<Broker> bindedList = new ArrayList<Broker>();
				if (checkSessionId(userId, sessionId)) {
					BindedResult bindedResult = new BindedResult();
					 List<UserAccAuth> userAccAuthlist = new ArrayList<UserAccAuth>();
					try {
						userAccAuthlist = userAuthService.queryAccessAble(userId);
						if(CollectionUtils.isNotEmpty(userAccAuthlist)){
							 for (UserAccAuth userAccAuth : userAccAuthlist){
									boolean isDef = userAccAuth.isDef();
									if(isDef){
										accountId = userAccAuth.getAccountId();
										brokerId = userAccAuth.getBrokerId();
								}
							}
//							没有默认券商  取第一个
							if(accountId == 0L){
								UserAccAuth usuth = userAccAuthlist.get(0);					
								accountId = usuth.getAccountId();	
								brokerId = usuth.getBrokerId();
							}
							if(StringUtils.isNotBlank(brokerId)){
								Broker broker = accountService.queryBroker(brokerId);
								bindedList.add(broker);
							}
						}
						
						JSONObject result = getUserBaseInfo(userId);
						if (result != null) {
							if (result.getIntValue("retcode") != 0) {
								return result.toString();
							}
							realname = result.getString("realName");
							idNumber = result.getString("idNumber");

						} else {
							return OpenResult.unknown("服务异常").buildJson();
						}
						log.info("userId:"+userId+"<--sessionId:"+sessionId+"<--accountId:"+accountId);
						if(accountId != 0){
							List<FundAccount> fundAccountList = accountQueryService
									.fundAccountQuery(userId, accountId);
						if (CollectionUtils.isNotEmpty(fundAccountList)) {
							FundAccount fundAccount = getFundAccount(
									fundAccountList, userId,accountId);
							if (fundAccount != null) {
								fundId = fundAccount.getFundAccount();
								BranchQueryResp branchQueryResp = branchQueryService
										.branchQuery(userId, accountId, String.valueOf(fundAccount.getBranchNo()),idNumber);
								branchName = branchQueryResp.getBranchName();	
							}
						}
						}
						bindedResult.setBindedList(bindedList);
						bindedResult.setBranchName(branchName);
						bindedResult.setFundAccount(fundId);
						bindedResult.setRealname(realname);
						return OpenResult.ok().add("data", bindedResult).buildJson();
					 }catch (ServiceException e) {
						log.error("query binded BrokerList ServiceException" +e.getMessage(), e);
						return OpenResult
								.serviceError(e.getErrorNo(), e.getErrorInfo())
								.buildJson();
					}catch (Exception e) {
						log.error("query binded BrokerList Exception" +e.getMessage(), e);
						return OpenResult
								.serviceError("-1", e.getMessage()).buildJson();
					}

				} else {
					return OpenResult.noAccess("未授权").buildJson();
				}
	}

	/**
	 * 券商绑定状态
	 * 
	 * @param headers
	 * @param userId
	 * @param sessionId
	 * @return
	 */
	@Path("/broker/status")
	@GET
	@Produces("application/json;charset=utf-8")
	public String brokerStatus(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("brokerId") String brokerId) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(brokerId)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				ModelConfigResult result = setModelConfig(brokerId);
				BindInfoResult bindInfoResult = new BindInfoResult(result);
				BindInfo bindInfo = accountService.getBindInfo(userId, brokerId);
				if (bindInfo != null) {
					bindInfoResult.setFundAccount(bindInfo.getFundAccount());
					bindInfoResult.setBindStauts("9");
					bindInfoResult.setClientName(bindInfo.getClientName());
				} else {
					bindInfoResult.setDafaultBroker("");
					bindInfoResult.setFundAccount("");
					bindInfoResult.setBindStauts("-9");
					bindInfoResult.setClientName("");
				}
				return OpenResult.ok().add("data", bindInfoResult).buildJson();
			} catch (ServiceException e) {
				log.error("券商绑定状态异常：" + e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	@Path("/bind/urlparam")
	@GET
	@Produces("application/json;charset=utf-8")
	public String getBindingTokenURL(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("brokerId") String brokerId) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(brokerId)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		String returnUrl = StockGlobals.BIND_CALLBACK_URL.replaceAll(
				"\\$brokerId", brokerId);
		if (checkSessionId(userId, sessionId)) {
			try {

//				String str = getRealNameIDNo(userId);
				JSONObject json = getUserBaseInfo(userId);
				log.info("call bindSend method params :userId" + userId
						+ "--realname:" + json.getString("realName")
						+ "---idnumber：" + json.getString("idNumber"));
				BindReq bindReq = accountService.bindSend(userId, IdType.ID,
						json.getString("idNumber"), json.getString("realName"),
						returnUrl, brokerId,json.getString("mobileNo"));
				BindSendResult bindSendResult = new BindSendResult();
				log.info("catch bindReq param is--:"
						+ JSONObject.toJSONString(bindReq));
				if (bindReq != null) {
					if (bindReq.getErrorNo() != 0) {
						return OpenResult.serviceError(bindReq.getErrorNo(),
								bindReq.getErrorInfo()).buildJson();
					}
					Map<String, String> map = bindReq.getParam();
					if(!map.isEmpty()){					
						if (BrokerId.ZSZQ_SECURITIES.equals(brokerId)) {
							String data = map.get("data");
							String sign = map.get("sign");
							bindSendResult.setSign(sign);
							bindSendResult.setData(data);
							bindSendResult.setParam(MapUtils.mapToString(map));
							bindSendResult.setMethod("get");
						} else if (BrokerId.CITIC_SECURITIES.equals(brokerId)) {
							String s = map.get("s");
							String q = map.get("q");
							bindSendResult.setS(s);
							bindSendResult.setQ(q);
							bindSendResult.setParam("s=" + s + "&q=" + q);						
							bindSendResult.setMethod("post");
						}else{
							bindSendResult.setParam(MapUtils.mapToString(map));
							bindSendResult.setMethod("post");
						}						
					}
					bindSendResult.setUrl(bindReq.getUrl());
					bindSendResult.setCallUrl(returnUrl);
					return OpenResult.ok().add("data", bindSendResult)
							.buildJson();
				} else {
					return OpenResult.nullObject("获取对象为空").buildJson();
				}
			} catch (ServiceException e) {
				log.error("catch bind url ServiceException" + e.getMessage(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			} catch (Exception e) {
				log.error("catch bind url Exception" + e.getMessage(), e);
				return OpenResult.unknown(e.getMessage()).buildJson();
			}

		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	@Path("/broker/bind")
	@POST
	@Produces("application/json;charset=utf-8")
	public String goToBindSecurities(String content) {
		if (StringUtils.isBlank(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String brokerId = json.getString("brokerId");
		String p = json.getString("p");
		String s = json.getString("s");
		String data = json.getString("data");
		String sign = json.getString("sign");
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(brokerId)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		Map<String, Object> param = new HashMap<String, Object>();
		if (checkSessionId(userId, sessionId)) {

			log.info("ZXZQ param p is-->" + p + "<---param s is-->" + s);
			log.info("ZSZQ param data is-->" + data);
			log.info("ZSZQ param sign is-->" + sign);
			try {
				if (BrokerId.ZSZQ_SECURITIES.equals(brokerId)) {
					param.put("p", data);
					param.put("s", sign);
				} else {
					param.put("p", p);
					param.put("s", s);
				}
				ModelConfigResult modelConfigResult = setModelConfig(brokerId);
				BindInfoResult bindInfoResult = new BindInfoResult(
						modelConfigResult);
				log.info("param is --->" + JSONObject.toJSON(param));
				BindInfo bindInfo = accountService.bindReceive(userId, brokerId,
						param);
				log.info("submit param is result bindInfo-->"
						+ JSONObject.toJSONString(bindInfo));
				if (bindInfo != null) {
					bindInfoResult.setDafaultBroker(brokerId);
					bindInfoResult.setFundAccount(bindInfo.getFundAccount());
					bindInfoResult.setBindStauts("9");
					bindInfoResult.setClientName(bindInfo.getClientName());
				} else {
					bindInfoResult.setDafaultBroker("");
					bindInfoResult.setFundAccount("");
					bindInfoResult.setBindStauts("-9");
					bindInfoResult.setClientName("");
				}

				return OpenResult.ok().add("data", bindInfoResult).buildJson();
			} catch (ServiceException e) {
				log.error("bind broker ServiceException-->" + e.getErrorInfo(),
						e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			} catch (Exception e) {
				log.error("bind broker Exception-->" + e.getMessage(), e);
				return OpenResult.unknown(e.getMessage()).buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	@Path("/broker/open")
	@POST
	@Produces("application/json;charset=utf-8")
	public String openStockAccount(String content) {
		if (StringUtils.isBlank(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String brokerId = json.getString("brokerId");
		int accountType = json.getIntValue("accountType");
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(brokerId) || accountType == 0) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				JSONObject userInfo = getUserBaseInfo(userId);
				OpenAccountReqResult openAccountReqResult = new OpenAccountReqResult();

				StockAccountType type = StockAccountType
						.getStockAccountType(accountType);
				String realName = userInfo.getString("realName");
				String idNumber =  userInfo.getString("idNumber");
				String mobileNo = userInfo.getString("mobileNo");
				
				log.info("<--realName:"+realName+"<--idnNmber:"+idNumber+"<--mobileNo:"+mobileNo);
				
				OpenAccountReq openAccountReq = stockAccountStatusService
						.insert(userId, realName,IdType.ID,idNumber,mobileNo, brokerId, type);

				String ZXZQFlag = BrokerId.CITIC_SECURITIES;
				String ZSZQFlag = BrokerId.ZSZQ_SECURITIES;
//				Android 下载链接
				String androidUrl = PropertyManager.getString(brokerId+"_androidUrl");
				String androidPgName = PropertyManager.getString(brokerId+"_androidPgName");
				openAccountReqResult.setAndroidUrl(androidUrl);
				openAccountReqResult.setAndroidPgName(androidPgName);
//				IOS 下载链接
				String iosUrl = PropertyManager.getString(brokerId+"_iosUrl");
				String urlSchemes = PropertyManager.getString(brokerId+"_urlSchemes");
				openAccountReqResult.setIosUrl(iosUrl);
				openAccountReqResult.setUrlSchemes(urlSchemes);
				
				if (ZXZQFlag.equals(brokerId)) {			
					openAccountReqResult.setAppName("您将使用中信手机客户端进行开户");					
				} else if (ZSZQFlag.equals(brokerId)) {
					String data = "";
					String sign = "";
					Map<String, String> map = openAccountReq.getParam();
					if (!map.isEmpty()) {
						data = map.get("data");
						sign = map.get("sign");
					}
					log.info("open param userId-->" + userId + "<--sign-->"
							+ sign);
					log.info("open param userId-->" + userId + "<--data-->"
							+ data);
					openAccountReqResult.setSign(sign);
					openAccountReqResult.setData(data);
					openAccountReqResult.setAppName("您将使用中山手机客户端进行开户");
				}

				if (openAccountReq != null) {
					openAccountReqResult
							.setOpenUrl(openAccountReq.getOpenUrl());
					openAccountReqResult
							.setChannel(openAccountReq.getChannel());
				}
				return OpenResult.ok().add("data", openAccountReqResult)
						.buildJson();
			} catch (ServiceException e) {
				log.error("kaihu ServiceException--->" + e.getMessage(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			} catch (Exception e) {
				log.error("kaihu Exception--->" + e.getMessage(), e);
				return OpenResult.unknown(e.getMessage()).buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	@Path("/broker/getOpStaus")
	@GET
	@Produces("application/json;charset=utf-8")
	@ChangeAccountId
	public String queryAccountStatusOld(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("brokerId") String brokerId,
			@HeaderParam("accountId") long accountId ) {
		return queryAccountStatusBase(userId, sessionId, brokerId, accountId);
	}

	/**
	 * 根据身份证和真实姓名判断该用户是否有盈利宝账户 依次检验身份证格式 身份证唯一性 身份证和真实姓名是否匹配
	 * 
	 * @param content
	 * @return
	 */
	@Path("/broker/isYLB")
	@POST
	@Produces("application/json;charset=utf-8")
	public String isYLBAccount(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String realName = json.getString("realName");
		String idNumber = json.getString("idNumber");

		if (StringUtils.isEmpty(realName) || StringUtils.isEmpty(idNumber)) {
			return OpenResult.parameterError("参数有误").buildJson();
		}
		// 检验身份证号码格式
		boolean flag = ValidateUtil.isIdNumber(idNumber);
		if (!flag) {
			return OpenResult.serviceError(10103, "身份证号码格式不正确").buildJson();
		}
		try {
			// 验证身份证唯一性
			JSONObject result = personalService.checkUniqueIdnumber(idNumber);
			int retcode = 0;
			String status = "";
			String UID = "";
			YLBStatusResult yLBStatusResult = new YLBStatusResult();
			if (result != null) {
				retcode = result.getIntValue("retcode");
				if (retcode == 0) {
					status = "1";
				} else if (retcode == 10104) {
					JSONObject userInfo = personalService
							.queryUserInfo(idNumber);
					if (userInfo != null) {
						retcode = userInfo.getIntValue("retcode");
						if (retcode == 0) {
							String userId = userInfo.getString("userid");
							if (StringUtils.isNotEmpty(userId)) {
								UserInfo info = userInfoService.queryUserInfo(userId);
								if(info != null){
									UID = info.getUserId();
									int userStatus = info.getStatus();
//									该用户已完善信息  直接登录
									if(userStatus == UserStatus.COMPETE.status){
										status = "-2";
									}else{
										status = "1";
									}
									
								}
							}
						}
					} else {
						return OpenResult.unknown("服务异常").buildJson();
					}

				} else {
					return result.toJSONString();
				}
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}

			// 是否校验身份证和真实姓名是否匹配 1校验 0不校验
			String checkFlag = PropertyManager.getString("checkIdNumber");
			JSONObject checkResult = null;
			if ("1".equals(checkFlag)) {
				checkResult = personalService.validRealNameId(idNumber,
						realName);
			} else {
				checkResult = personalService.validRealNameId(idNumber,
						realName);
			}
			if (checkResult != null) {
				retcode = checkResult.getInteger("retcode");
				if (retcode != 0) {
					return checkResult.toJSONString();
				}
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
			yLBStatusResult.setStatus(status);
			yLBStatusResult.setUID(UID);
			return OpenResult.ok().add("data", yLBStatusResult).buildJson();
		} catch (StockServiceException e) {
			log.error(
					"current user is or not YLB account StockServiceException"
							+ e.getMessage(), e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		} catch (Exception e) {
			log.error(
					"current user is or not YLB account Exception--->"
							+ e.getMessage(), e);
			return OpenResult.unknown(e.getMessage()).buildJson();
		}
	}

}
