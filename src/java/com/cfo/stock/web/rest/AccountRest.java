package com.cfo.stock.web.rest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.base.AccountBase;
import com.cfo.stock.web.rest.config.BrokerAppConfig;
import com.cfo.stock.web.rest.globals.StockGlobals;
import com.cfo.stock.web.rest.logger.Ioc9Loggor;
import com.cfo.stock.web.rest.result.BindInfoResult;
import com.cfo.stock.web.rest.result.BindSendResult;
import com.cfo.stock.web.rest.result.BrokerEx;
import com.cfo.stock.web.rest.result.BrokersResult;
import com.cfo.stock.web.rest.result.ConfigPassWordResult;
import com.cfo.stock.web.rest.result.CurrentPostionResult;
import com.cfo.stock.web.rest.result.ManagerAccountResult;
import com.cfo.stock.web.rest.result.ModelConfigResult;
import com.cfo.stock.web.rest.result.OpenAccountReqResult;
import com.cfo.stock.web.rest.result.UserAccAuthResult;
import com.cfo.stock.web.rest.service.PersonalService;
import com.cfo.stock.web.rest.utils.BusinessUtils;
import com.cfo.stock.web.rest.utils.ConstantVariable;
import com.cfo.stock.web.rest.utils.CountByLogAndMemcacheUtil;
import com.cfo.stock.web.rest.utils.HttpHeaderUtils;
import com.cfo.stock.web.rest.utils.ListUtils;
import com.cfo.stock.web.rest.utils.MapUtils;
import com.cfo.stock.web.rest.utils.ValidateUtil;
import com.cfo.stock.web.rest.vo.AccountVo;
import com.cfo.stock.web.rest.vo.IntentDataVo;
import com.jrj.common.utils.DateUtil;
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
import com.jrj.stocktrade.api.account.vo.UserAccount;
import com.jrj.stocktrade.api.account.vo.UserInfo;
import com.jrj.stocktrade.api.authorization.AuthorizationService;
import com.jrj.stocktrade.api.common.AccUserStatus;
import com.jrj.stocktrade.api.common.AccUserType;
import com.jrj.stocktrade.api.common.AccountAuthStatus;
import com.jrj.stocktrade.api.common.AccountStatus;
import com.jrj.stocktrade.api.common.AuthorizeType;
import com.jrj.stocktrade.api.common.BrokerId;
import com.jrj.stocktrade.api.common.IdType;
import com.jrj.stocktrade.api.common.StockAccountType;
import com.jrj.stocktrade.api.deposit.FundService;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.opstatus.StockAccountStatusService;
import com.jrj.stocktrade.api.opstatus.vo.OpenAccountReq;
import com.jrj.stocktrade.api.opstatus.vo.StockAccountStatus;
import com.jrj.stocktrade.api.pub.BranchQueryService;
import com.jrj.stocktrade.api.rpc.HsRpcContext;
import com.jrj.stocktrade.api.stock.StockAccountQueryService;
import com.jrj.stocktrade.api.stock.vo.StockAccount;

/**
 * 
 * @className：AccountRest
 * @classDescription： 账户相关接口
 * @author：kecheng.Li
 * 
 * @dateTime：2014年4月19日 上午11:54:39
 */
@Path(StockBaseRest.baseuri + "/v2" + "/account")
@Controller
public class AccountRest extends AccountBase {
	
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
	private StockAccountStatusService stockAccountStatusService;

	@Autowired
	private StockAccountQueryService stockAccountQueryService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	protected FundService fundService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private AuthorizationService authorizationService;
	
	@Autowired
	private CountByLogAndMemcacheUtil  countByLogAndMemcacheUtil;
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
			@QueryParam("sessionId") String sessionId,
			@Context HttpHeaders headers) {
			log.info("-->userId:"+userId+"<--sessionId:"+sessionId);
			if(StringUtils.isBlank(userId)||StringUtils.isBlank(sessionId)){
				return OpenResult.parameterError("数据请求失败").buildJson();
			}
		try {
			boolean checkFlag = checkSessionId(userId, sessionId);

			List<Broker> brokers = new ArrayList<Broker>();
			List<BrokerEx> brokerExList = new ArrayList<BrokerEx>();
			BrokersResult brokersResult = new BrokersResult();
			// 根据不同业务区分
			String headerValue = HttpHeaderUtils.getHeaderValue(headers,"productid");
			String businessValue = BusinessUtils.getBusinessByProductId(headerValue);
			String channelIdValue = HttpHeaderUtils.getHeaderValue(headers,"channelId");
			brokers = accountService.queryAllBrokers();
			String appver = HttpHeaderUtils.getHeaderValue(headers, "appver");
			log.info("<--businessValue:" + businessValue+"<--appver:" + appver+"<--channelIdValue:"+channelIdValue);
			List<String> initList = ListUtils.stringToList(ConstantVariable.initBrokerId, ",");
			List<String> txPwdList = ListUtils.stringToList(ConstantVariable.txpwdBroker, ",");
			for (Broker broker : brokers) {
				String brokerId = broker.getBrokerId();
				String brokerName = broker.getBrokerName();
				log.info("brokerId--->"+brokerId);
				boolean openbind = openOrBind(brokerId, userId);
				// 未绑定未开户用户 不用检验session直接通过
				if (checkFlag || openbind) {
					BrokerEx brokerEx = BrokerAppConfig.getBrokerEx(brokerId,
							channelIdValue, appver, businessValue);
					
					if (brokerEx == null){
						continue;
					}
					String mark = ConstantVariable.markUrl + "/" + brokerId + "_i.html";
					String brokerLogo = ConstantVariable.brokerLogoUrl + "/" + brokerId
							+ "_mobile.png";
					brokerEx.setMark(mark);
					brokerEx.setBrokerLogo(brokerLogo);
					brokerEx.setZQTProtocol( ConstantVariable.markUrl+"/ZQT_AGREE.html");
					if(StringUtils.equals(BrokerId.CITIC_SECURITIES, brokerId)){
						brokerEx.setZXZQProtocol(ConstantVariable.markUrl+"/ZXZQ_AGREE.html");
					}
					
//					配置密码
					ConfigPassWordResult result = setPwdType(brokerId, initList, txPwdList);
					result.setInitTxPassword(broker.getTxPwd()==1?"true":"false");
					brokerEx.setConfigPassWordResult(result);
					// 为了显示券商其他信息 在for循环中处理异常
					try {
						BindInfo bindInfo = accountService.getBindInfo(userId,
								brokerId);
						if (bindInfo != null) {
							brokerEx.setSelfBindFlag("7");
						} else {
							brokerEx.setSelfBindFlag("-7");
						}
					} catch (Exception e) {
						log.error("brokerId:"+ brokerId+ "--brokerName:"+ brokerName
										+ "--query available brokerlist for loop  Exception :"
										+ e.getMessage(), e);
						brokerEx.setSelfBindFlag("-7");
					}
					BeanUtils.copyProperties(broker, brokerEx);
					brokerEx.setTradeWay(getTradeWay(brokerId));
					brokerExList.add(brokerEx);
				} else {
					return OpenResult.noAccess("未授权").buildJson();
				}
			}
			// 排序
			orderAvailableBrokerList(brokerExList);
			brokersResult.setBrokerList(brokerExList);
			return OpenResult.ok().add("data", brokersResult).buildJson();

		} catch (ServiceException e) {
			log.error(
					"query available brokerlist ServiceException :"
							+ e.getErrorInfo(), e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
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
		log.info("<--userId:"+userId+"<--sessionId:"+sessionId+"<--brokerId:"+brokerId);
		if(StringUtils.isBlank(userId)||StringUtils.isBlank(sessionId)||StringUtils.isBlank(brokerId)){
			return OpenResult.parameterError("数据请求失败").buildJson();
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

	/**
	 * 获取绑定券商的URL，S，P参数
	 * 
	 * @param userId
	 * @param sessionId
	 * @param brokerId
	 * @return
	 */
	@Path("/bind/urlparam")
	@GET
	@Produces("application/json;charset=utf-8")
	public String getBindingTokenURL(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("brokerId") String brokerId) {
		log.info("<--userId:"+userId+"<--sessionId:"+sessionId+"<--brokerId:"+brokerId);
		if(StringUtils.isBlank(userId)||StringUtils.isBlank(sessionId)||StringUtils.isBlank(brokerId)){
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		
		try {
			boolean openbind = openOrBind(brokerId, userId);
			boolean checkFlag = checkSessionId(userId, sessionId);
			String returnUrl = StockGlobals.BIND_CALLBACK_URL.replaceAll("\\$brokerId", brokerId);
			BindSendResult bindSendResult = new BindSendResult();
			JSONObject json = getUserBaseInfo(userId);
			String idNumber = json.getString("idNumber");
			String realName = json.getString("realName");
			String mobileNo = json.getString("mobileNo");
			String data = "";
			String sign = "";
			String s = ""; //中信 s q
			String q = "";
			String p = "" ; //恒泰 s p
			Map<String, String> map  = new HashMap<String, String>();
			String method = "post";
			String param = "";
			String url = "";
			if (openbind || checkFlag) {
				log.info("#getBindingTokenURL :[userId" + userId+ ",realname:" + realName+ ",idnumber：" + idNumber+"]");
				if(StringUtils.equals("HAITZQ", brokerId)){
					url = ConstantVariable.markUrl+"/HAITZQ_MidPage.html";
					method = "get";
				}else{
					BindReq bindReq = accountService.bindSend(userId, IdType.ID,
							idNumber, realName,returnUrl, brokerId, mobileNo);
					log.info("catch bindReq is--:"+ JSONObject.toJSONString(bindReq));
					if (bindReq != null) {
						if (bindReq.getErrorNo() != 0) {
							if(bindReq.getErrorNo() == -2){
								return OpenResult.serviceError(bindReq.getErrorNo(),"未找到您在该券商的开户记录").buildJson();
							}
							return OpenResult.serviceError(bindReq.getErrorNo(),
									bindReq.getErrorInfo()).buildJson();
						}
						map = bindReq.getParam();
						if (!map.isEmpty()) {
							if (BrokerId.ZSZQ_SECURITIES.equals(brokerId)) {
								data = map.get("data");
								sign = map.get("sign");
								method = "get";
								param=MapUtils.mapToString(map);
							} else if (BrokerId.CITIC_SECURITIES.equals(brokerId)) {
								s = map.get("s");
								q = map.get("q");
								param = "s=" + s + "&q=" + q;
							} else if (BrokerId.CNHT_SECURITIES.equals(brokerId)) {
								s = map.get("s");
								p = map.get("p");
								param = MapUtils.mapToString(map);
							} else {
								param=MapUtils.mapToString(map);
							}
						}
						url = bindReq.getUrl();
					}
				}
				bindSendResult.setSign(sign);
				bindSendResult.setData(data);
				bindSendResult.setS(s);
				bindSendResult.setQ(q);
				bindSendResult.setP(p);
				bindSendResult.setParam(param);
				bindSendResult.setMethod(method);
				bindSendResult.setUrl(url);
				bindSendResult.setCallUrl(returnUrl);
				return OpenResult.ok().add("data", bindSendResult)
						.buildJson();
			} else {
				return OpenResult.noAccess("未授权").buildJson();
			}
		} catch (ServiceException e) {
			log.error("catch bind url ServiceException" + e.getMessage(), e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
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
		log.info("-->userId:"+userId+"<--sessionId:"+sessionId+
				"<--brokerId:"+brokerId+"<--p:"+p+"<--s:"+s+"<--data:"+data+"<--sign:"+sign);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(brokerId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			ModelConfigResult modelConfigResult = setModelConfig(brokerId);
			BindInfoResult bindInfoResult = new BindInfoResult(
					modelConfigResult);
			boolean checkFlag = checkSessionId(userId, sessionId);
			boolean openbind = openOrBind(brokerId, userId);
			if (checkFlag || openbind) {
				if (BrokerId.ZSZQ_SECURITIES.equals(brokerId)) {
					param.put("p", data);
					param.put("s", sign);
				} else if (BrokerId.CNHT_SECURITIES
						.equals(brokerId)) {
					param.put("p", URLDecoder.decode(p, "utf-8"));
					param.put("s", URLDecoder.decode(s, "utf-8"));
				} else {
					param.put("p", p);
					param.put("s", s);
				}
				log.info("param is --->" + JSONObject.toJSON(param));
				BindInfo bindInfo = accountService.bindReceive(userId, brokerId,
						param);
				log.info("submit param is result bindInfo:"
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
			} else {
				return OpenResult.noAccess("未授权").buildJson();
			}
			return OpenResult.ok().add("data", bindInfoResult).buildJson();
		} catch (ServiceException e) {
			log.error("bind broker ServiceException:" + e.getErrorInfo(), e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
		} catch (UnsupportedEncodingException e) {
			log.error("bind broker ServiceException:" + e.getMessage(), e);
			return OpenResult.serviceError(-1, "不支持改编码").buildJson();
		}

	}

	@Path("/broker/open/common")
	@POST
	@Produces("application/json;charset=utf-8")
	public String openStockAccountCommon(String content,
			@Context HttpHeaders headers) {
		if (StringUtils.isBlank(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String brokerId = json.getString("brokerId");
		int accountType = json.getIntValue("accountType");
		log.info("-->userId:"+userId+"<--sessionId:"+sessionId+
				"<--brokerId:"+brokerId+"<--accountType:"+accountType);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(brokerId) || accountType == 0) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}		
		try {
			OpenAccountReqResult openAccountReqResult = new OpenAccountReqResult();
			String appver = HttpHeaderUtils.getHeaderValue(headers, "appver");
			String paltIdValue =HttpHeaderUtils.getHeaderValue(headers,"paltid");
			String systemVersionValue =HttpHeaderUtils.getHeaderValue(headers,"systemVersion");
			boolean checkFlag = checkSessionId(userId, sessionId);
			boolean openbind = openOrBind(brokerId, userId);
			if (checkFlag || openbind) {
				JSONObject userInfo = getUserBaseInfo(userId);
//				移动端 转户功能全部去掉  此处直接获取开户
				StockAccountType type = StockAccountType
						.getStockAccountType(accountType);
				String typeName = "";
				if (accountType == 1) {
					typeName = "开户";
				} else if (accountType == 2) {
					typeName = "转户";
				} else {
					typeName = "开户";
				}
				String realName = userInfo.getString("realName");
				String idNumber = userInfo.getString("idNumber");
				String mobileNo = userInfo.getString("mobileNo");
				log.info("<--realName:" + realName + "<--idnNmber:" + idNumber+ "<--mobileNo:" + mobileNo);
				// 用户开户
				OpenAccountReq openAccountReq = stockAccountStatusService.insert(userId, realName, IdType.ID, idNumber,
								mobileNo, brokerId, type);
				String ZSZQFlag = BrokerId.ZSZQ_SECURITIES;
				String HTZQFlag = BrokerId.CNHT_SECURITIES;
				String ZXZQFlag = BrokerId.CITIC_SECURITIES;
				String ZJZQFlag = BrokerId.ZJZQ_SECURITIES;//中金
				String CCZQFlag = BrokerId.CGWS_SECURITIES;//长城证券
				String PAZQFlag = BrokerId.PAZQ_SECURITIES;//平安证券
				String CJZQFlag = BrokerId.CJZQ_SECURITIES;//长江证券
				
				String brokerName = brokerHelper.getBrokerName(brokerId);

				String androidUrl = PropertyManager.getString(brokerId+ "_androidUrl");
				String androidPgName = PropertyManager.getString(brokerId+ "_androidPgName");
				openAccountReqResult.setAndroidUrl(androidUrl);
				openAccountReqResult.setAndroidPgName(androidPgName);

				String iosUrl = PropertyManager.getString(brokerId + "_iosUrl");
				String urlSchemes = PropertyManager.getString(brokerId+ "_urlSchemes");
				openAccountReqResult.setIosUrl(iosUrl);
				openAccountReqResult.setUrlSchemes(urlSchemes);
				openAccountReqResult.setAppName("您将使用" + brokerName + "手机客户端进行"+ typeName);
				// 根据不同券商设置信息
				Map<String, String> map = openAccountReq.getParam();
				//基本参数设置
				String andriodParam = "";
				String iosParam = "";
				int andriodParamSize = 0;
				int iosParamSize = 0;
				IntentDataVo intentData = new IntentDataVo();
				String data = "";
				String sign = "";
				String DATA = "";
				String andriodDATA = "";
				if (ZSZQFlag.equals(brokerId)) {
					if (!map.isEmpty()) {
						Map<String, String> encodeMap = MapUtils.encodeMap(map);
						data = encodeMap.get("data");
						sign = encodeMap.get("sign");
						DATA = MapUtils.mapToString(encodeMap);
						String ds = JSONObject.toJSONString(encodeMap);
						andriodDATA = URLEncoder.encode(ds, "UTF-8");
						iosParamSize = map.size();
						andriodParamSize = map.size();
					}
					log.info("open param userId:" + userId + "<--encodesign:"+ sign + "<--encodedata:" + data);
					log.info("open param userId:" + userId + "<--map--->"+JSONObject.toJSONString(map));
					String componentName = "com.thinkive.mobile.account.activitys.HomeActivity";
					intentData.setComponentName(componentName);
					openAccountReqResult.setSign(sign);
					openAccountReqResult.setData(data);
					openAccountReqResult.setAndriodParam("DATA=" +andriodDATA);
					openAccountReqResult.setIosParam(DATA);
					int result = compareVersion(appver, "1.1.0");
//					1.1.1版本之后 中山开户变成H5页面
					if(result>0){
						if(openAccountReq != null){
							openAccountReqResult.setIosWeb(openAccountReq.getOpenUrl());
						}
					}else{
						openAccountReqResult.setIosAPP("true");
					}
					
					openAccountReqResult.setAndroidAPP("true");

				} else if (HTZQFlag.equals(brokerId)) {
					if (!map.isEmpty()) {
						andriodParam = MapUtils.mapToString(map);
						iosParam = MapUtils.mapToString(map);
						log.info("brokerId" + brokerId + "<--map"+ andriodParam);
						andriodParamSize = map.size();
						iosParamSize = map.size();
					}
					String uriData = "CRHSJKH://" + "channel=" + "JRJTEST";
					String action = "android.intent.action.VIEW";
					intentData.setUriData(uriData);
					intentData.setAction(action);
					intentData.setComponentName("com.cairh.app.sjkh.MainActivity");
					
					openAccountReqResult.setAndriodParam(andriodParam);
					openAccountReqResult.setAndroidAPP("true");

					openAccountReqResult.setIosParam(iosParam);
					openAccountReqResult.setIosSDK("true");

				} else if (ZJZQFlag.equals(brokerId)) {//中金
					intentData.setComponentName("com.cairh.app.sjkh.MainActivity");
					openAccountReqResult.setAndriodParam("channel=jrjapp");
				} else if (ZXZQFlag.equals(brokerId)) {
//					配置 ios和安卓 2.2.0版本的中信开户，模式和海通的一样
					if(StringUtils.equals(paltIdValue, "IOS")){
						openAccountReqResult.setIosBrowserUrl(openAccountReq.getOpenUrl());
					}else{//Andriod 2.2.0版本采用H5开户 2.2.0之前还是第三方APP下载
						int result = compareVersion(appver, "2.2.0");
						if(result>=0){
							openAccountReqResult.setAndroidBrowserUrl(openAccountReq.getOpenUrl());
						}else{
							openAccountReqResult.setAndroidAPP("true");
						}
					}
				}else if(CCZQFlag.equals(brokerId)){	//长城证券 IOS H5页面开户 Android 暂时不考虑														
					if(openAccountReq != null){
						openAccountReqResult.setIosBrowserUrl(openAccountReq.getOpenUrl());
						openAccountReqResult.setAndroidBrowserUrl(openAccountReq.getOpenUrl());
					}				
				}else if("HAITZQ".equals(brokerId)){
//					海通开户  IOS     2.0.0开户方式为 IosWeb 2.0.1之后为 iosBrowserUrl
//					海通开户 Android  2.0.1之后全部为AndroidBrowserUrl
					if(openAccountReq != null){
						if("IOS".equals(paltIdValue)){
							int result = compareVersion(appver, "2.0.0");
							if(result == 0){
								openAccountReqResult.setIosWeb(openAccountReq.getOpenUrl());
							}else if(result>0){
								openAccountReqResult.setIosBrowserUrl(openAccountReq.getOpenUrl());
							}
						}else{
							openAccountReqResult.setAndroidBrowserUrl(openAccountReq.getOpenUrl());
						}
					}
				}else if(StringUtils.equals(PAZQFlag, brokerId)){ //平安证券
					/*计数功能*/
					intentData.setComponentName("com.thinkive.mobile.account_pa.activitys.HomeActivity");
					String[] URL_ARRAY=openAccountReq.getOpenUrl().split(";");
					String IOS_URL = URL_ARRAY[0];
					String IOS9_URL = URL_ARRAY[1];
					if(systemVersionValue.startsWith("9")){					
						openAccountReqResult.setIosBrowserUrl(IOS9_URL+"?moblie="+mobileNo);
						openAccountReqResult.setOpenUrl(IOS9_URL);
						Ioc9Loggor.info("=========="+","+IOS9_URL+"?moblie="+mobileNo);
						}
					else{
					openAccountReqResult.setIosBrowserUrl(IOS_URL);
					openAccountReqResult.setOpenUrl(IOS_URL);
					}
					/*ios连接和安卓都是第一个url*/
					openAccountReqResult.setAndroidBrowserUrl(IOS_URL);
				}else if(StringUtils.equals(CJZQFlag, brokerId)){ //长江证券
					openAccountReqResult.setIosBrowserUrl(openAccountReq.getOpenUrl());
					openAccountReqResult.setAndroidBrowserUrl(openAccountReq.getOpenUrl());
				}
				if (openAccountReq != null&&!StringUtils.equals(PAZQFlag, brokerId)) {
					openAccountReqResult
							.setOpenUrl(openAccountReq.getOpenUrl());
					openAccountReqResult
							.setChannel(openAccountReq.getChannel());
				}
				// 通用部分
				openAccountReqResult.setIntentData(intentData);
				openAccountReqResult.setIosParamSize(iosParamSize);
				openAccountReqResult.setAndriodParamSize(andriodParamSize);
			} else {
				return OpenResult.noAccess("未授权").buildJson();
			}	
			Ioc9Loggor.info(paltIdValue+","+systemVersionValue+","+brokerId);
			countByLogAndMemcacheUtil.JudgeBrokerId(paltIdValue,systemVersionValue,brokerId);
			return OpenResult.ok().add("data", openAccountReqResult)
					.buildJson();
		} catch (ServiceException e) {
			log.error("kaihu ServiceException--->" + e.getMessage(), e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
		} catch (UnsupportedEncodingException e) {
			log.error("bind broker ServiceException:" + e.getMessage(), e);
			return OpenResult.serviceError(-1, "不支持该编码").buildJson();
		}

	}

	/**
	 * 开户状态查询
	 * 
	 * @param userId
	 * @param sessionId
	 * @param brokerId
	 * @param accountId
	 * @return
	 */
	@Path("/broker/getOpStaus")
	@GET
	@Produces("application/json;charset=utf-8")
	public String queryAccountStatus(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("brokerId") String brokerId,
			@QueryParam("accountId") long accountId) {
		log.info("-->userId:"+userId+"<--sessionId:"+sessionId+
				"<--brokerId:"+brokerId+"<--accountId:"+accountId);		
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		return queryAccountStatusBase(userId, sessionId, brokerId, accountId);
	}

	/*
	 * 绑定好友账户 流程 身份证真实姓名是否一致-->获取好友基本信息--> 否开通证券通--> 是否开通所绑定的券商--> 是否已被绑定-->
	 * 申请绑定
	 */
	@Path("/bind/friend/account")
	@POST
	@Produces("application/json;charset=utf-8")
	public String bindFriendAccount(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String brokerId = json.getString("brokerId");
		String realName = json.getString("realName");
		String idNumber = json.getString("idNumber");
		log.info("-->userId:"+userId+"<--sessionId:"+sessionId+
				"<--brokerId:"+brokerId+"<--realName:"+realName+"<--idNumber:"+idNumber);		
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)
				|| StringUtils.isEmpty(brokerId)
				|| StringUtils.isEmpty(realName)
				|| StringUtils.isEmpty(idNumber)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				int retcode = 0;
				String msg = "";
				// 验证身份证号是否符合格式
				boolean flag = ValidateUtil.isIdNumber(idNumber);
				if (!flag) {
					return OpenResult.serviceError("-1",
							"身份证号只能是18位且末位如为X需大写,请正确填写").buildJson();
				}
				// 验证姓名是否全是中文
				flag = ValidateUtil.isChinese(realName);
				if (!flag) {
					return OpenResult.serviceError("-1", "姓名只能是中文,请正确填写")
							.buildJson();
				}
				// 判断输入身份证号码是否与本人相同
				JSONObject j = getUserBaseInfo(userId);
				if (j != null) {
					String selfIdNo = j.getString("idNumber");
					if (selfIdNo.equals(idNumber)) {
						retcode = -1;
						msg = "请您输入好友身份信息";
						return OpenResult.serviceError(retcode, msg)
								.buildJson();
					}
				}

				// 测试环境不检验身份证真实姓名  灰度和线上判断身份证真实姓名是否一致				
				String isCheck = PropertyManager.getString("check_realname_idnumber").trim();
				if(isCheck.equals("true")){
					j = personalService.validRealNameId(idNumber, realName);
					retcode = j.getIntValue("retcode");
					msg = j.getString("msg");
					if (retcode != 0) {
						return OpenResult.serviceError(retcode, msg).buildJson();
					}
				}			
				// 用真实姓名身份证获取好友基本信息
				JSONObject jsonId = personalService.queryUserIdByIdNumber(
						realName, idNumber);
				String accUserId = jsonId.getString("userId");
				// 判断好友是否开通证券通
				UserInfo userInfo = userInfoService.queryUserInfo(accUserId);
				if (userInfo == null) {
					retcode = -1;
					msg = "该用户未开通证券通";
					return OpenResult.serviceError(retcode, msg).buildJson();
				}
				// 判断好友是否开通所绑定的券商
				UserAccount userAccount = userAccountService.queryAccount(
						accUserId, brokerId);
				if (userAccount == null) {
					retcode = -1;
					msg = "该用户未绑定" + brokerHelper.getBrokerName(brokerId) + "券商";
					return OpenResult.serviceError(retcode, msg).buildJson();
				}
				// 判断改账户是否已被绑定
				if (userAccount.getAuthStatus() == AccountAuthStatus.AUTH
						.getValue()) {
					retcode = -1;
					msg = "该账户已被其他账户绑定";
					return OpenResult.serviceError(retcode, msg).buildJson();
				}
				long accountId = userAccount.getAccountId();
				log.info("-->userId:" + userId + "--accUserId:" + accUserId
						+ "--accountId:" + accountId + "--AuthorizeType:"
						+ AuthorizeType.REQUEST);
				userAuthService.authorize(userId, accUserId, accountId,
						AuthorizeType.REQUEST);
				// retcode = 0;
				// msg = "已提交绑定申请";
				return OpenResult.ok().buildJson();
			} catch (ServiceException e) {
				log.error(
						"bind friend account ServiceException"
								+ e.getErrorInfo(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			} catch (Exception e) {
				log.error("bind friend account Exception" + e.getMessage(), e);
				return OpenResult.serviceError("-1", e.getMessage())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}

	}

	/**
	 * 查询用户所有可交易账户 包括自己的及授权的账户
	 * 
	 * @param userId
	 * @param sessionId
	 * @return
	 */
	@Path("/query/tradable/accounts")
	@GET
	@Produces("application/json;charset=utf-8")
	public String queryTradableccountList(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@Context HttpHeaders headers) {
		log.info("-->userId:"+userId+"<--sessionId:"+sessionId);	
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			List<UserAccAuthResult> list = new ArrayList<UserAccAuthResult>();
			List<UserAccAuth> userAccAuths = new ArrayList<UserAccAuth>();
			List<FundAccount> fundAccountList = new ArrayList<FundAccount>();
			try {
				// 根据不同业务区分
				String headerValue = HttpHeaderUtils.getHeaderValue(headers,"productid");
				String businessValue = BusinessUtils.getBusinessByProductId(headerValue);
				String appver = HttpHeaderUtils.getHeaderValue(headers,"appver");
				log.info("#queryTradableccountList businessValue:"+businessValue+",appver:" + appver);
				userAccAuths = userAuthService.queryAccessAble(userId);
				for (UserAccAuth u : userAccAuths) {
					log.info("for loop brokerId:"+u.getBrokerId());
				}
				String fundId = "********";
				List<String> fiterBrokerList = ListUtils.stringToList(ConstantVariable.strBrokerId, ",");
				List<String> appverFiterBrokerList = ListUtils.stringToList(ConstantVariable.appverStrBrokerId, ",");
				List<String> platBrokerList = ListUtils.stringToList(PropertyManager.getString("plat_brokerId_list"), ",");	
//				先区分业务 再区分版本
				if(compareVersion(appver, "2.1.0")>=0){
					userAccAuths = filterUserAccAuthListByDemand(businessValue, appver, appverFiterBrokerList, userAccAuths);
				}else{
					userAccAuths = filterUserAccAuthListByDemand(businessValue, appver, fiterBrokerList, userAccAuths);
				}
//				切换账户列表 用户通过ITN绑定的后来下线了 不能切换到该账户 要过滤掉 比如 长江证券 平安证券
				userAccAuths = filterUserAccAuthListByDemand(businessValue, appver, ListUtils.stringToList(ConstantVariable.FILTER_TRADEABLE_BROKERS,","), userAccAuths);
//				切换账户去掉中金证券
				userAccAuths  = filterUserAccAuthListByPlat("", platBrokerList, userAccAuths);
				
				for (UserAccAuth u : userAccAuths) {
					Date updateTime = u.getUtime();
					Date createTime = u.getCtime();
					UserAccAuthResult result = new UserAccAuthResult(
							u.getUserId(), u.getAccUserId(), u.getAccountId(),
							u.getStatus(), u.getType(), u.getSort());
					result.setUtime(DateUtil.format(updateTime, "yyyy年MM月dd日"));
					result.setCtime(DateUtil.format(createTime, "yyyy年MM月dd日"));
					result.setBrokerId(u.getBrokerId());
					// https://zqttest.jrj.com.cn/stock/brokerImages/CCZQ_mobile.png
					result.setBrokerName(u.getBrokerName());
					result.setBrokerLogo(ConstantVariable.brokerLogoUrl + "/" + u.getBrokerId()
							+ "_mobile.png");
					String accUserId = u.getAccUserId();
					JSONObject j = getUserBaseInfo(accUserId);
					if (j != null) {
						String realName = j.getString("realName");
						result.setRealName(realName);
					}
					Broker broker = getBroker(u.getBrokerId());
					result.setBrokerType(broker==null?"":broker.getBrokerType());
					result.setTradeWay(getTradeWay(u.getBrokerId()));
					log.info("UserAccAuth--->" + JSONObject.toJSONString(u));
					// 取资金账号时各个券商相互独立
					try {
						fundAccountList = accountQueryService.fundAccountQuery(
								userId, u.getAccountId());
						if (CollectionUtils.isNotEmpty(fundAccountList)) {
							FundAccount fundAccount = getFundAccount(
									fundAccountList, userId, u.getAccountId());
							if (fundAccount != null) {
								fundId = fundAccount.getFundAccount();
								result.setFundId(fundId);
							}
						}
					} catch (ServiceException e) {
						log.error("for loop query tradable ServiceException"
								+ e.getErrorInfo(), e);
						result.setFundId(fundId);
						list.add(result);
						continue;
					} catch (Exception e) {
						log.error(
								"for loop query tradable Exception:"+ e.getMessage(), e);
						result.setFundId(fundId);
						list.add(result);
						continue;
					}
					list.add(result);

				}
				return OpenResult.ok().add("data", list).buildJson();
			} catch (ServiceException e) {
				log.error("query tradable ServiceException" + e.getErrorInfo(),
						e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	@Path("/manager/all/account")
	@GET
	@Produces("application/json;charset=utf-8")
	public String managerAllAccounts(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@Context HttpHeaders headers) {
		log.info("-->userId:"+userId+"<--sessionId:"+sessionId);
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		try {
			List<StockAccountStatus> stockAccountStatusList = new ArrayList<StockAccountStatus>();
			List<UserAccAuth> userAccAuthList = userAuthService
					.queryUserAuth(userId);
			List<ManagerAccountResult> list = new ArrayList<ManagerAccountResult>();
			List<FundAccount> fundAccountList = new ArrayList<FundAccount>();
			JSONObject j = getUserBaseInfo(userId);
			String realName = j.getString("realName");
			String fundId = "************";
			stockAccountStatusList = stockAccountStatusService
					.queryAllStockOpenStatusByUserId(userId);
			// 根据不同业务区分
			String headerValue =HttpHeaderUtils.getHeaderValue(headers,"productid");
		    String businessValue =BusinessUtils.getBusinessByProductId(headerValue);
			String appver = HttpHeaderUtils.getHeaderValue(headers, "appver");
			String paltIdValue =HttpHeaderUtils.getHeaderValue(headers,"paltid");
			List<Broker> availableBrokers = accountService.queryAllBrokers();
			boolean openBindFlag = false;
			for (Broker broker : availableBrokers) {
				openBindFlag = openOrBind(broker.getBrokerId(), userId);
			}
			boolean checkFlag = checkSessionId(userId, sessionId);
			if (checkFlag || openBindFlag) {
				// 过滤券商从配置文件读取
				List<String> fiterBrokerList = ListUtils.stringToList(ConstantVariable.strBrokerId, ",");
				List<String> appverFiterBrokerList = ListUtils.stringToList(ConstantVariable.appverStrBrokerId, ",");
				List<String> platBrokerList = ListUtils.stringToList(PropertyManager.getString("plat_brokerId_list"), ",");			
//				先区分业务 在区分版本
				if(compareVersion(appver, "2.1.0")>=0){
					userAccAuthList = filterUserAccAuthListByDemand(businessValue, appver,appverFiterBrokerList, userAccAuthList);
				}else{
					userAccAuthList = filterUserAccAuthListByDemand(businessValue, appver,fiterBrokerList, userAccAuthList);
				}
//				切换账户列表 用户通过ITN绑定的后来下线了 不能切换到该账户 要过滤掉 比如 长江证券 平安证券
				userAccAuthList = filterUserAccAuthListByDemand(businessValue, appver, ListUtils.stringToList(ConstantVariable.FILTER_TRADEABLE_BROKERS,","), userAccAuthList);
				userAccAuthList = filterUserAccAuthListByPlat("", platBrokerList, userAccAuthList);
				log.info("userAccAuthList--->"+userAccAuthList);
				for (UserAccAuth userAccAuth : userAccAuthList) {
					ManagerAccountResult m = new ManagerAccountResult();
					m.setAccUserId(userAccAuth.getAccUserId());
					m.setAccountId(userAccAuth.getAccountId());
					m.setStatus(userAccAuth.getStatus());
					m.setType(userAccAuth.getType());
					m.setSort(userAccAuth.getSort());
					m.setCtime(DateUtil.format(userAccAuth.getCtime(),
							"yyyy年MM月dd日"));
					m.setUtime(DateUtil.format(userAccAuth.getUtime(),
							"yyyy年MM月dd日"));
					m.setCreateTime(DateUtil.format(userAccAuth.getCtime(),
							"yyyy年MM月dd日"));
					String brokerId = userAccAuth.getBrokerId();
					m.setBrokerId(brokerId);
					m.setBrokerLogo(ConstantVariable.brokerLogoUrl + "/" + brokerId
							+ "_mobile.png");
					m.setBrokerName(brokerHelper.getBrokerName(brokerId));
					String accUserId = userAccAuth.getAccUserId();
					j = getUserBaseInfo(accUserId);
					realName = j.getString("realName");
					m.setRealName(realName);
					m.setTradeWay(getTradeWay(brokerId));
					m.setZQTProtocol( ConstantVariable.markUrl+"/ZQT_AGREE.html");
					if(StringUtils.equals(BrokerId.CITIC_SECURITIES, brokerId)){
						m.setZXZQProtocol(ConstantVariable.markUrl+"/ZXZQ_AGREE.html");
					}
					try {
						if (userAccAuth.getType() == AccUserType.OWNER.type
								|| userAccAuth.getStatus() == AccUserStatus.CONTROLING.status) {
							fundAccountList = accountQueryService
									.fundAccountQuery(userId,userAccAuth.getAccountId());
							if (CollectionUtils.isNotEmpty(fundAccountList)) {
								FundAccount fundAccount = getFundAccount(
										fundAccountList, userId,
										userAccAuth.getAccountId());
								if (fundAccount != null) {
									fundId = fundAccount.getFundAccount();
									m.setFundId(fundId);
								}
							}
						}
					} catch (ServiceException e) {
						log.error("for loop query tradable ServiceException:"
								+ e.getErrorInfo(), e);
						m.setFundId(fundId);
						list.add(m);
						continue;
					} catch (Exception e) {
						log.error(
								"for loop query tradable Exception:"
										+ e.getMessage(), e);
						m.setFundId(fundId);
						list.add(m);
						continue;
					}
					list.add(m);

				}

				stockAccountStatusList = filterStockAccountStatusListByDemand(businessValue, appver, 
						fiterBrokerList, stockAccountStatusList);
				
				stockAccountStatusList =  filterStockAccountStatusListByPlat(paltIdValue,appver,platBrokerList,
						stockAccountStatusList);
				List<String> onlineTradeBrokers = ListUtils.stringToList(ConstantVariable.ONLINE_TRADEABLE_BROKERS, ",");
				for (StockAccountStatus s : stockAccountStatusList) {
					log.info("StockAccountStatus :"+ JSONObject.toJSONString(s));
					// 判断待绑定状态用户是否已被绑定
					if (s.getAccountStatus() == AccountStatus.TRADEABLE) {
						// 交易未下线 保留授权记录表中的记录 过滤开户状态表中的记录						
						if(onlineTradeBrokers.contains(s.getBrokerId())){
							// 如果已被绑定 列表中 过滤掉待绑定记录
							if (isBindForOpenUser(s, userAccAuthList)) {
								continue;
							}
						}
					}
					//券商通过ITN已绑定 又点击改券商开户 过滤掉该券商该条开户记录
					if(filterBindedByITN(s, userAccAuthList)){
						continue;
					}
					
					ManagerAccountResult m = new ManagerAccountResult();
					m.setRealName(s.getRealName());
					m.setIdNo(s.getIdNo());
					m.setAccountStatus(s.getAccountStatus().getValue());
					String brokerId = s.getBrokerId();
					m.setBrokerLogo(ConstantVariable.brokerLogoUrl + "/" + brokerId
							+ "_mobile.png");
					m.setBrokerName(brokerHelper.getBrokerName(brokerId));
					m.setCreateTime(DateUtil.format(s.getCreateTime(),
							"yyyy年MM月dd日"));
					m.setCtime(DateUtil.format(s.getCreateTime(), "yyyy年MM月dd日"));
					m.setOpenTime(DateUtil.format(s.getOpenTime(),
							"yyyy年MM月dd日"));
					m.setTradeableTime(DateUtil.format(s.getTradeableTime(),
							"yyyy年MM月dd日"));
					m.setFundId(s.getFundId());
					m.setAccountType(s.getType().type);
					m.setSzStockAccount(s.getSzStockAccount());
					m.setShStockAccount(s.getShStockAccount());
					m.setCompleteTime(DateUtil.format(s.getCreateTime(),
							"yyyy年MM月dd日"));
					m.setSzStackAccountStatus(s.getSzStackAccountStatus());
					m.setShStackAccountStatus(s.getSzStackAccountStatus());
					m.setLastFailTime(DateUtil.format(s.getLastFailTime(),
							"yyyy年MM月dd日"));
					m.setBindButtonFlag(getBindButtonFlag(brokerId));
					m.setTradeWay(getTradeWay(brokerId));
					m.setZQTProtocol( ConstantVariable.markUrl+"/ZQT_AGREE.html");
					if(StringUtils.equals(BrokerId.CITIC_SECURITIES, brokerId)){
						m.setZXZQProtocol(ConstantVariable.markUrl+"/ZXZQ_AGREE.html");
					}
//					对brokerId做特殊处理  如果tradeWay是1 brokerId必须包含ITN字符
					/*if(StringUtils.equals("1", getTradeWay(brokerId))){
						if(!StringUtils.contains(brokerId, "ITN")){
							brokerId = "ITN_".concat(brokerId);
						}
					}*/
					m.setBrokerId(brokerId);
					list.add(m);
				}

				return OpenResult.ok().add("data", list).buildJson();

			} else {
				return OpenResult.noAccess("未授权").buildJson();
			}
		} catch (ServiceException e) {
			log.error("manager account ServiceException" + e.getErrorInfo(), e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
		}

	}

	/**
	 * @param userId
	 * @param sessionId
	 * @param brokerId
	 * @param accountId
	 * @return
	 */
	@Path("/query/single/detail")
	@GET
	@Produces("application/json;charset=utf-8")
	public String getSingleAccountDetail(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("brokerId") String brokerId,
			@QueryParam("accountId") long accountId,
			@Context HttpHeaders headers) {
		log.info("-->userId:"+userId+"<--sessionId:"+sessionId+"<--brokerId:"+brokerId+"<--accountId:"+accountId);
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		try {
			ManagerAccountResult m = new ManagerAccountResult();
			StockAccountStatus s = null;
			String shStockAccount = "";
			String szStockAccount = "";
			UserAccAuth userAccAuth = null;
			String paltIdValue =HttpHeaderUtils.getHeaderValue(headers,"paltid");
			String appver = HttpHeaderUtils.getHeaderValue(headers, "appver");
			JSONObject j = getUserBaseInfo(userId);
			String realName = j.getString("realName");
			String idNo = j.getString("idNumber");
			boolean openbind = false;
			// 查询开户记录
			if (StringUtils.isNotBlank(brokerId)) {
				s = stockAccountStatusService.queryStockOpenStatus(userId,
						brokerId);
			//	未开户未绑定 判断			
				openbind = openOrBind(brokerId, userId);
			}
			userAccAuth = userAuthService.queryUserAuth(userId, accountId);			
			boolean checkFlag = checkSessionId(userId, sessionId);
			if (checkFlag||openbind) {
				if (s != null) {
					m.setRealName(s.getRealName());
					m.setIdNo(s.getIdNo());
					m.setAccountStatus(s.getAccountStatus().getValue());
					m.setCtime(DateUtil.format(s.getCreateTime(), "yyyy年MM月dd日"));
					m.setCreateTime(DateUtil.format(s.getCreateTime(),"yyyy年MM月dd日"));
					m.setOpenTime(DateUtil.format(s.getOpenTime(),"yyyy年MM月dd日"));
					m.setTradeableTime(DateUtil.format(s.getTradeableTime(),"yyyy年MM月dd日"));
					m.setFundId(s.getFundId());
					m.setAccountType(s.getType().type);
					shStockAccount = s.getShStockAccount();
					szStockAccount = s.getSzStockAccount();
					m.setShStockAccount(s.getShStockAccount());
					m.setCompleteTime(DateUtil.format(s.getCreateTime(),"yyyy年MM月dd日"));
					m.setSzStackAccountStatus(s.getSzStackAccountStatus());
					m.setShStackAccountStatus(s.getSzStackAccountStatus());
					m.setLastFailTime(DateUtil.format(s.getLastFailTime(),"yyyy年MM月dd日"));
				}
				if (userAccAuth != null) {
					if (StringUtils.isEmpty(brokerId)) {
						brokerId = userAccAuth.getBrokerId();
					}
					m.setAccUserId(userAccAuth.getAccUserId());
					m.setAccountId(userAccAuth.getAccountId());
					m.setStatus(userAccAuth.getStatus());
					m.setType(userAccAuth.getType());
					m.setSort(userAccAuth.getSort());
					m.setCtime(DateUtil.format(userAccAuth.getCtime(),"yyyy年MM月dd日"));
					m.setUtime(DateUtil.format(userAccAuth.getUtime(),"yyyy年MM月dd日"));
					// 被绑定人/好友的姓名
					j = getUserBaseInfo(userAccAuth.getRelationUserId());
					String friendName = j.getString("realName");
					if (StringUtils.isNotEmpty(friendName)) {
						m.setFriendName(friendName);
					} else {
						m.setFriendName("--");
					}
					// 获取账户持有人的姓名
					j = getUserBaseInfo(userAccAuth.getAccUserId());
					realName = j.getString("realName");
					m.setRealName(realName);
					try {
						if (userAccAuth.getType() == AccUserType.OWNER.type
								|| userAccAuth.getStatus() == AccUserStatus.CONTROLING.status) {

							List<StockAccount> stockList = stockAccountQueryService
									.clientStockAccountQuery(userId, accountId);
							// 沪深股东账号
							Map<String, String> map = getStockAccount(stockList);
							if (!map.isEmpty()) {
								shStockAccount = map.get("shStockAccount");
								szStockAccount = map.get("szStockAccount");
							}
							// 资金账号 营业部
							AccountVo accountVo = getFundStockAccount(userId,
									accountId, idNo);
							if (accountVo != null) {
								m.setFundId(accountVo.getFundAccount());
								m.setBranchName(accountVo.getBranchName());
							}
						}
					} catch (ServiceException e) {
						log.error(
								"query single detail ServiceException:"
										+ e.getErrorInfo(), e);
					} catch (Exception e) {
						log.error(
								"query single detail  Exception:"
										+ e.getMessage(), e);
					}
				}
				if (StringUtils.isEmpty(szStockAccount)) {
					szStockAccount = "********";
				}
				if (StringUtils.isEmpty(shStockAccount)) {
					shStockAccount = "********";
				}
				m.setBrokerId(brokerId);
				m.setBrokerLogo(ConstantVariable.brokerLogoUrl + "/" + brokerId + "_mobile.png");
				m.setBrokerName(brokerHelper.getBrokerName(brokerId));
				m.setShStockAccount(shStockAccount);
				m.setSzStockAccount(szStockAccount);
				String openTransferFlag = getOpenTransferFlag(paltIdValue, brokerId,appver);
				m.setOpen(openTransferFlag);
				m.setTransfer(openTransferFlag);
				m.setTradeWay(getTradeWay(brokerId));
				m.setZQTProtocol( ConstantVariable.markUrl+"/ZQT_AGREE.html");
				if(StringUtils.equals(BrokerId.CITIC_SECURITIES, brokerId)){
					m.setZXZQProtocol(ConstantVariable.markUrl+"/ZXZQ_AGREE.html");
				}
			} else {
				return OpenResult.noAccess("未授权").buildJson();
			}
			return OpenResult.ok().add("data", m).buildJson();
		} catch (ServiceException e) {
			log.error(
					"query single detail ServiceException" + e.getErrorInfo(),
					e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
		} 

	}

	/** 当前账户页账户信息 总资产 
	 * @param userId
	 * @param sessionId
	 * @param accountId
	 * @return
	 */
	@Path("/current/postion")
	@GET
	@Produces("application/json;charset=utf-8")
	public String queryCurrentPostion(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId") long accountId) {
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->accountId:"+accountId);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				CurrentPostionResult res = new CurrentPostionResult();
				if (accountId != 0L) {					
					res= setPostionAsset(userId, accountId);					
					UserAccAuth userAccAuth = userAuthService.queryUserAuth(
							userId, accountId);				
					res = setPostionBaseInfo(res, userAccAuth);
				}
				return OpenResult.ok().add("data", res).buildJson();
			} catch (ServiceException e) {
				log.error(
						"query user current position ServiceException"
								+ e.getErrorInfo(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getMessage()).buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	/** 当前账户页 账户基本信息
	 * @param userId
	 * @param sessionId
	 * @param accountId
	 * @return
	 */
	@Path("/current/postion/baseInfo")
	@GET
	@Produces("application/json;charset=utf-8")
	public String queryCurrentPostionBaseInfo(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId") long accountId) {
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->accountId:"+accountId);	
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				CurrentPostionResult res = new CurrentPostionResult();
					UserAccAuth userAccAuth = userAuthService.queryUserAuth(
							userId, accountId);
					res = setPostionBaseInfo(res, userAccAuth);			
				return OpenResult.ok().add("data", res).buildJson();
			} catch (ServiceException e) {
				log.error(
						"query user current position ServiceException"
								+ e.getErrorInfo(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	/**
	 * @param userId
	 * @param sessionId
	 * @param accountId
	 * @return
	 */
	@Path("/current/postion/asset")
	@GET
	@Produces("application/json;charset=utf-8")
	public String queryCurrentPostionAsset(@QueryParam("userId") String userId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("accountId") long accountId) {
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->accountId:"+accountId);	
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				CurrentPostionResult res = new CurrentPostionResult();
				if (accountId != 0L) {
					res = setPostionAsset(userId, accountId);
				}
				return OpenResult.ok().add("data", res).buildJson();
			} catch (ServiceException e) {
				log.error(
						"query user current position ServiceException"
								+ e.getErrorInfo(), e);
				return OpenResult.serviceError(e.getErrorNo(), e.getMessage())
						.buildJson();
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
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->brokerId:"+brokerId+"-->accountType:"+accountType);
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)
				|| StringUtils.isBlank(brokerId) || accountType == 0) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				JSONObject userInfo = getUserBaseInfo(userId);
				OpenAccountReqResult openAccountReqResult = new OpenAccountReqResult();

				StockAccountType type = StockAccountType
						.getStockAccountType(accountType);
				String typeName = "";
				if (accountType == 1) {
					typeName = "开户";
				} else if (accountType == 2) {
					typeName = "转户";
				} else {
					typeName = "开户";
				}
				String realName = userInfo.getString("realName");
				String idNumber = userInfo.getString("idNumber");
				String mobileNo = userInfo.getString("mobileNo");
				// 用户开户
				OpenAccountReq openAccountReq = stockAccountStatusService
						.insert(userId, realName, IdType.ID, idNumber,
								mobileNo, brokerId, type);

				String ZXZQFlag = BrokerId.CITIC_SECURITIES;
				String ZSZQFlag = BrokerId.ZSZQ_SECURITIES;
				String CCZQFlag = BrokerId.CGWS_SECURITIES;

				String androidUrl = PropertyManager.getString(brokerId
						+ "_androidUrl");
				String androidPgName = PropertyManager.getString(brokerId
						+ "_androidPgName");
				openAccountReqResult.setAndroidUrl(androidUrl);
				openAccountReqResult.setAndroidPgName(androidPgName);

				String iosUrl = PropertyManager.getString(brokerId + "_iosUrl");
				String urlSchemes = PropertyManager.getString(brokerId
						+ "_urlSchemes");
				openAccountReqResult.setIosUrl(iosUrl);
				openAccountReqResult.setUrlSchemes(urlSchemes);
				// 根据不同券商设置信息
				if (ZXZQFlag.equals(brokerId)) {
					openAccountReqResult.setAppName("您将使用中信手机客户端进行" + typeName);
				} else if (ZSZQFlag.equals(brokerId)) {
					String data = "";
					String sign = "";
					Map<String, String> map = openAccountReq.getParam();
					if (!map.isEmpty()) {
						data = map.get("data");
						sign = map.get("sign");
					}
					openAccountReqResult.setSign(sign);
					openAccountReqResult.setData(data);

					openAccountReqResult.setAppName("您将使用中山手机客户端进行" + typeName);
				} else if (CCZQFlag.equals(brokerId)) {

					openAccountReqResult.setAppName("您将使用长城手机客户端进行" + typeName);
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
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	@Path("initialize/pwd")
	@POST
	@Produces("application/json;charset=utf-8")
	public String initBrokerPwd(String content,@Context HttpHeaders headers) {
		if (StringUtils.isBlank(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		long accountId = json.getLongValue("accountId");
		String password = json.getString("password");
		String txPassword = json.getString("txPassword");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->accountId:"+accountId+
				"-->password:"+password==null?"password为空":"password不为空"+
				"-->txPassword:"+txPassword==null?"txPassword为空":"txPassword不为空");
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)
				|| StringUtils.isEmpty(password) || accountId == 0L) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		boolean fixUserToken = false;
		if (checkSessionId(userId, sessionId)) {
			try {
				password = StringUtils.trim(password);
				String appver = HttpHeaderUtils.getHeaderValue(headers, "appver");
				String paltid = HttpHeaderUtils.getHeaderValue(headers, "paltid");
				int length = password.length();
				if(StringUtils.equalsIgnoreCase(paltid, "IOS")
				   &&StringUtils.equalsIgnoreCase(appver,"2.0.1")
				   &&length==6){
				 fixUserToken = true;
				 HsRpcContext.putValue("fixUserToken", fixUserToken+"");
				}
				if(StringUtils.isEmpty(txPassword)){
					authorizationService.authorization(userId, accountId, password);
				}else{
					txPassword = StringUtils.trim(txPassword);
					authorizationService.authorization(userId, accountId, password, txPassword);
				}			
				return OpenResult.ok().buildJson();
			} catch (ServiceException e) {
				log.error("initBrokerPwd ServiceException--->" + e.getMessage(),e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
}