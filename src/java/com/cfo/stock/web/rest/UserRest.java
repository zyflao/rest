package com.cfo.stock.web.rest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.captcha.processor.ImageCaptchaValidator;
import com.cfo.stock.web.rest.common.InfoMasker;
import com.cfo.stock.web.rest.exception.StockRestException;
import com.cfo.stock.web.rest.exception.StockServiceException;
import com.cfo.stock.web.rest.result.NoPwdResult;
import com.cfo.stock.web.rest.result.PassPortLoginResult;
import com.cfo.stock.web.rest.service.LoginOutService;
import com.cfo.stock.web.rest.service.PersonalService;
import com.cfo.stock.web.rest.service.RegistService;
import com.cfo.stock.web.rest.utils.BusinessUtils;
import com.cfo.stock.web.rest.utils.ConstantVariable;
import com.cfo.stock.web.rest.utils.HttpHeaderUtils;
import com.cfo.stock.web.rest.utils.ListUtils;
import com.cfo.stock.web.rest.utils.ValidateUtil;
import com.cfo.stock.web.rest.vo.PassportLoginVo;
import com.jrj.common.utils.PropertyManager;
import com.jrj.stocktrade.api.account.AccountService;
import com.jrj.stocktrade.api.account.UserAuthService;
import com.jrj.stocktrade.api.account.UserInfoService;
import com.jrj.stocktrade.api.account.vo.BindInfo;
import com.jrj.stocktrade.api.account.vo.Broker;
import com.jrj.stocktrade.api.account.vo.UserAccAuth;
import com.jrj.stocktrade.api.account.vo.UserInfo;
import com.jrj.stocktrade.api.common.AccUserType;
import com.jrj.stocktrade.api.common.MarketType;
import com.jrj.stocktrade.api.common.UserStatus;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.opstatus.StockAccountStatusService;
import com.jrj.stocktrade.api.opstatus.vo.StockAccountStatus;

/**
 * 
 * 类名称：UserRest 类描述： 用户相关rest接口 创建人：kecheng.Li
 * 
 * 创建时间：2014年4月19日 上午11:50:00
 */
@Path(StockBaseRest.baseuri + "/v2" + "/user")
@Controller
public class UserRest extends StockBaseRest {

	@Autowired
	private LoginOutService loginOutService;

	@Autowired
	private RegistService registService;

	@Autowired
	private PersonalService personalService;

	// 券商账户相关接口
	@Autowired
	private AccountService accountService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private StockAccountStatusService stockAccountStatusService;


	/**
	 * 获取手机验证码
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/get/code")
	@POST
	@Produces("application/json;charset=utf-8")
	public String getCode(String content) {
		// 参数是否为空
		if (StringUtils.isEmpty("content")) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String mobileno = json.getString("mobileno");
		log.info("-->mobileno"+mobileno);
		if (StringUtils.isEmpty(mobileno)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		// 验证手机号格式是否正确
		boolean flag = ValidateUtil.isMobile(mobileno);
		if (!flag) {
			return OpenResult.serviceError(10119, "手机号码有误").buildJson();
		}
		
		try {
			// 获取手机验证码
			JSONObject result = registService.getIdentifyingCode(mobileno);
			if (result != null) {
				int retcode = result.getIntValue("retcode");
				String msg = result.getString("msg");
				if (retcode != 0) {
					return OpenResult.serviceError(retcode, msg).buildJson();
				}
			} else {
				return OpenResult.serviceError(-1, "json为空").buildJson();
			}
			
			return OpenResult.ok().buildJson();
		} catch (StockServiceException e) {
			log.error("get mobile validcode" + e.getMessage(), e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		}
	}

	/**
	 * 验证手机验证码
	 * 
	 * @param headers
	 *            头部信息
	 * @param content
	 *            post 请求参数
	 * @return
	 */
	@Path("/valid/code")
	@POST
	@Produces("application/json;charset=utf-8")
	public String validcode(String content) {

		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String mobileno = json.getString("mobileno");
		String codetype = json.getString("codetype");
		String validcode = json.getString("validcode");
		log.info("-->mobileno:"+mobileno+"-->codetype:"+codetype+"-->validcode:"+validcode);
		if (StringUtils.isBlank(mobileno) || StringUtils.isBlank(validcode)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		try {

			JSONObject result = registService.checkIdentifyingCode(mobileno,
					"", validcode);
			if (result != null) {
				int retcode = result.getIntValue("retcode");
				String msg = result.getString("msg");
				if (retcode != 0) {
					return OpenResult.serviceError(retcode, msg).buildJson();
				}
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
			return result.toJSONString();
		} catch (StockServiceException e) {
			log.error("验证手机验证码异常：" + e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		}

	}

	/** 检验session
	 * @param content
	 * @return
	 */
	@Path("/check/session")
	@POST
	@Produces("application/json;charset=utf-8")
	public String checkSessionId(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId);
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		try {
			List<Broker> brokers = new ArrayList<Broker>();
			brokers = accountService.queryAllBrokers();
			for (Broker broker : brokers) {
				String brokerId = broker.getBrokerId();
				boolean openbind = openOrBind(brokerId, userId);
				boolean flag = checkSessionId(userId, sessionId);
				if (flag||openbind) {
					return OpenResult.ok().buildJson();
				} else {
					return OpenResult.noAccess("未授权").buildJson();
				}
			}
			return OpenResult.ok().buildJson();
		} catch (ServiceException e) {
			log.error(
					"checkSessionId Exception ServiceException :"
							+ e.getErrorInfo(), e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
		} 
	}

	@Path("/get/userInfo")
	@POST
	@Produces("application/json;charset=utf-8")
	public String getUserInfo(@Context HttpServletRequest request,
			String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String idNumber = json.getString("idNumber");
		String captcha = json.getString("captcha");
		String uuId = json.getString("uuId");
		log.info("-->idNumber:"+idNumber+"-->captcha:"+captcha+"-->uuId:"+uuId);
		
		if (StringUtils.isEmpty(idNumber) || StringUtils.isEmpty(captcha)
				|| StringUtils.isEmpty(uuId)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		try {
			if (!ValidateUtil.isIdNumber(idNumber)) {
				return OpenResult
						.parameterError(10103, "身份证号有误，请正确填写您的18位身份证号")
						.buildJson();
			}
			if (!ImageCaptchaValidator.validateResponse(uuId, captcha)) {
				return OpenResult.parameterError(10203, "验证码不正确").buildJson();
			}
			JSONObject result = personalService.queryUserInfo(idNumber);
			if (result != null) {
				int retcode = result.getIntValue("retcode");
				if (retcode != 0) {
					return result.toJSONString();
				}
				NoPwdResult pwdResult = new NoPwdResult();
				String mobileNo = result.getString("mobileno");
				mobileNo = InfoMasker.masker(mobileNo, 3, 4, "*", 1);
				pwdResult.setMobileno(mobileNo);

				JSONObject userInfo = new JSONObject();
				userInfo.put("mobileno", result.getString("mobileno"));
				userInfo.put("userid", result.getString("userid"));
				userInfo.put("email", result.getString("email"));
				setMemcacheJSON(idNumber, userInfo);

				return OpenResult.ok().add("data", pwdResult).buildJson();
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}

		} catch (StockRestException e) {
			log.error("找回密码时获取用户信息异常：" + e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		} catch (StockServiceException e) {
			log.error("找回密码时获取用户信息异常：" + e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		}
	}

	/*
	 * 完善用户信息 流程 验证身份证唯一性 身份证真实姓名 手机号码自动带出
	 */
	@Path("/complete/userInfo")
	@POST
	@Produces("application/json;charset=utf-8")
	public String completeUserInfo(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String realName = json.getString("realName");
		String mobileNo = json.getString("mobileNo");
		String idNumber = json.getString("idNumber");
		String code = json.getString("code");
		String inviteCode = json.getString("inviteCode");
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		
		log.info("-->userId:"+userId+"-->sessionId:"+sessionId+"-->realName:"+realName+
				"-->mobileNo:"+mobileNo+"-->idNumber:"+idNumber+"-->code:"+code+
				"-->inviteCode:"+inviteCode+"-->idNumber:"+idNumber);
		
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)
				|| StringUtils.isEmpty(realName)
				|| StringUtils.isEmpty(mobileNo)
				|| StringUtils.isEmpty(idNumber)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		String errorNo = "";
		String errorInfo = "";
		String realNameVerify, idNumberVerify, mobileNoVerify;
		realNameVerify = idNumberVerify = mobileNoVerify = null;

		// 查询用户是否完善过信息
		if (checkSessionId(userId, sessionId)) {
			try {
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
				// 验证身份证唯一性
				JSONObject j = personalService.checkUniqueIdnumber(idNumber);
				int retcode = j.getIntValue("retcode");
				String msg = j.getString("msg");
				if (retcode != 0) {
					return OpenResult.serviceError(retcode,
							"您填写的身份证已注册，请联系客服 400-166-1188").buildJson();
				}
				//测试环境不检验身份证真实姓名  灰度和线上判断身份证真实姓名是否一致	
				String isCheck = PropertyManager.getString("check_realname_idnumber").trim();
				if(isCheck.equals("true")){
					j = personalService.validRealNameId(idNumber, realName);
					retcode = j.getIntValue("retcode");
					msg = j.getString("msg");
					if (retcode != 0) {
						return OpenResult.serviceError(retcode, msg).buildJson();
					}
				}				
				idNumberVerify = idNumber;
				realNameVerify = realName;
				// 如果用户更新手机号码 先验证手机号是否被注册过 再 验证手机号验证码是否匹配
				if (StringUtils.isNotEmpty(code)) {
					// 验证手机号是否被注册过
					j = registService.mobileUnique(mobileNo);
					retcode = j.getIntValue("retcode");
					msg = j.getString("msg");
					if (retcode != 0) {
						if (retcode == 1) {
							return OpenResult.serviceError(retcode, "手机号已注册")
									.buildJson();
						} else {
							return OpenResult.serviceError(retcode, msg)
									.buildJson();
						}
					}
					// 验证手机号验证码是否匹配
					j = registService.checkIdentifyingCode(mobileNo, "", code);
					retcode = j.getIntValue("retcode");
					msg = j.getString("msg");
					if (retcode != 0) {
						return OpenResult.serviceError(retcode, msg)
								.buildJson();
					}
					mobileNoVerify = mobileNo;
				}
//				邀请码
				if(StringUtils.isNotBlank(inviteCode)){
					flag = ValidateUtil.checkInviteCode(inviteCode);
					if(!flag){
						return OpenResult.serviceError("-1", OpenResult.VERIFY_INVATITAION_CODE).buildJson();
					}
					j = personalService.verifyInvitationCode(userId, inviteCode);
					retcode = j.getIntValue("retcode");
					msg = j.getString("msg");
					if (retcode != 0) {					
						return OpenResult.serviceError(retcode, msg).buildJson();
					}
				}
				// 将身份证真实姓名更新到通行证
				j = personalService.updateRealNameIDNumber(userId,
						idNumberVerify, realNameVerify, mobileNoVerify);
				retcode = j.getIntValue("retcode");
				msg = j.getString("msg");
				if (retcode != 0) {
					return OpenResult.serviceError(retcode, msg).buildJson();
				}
				UserInfo userInfo = userInfoService.queryUserInfo(userId);
				UserInfo info = null;
				if (userInfo == null) {
					info = userInfoService.createUserInfo(userId,
							UserStatus.COMPETE);
					if (info == null) {
						OpenResult.serviceError("-1", "完善信息失败").buildJson();
					}
				} else {
					if (userInfo.getStatus() == UserStatus.INCOMPLETE.status) {
						info = userInfoService.updateUserInfo(userId,
								UserStatus.COMPETE);
						if (info == null) {
							OpenResult.serviceError("-1", "完善信息失败").buildJson();
						}
					}
					if (userInfo.getStatus() == UserStatus.COMPETE.status) {
						errorNo = "-1";
						errorInfo = "您已完善信息";
						return OpenResult.serviceError(errorNo, errorInfo)
								.buildJson();
					}
				}
				return OpenResult.ok().buildJson();

			} catch (ServiceException e) {
				errorNo = e.getErrorNo();
				errorInfo = e.getErrorInfo();
				log.error(
						"complete userInfo ServiceException" + e.getErrorInfo(),
						e);
				return OpenResult.serviceError(errorNo, errorInfo).buildJson();
			} catch (Exception e) {
				errorNo = "-1";
				errorInfo = "完善信息失败";
				log.error("complete userInfo Exception" + e.getMessage(), e);
				return OpenResult.serviceError(errorNo, errorInfo).buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	/***
	 * 邀请码
	 * @author: jianchao.zhao
	 * @date: 2015年1月27日 上午9:12:26
	 * @mail: jianchao.zhao@jrj.com.cn
	 * @param content
	 * @return
	 */
	@Path("/complete/userInfoInviteCode")
	@POST
	@Produces("application/json;charset=utf-8")
	public String userInfoInviteCode(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String inviteCode = json.getString("inviteCode");
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		log.info("<----userId:"+userId+"<----sessionId:"+sessionId+"<----inviteCode:"+inviteCode);		
		boolean flag=false;
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(inviteCode)) {
			return OpenResult.parameterError("参数有误").buildJson();
		}
		String yqcode="2";
		try {
			UserInfo userInfo = userInfoService.queryUserInfo(userId);
			if(userInfo!=null){
				yqcode = userInfo.getYqcode();
			}else{
				return OpenResult.serviceError("-1", "未完成用户信息").buildJson();
			}
			if("0".equals(yqcode) || "1".equals(yqcode)){
				List<UserAccAuth> accauthList = userAuthService.queryUserAuth(userId,AccUserType.OWNER);
				if(accauthList.size()>0){
					yqcode = "2";
					return OpenResult.serviceError("-1", "已绑定资金账户").buildJson();
				}
			}
		} catch (ServiceException e) {
			log.error("查询是否需要验证码，失败" + e.getMessage(), e);
			return OpenResult.serviceError("-1", "查询是否需要验证码失败").buildJson();
		}
		if("0".equals(yqcode) || "1".equals(yqcode)){
			try {
//				flag = ValidateUtil.verifyInviteCode(inviteCode);
//				if(!flag){
//					return OpenResult.serviceError("-1", OpenResult.VERIFY_INVATITAION_CODE).buildJson();
//				}
				JSONObject j = personalService.verifyInvitationCode(userId, inviteCode);
				int retcode = j.getIntValue("retcode");
				String msg = j.getString("msg");
				if (retcode != 0) {					
					return OpenResult.serviceError(retcode, msg).buildJson();
				}
				userInfoService.updateUserInfo(userId, MarketType.getMarketType(Integer.parseInt("1")), UserStatus.COMPETE, "2");
			} catch (NumberFormatException e) {
				log.error("complete userInfoInviteCode NumberFormatException" + e.getMessage(), e);
				return OpenResult.serviceError("-1", "更新标识失败").buildJson();
			} catch (ServiceException e) {
				log.error("complete userInfoInviteCode ServiceException" + e.getMessage(), e);
				return OpenResult.serviceError("-1", "更新标识失败").buildJson();
			}
			return OpenResult.ok().buildJson();
		}else{
			return OpenResult.serviceError("-1", "已输入邀请码").buildJson();
		}
		
	}
	/**
	 * 通行证登录 登陆结果区分 爱投顾 炒股必备
	 * 
	 * @param content
	 * @return
	 */
	@Path("/passport/login")
	@POST
	@Produces("application/json;charset=utf-8")
	public String passportLogin(String content, @Context HttpHeaders headers) {
		log.info("passport login content-->" + content);
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		// passportId loginToken
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("passportId");
		// String loginToken = json.getString("loginToken");
		String accessToken = json.getString("accessToken");
		log.info("<----userId:"+userId+"<----accessToken:"+accessToken);
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(accessToken)) {
			return OpenResult.parameterError("数据请求失败").buildJson();
		}
		try {
			String paltid = HttpHeaderUtils.getHeaderValue(headers, "paltid");
			// 校验AccessToken
			json = personalService.checkAccessToken(userId, accessToken);
			int retcode = json.getIntValue("retcode");
			String msg = json.getString("msg");
			/*if (retcode != 0) {				
				if (retcode == 1) {
//					Android 返回1004 
					if(paltid.equalsIgnoreCase("android")){
						return OpenResult.serviceError(10004, msg).buildJson();
//					IOS 返回-401
					}else{
						return OpenResult.serviceError(-401, msg).buildJson();
					}					
				}
				return OpenResult.serviceError(retcode, msg).buildJson();
			}*/
			long accountId = 0L;
			String brokerId = "";
			BindInfo bindInfo = null;
			PassportLoginVo loginVo = new PassportLoginVo();
			PassPortLoginResult re = new PassPortLoginResult();
			String headerValue = HttpHeaderUtils.getHeaderValue(headers,"productid");
			String businessValue = BusinessUtils.getBusinessByProductId(headerValue);
			List<StockAccountStatus> stockAccountStatusList = new ArrayList<StockAccountStatus>();
			List<UserAccAuth> userAccAuthlist = new ArrayList<UserAccAuth>();
			userAccAuthlist = userAuthService.queryAccessAble(userId);
			log.info("userAccAuthlist-->"
					+ JSONObject.toJSONString(userAccAuthlist));
			String appver = HttpHeaderUtils.getHeaderValue(headers, "appver");
			String accuserid = "";
			UserInfo userInfo  = null;
			String brokerType  = "";
			// 过滤券商从配置文件读取
			List<String> fiterBrokerList = ListUtils.stringToList(ConstantVariable.strBrokerId, ",");
			List<String> platBrokerList = ListUtils.stringToList(PropertyManager.getString("plat_brokerId_list"), ",");
			List<String> appverFiterBrokerList = ListUtils.stringToList(ConstantVariable.appverStrBrokerId, ",");
			if (CollectionUtils.isNotEmpty(userAccAuthlist)) {
//				先区分业务 再区分版本号
				if(compareVersion(appver, "2.1.0")>=0){
					userAccAuthlist = filterUserAccAuthListByDemand(businessValue, appver,appverFiterBrokerList, userAccAuthlist);
				}else{
					userAccAuthlist = filterUserAccAuthListByDemand(businessValue, appver,fiterBrokerList, userAccAuthlist);
				}
//				切换账户列表 用户通过ITN绑定的后来下线了 不能切换到该账户 要过滤掉 比如 长江证券 平安证券
				userAccAuthlist = filterUserAccAuthListByDemand(businessValue, appver, ListUtils.stringToList(ConstantVariable.FILTER_TRADEABLE_BROKERS,","), userAccAuthlist);
				userAccAuthlist  = filterUserAccAuthListByPlat("", platBrokerList, userAccAuthlist);
				for (UserAccAuth userAccAuth : userAccAuthlist) {
					boolean isDef = userAccAuth.isDef();
					if (isDef) {
						accountId = userAccAuth.getAccountId();
						brokerId = userAccAuth.getBrokerId();
						accuserid = userAccAuth.getAccUserId();
					}
				}
				
				// 没有默认券商 取第一个
				if (accountId == 0L) {
					if (CollectionUtils.isNotEmpty(userAccAuthlist)) {
						if (userAccAuthlist.size() > 0) {
							UserAccAuth usuth = userAccAuthlist.get(0);
							accuserid = usuth.getAccUserId();
							accountId = usuth.getAccountId();
							brokerId = usuth.getBrokerId();
						}
					}
				}
				if (accountId != 0L) {
					re.setFlag("true");
				} else {
					re.setFlag("false");
				}
			} else {
				userAccAuthlist = userAuthService.queryUserAuth(userId);				
				if(StringUtils.isBlank(appver)){
					appver = "1.0.3";
				}
//				屏蔽券商先区分业务 再区分版本号
				userAccAuthlist = filterUserAccAuthListByDemand(businessValue, appver, 
						fiterBrokerList, userAccAuthlist);
//				切换账户列表 用户通过ITN绑定的后来下线了 不能切换到该账户 要过滤掉 比如 长江证券 平安证券
				userAccAuthlist = filterUserAccAuthListByDemand(businessValue, appver, ListUtils.stringToList(ConstantVariable.FILTER_TRADEABLE_BROKERS,","), userAccAuthlist);
				userAccAuthlist  = filterUserAccAuthListByPlat("", platBrokerList, userAccAuthlist);
				if (CollectionUtils.isNotEmpty(userAccAuthlist)) {
					accountId = userAccAuthlist.get(0).getAccountId();
					brokerId = userAccAuthlist.get(0).getBrokerId();
				}
				re.setFlag("false");
			}
			if (StringUtils.isNotBlank(accuserid)&& StringUtils.isNotBlank(brokerId)) {
				bindInfo = accountService.getBindInfo(accuserid,brokerId);
				Broker broker = getBroker(brokerId);
				if(broker!=null){
					brokerType = broker.getBrokerType();
				}
				
			}
			re.setFundAccount(bindInfo == null ? "" : bindInfo.getFundAccount());
			loginVo.setUserId(userId);
			re.setUserId(userId);
			re.setAccountId(accountId);
			re.setBrokerId(brokerId);
			// 生成sessionId
			String sessionId = generateSessionId(loginVo);

			re.setSessionId(sessionId);
			re.setUserId(userId);
			// 是否需要完善信息
			JSONObject j = getUserBaseInfo(userId);
			String mobileNo = "";
			String realName = "";
			String idNo = "";
			if (j == null) {
				re.setIsCompleted("true");
			} else {
				mobileNo = j.getString("mobileNo");
				realName = j.getString("realName");
				idNo = j.getString("idNumber");

			}
			userInfo = userInfoService.queryUserInfo(userId);
			if (StringUtils.isNotEmpty(mobileNo)
					&& StringUtils.isNotEmpty(realName)
					&& StringUtils.isNotEmpty(idNo)) {
				re.setIsCompleted("false");
//				用户通过其他渠道完善信息  第一次进入证券通先查询该用户在证券通userinfo表是否留存记录 如果没有 插入一条记录 并将邀请码设置为 '0'
				if(userInfo == null){
					UserInfo u = userInfoService.createUserInfo(userId, MarketType.STOCK, UserStatus.COMPETE, "0");
					log.info("u-->"+JSONObject.toJSONString(u));
				}
			} else {
				re.setIsCompleted("true");
			}
			//判断是否需要调起邀请码-zhaojianchao、20150126
			
			if(userInfo==null){
				re.setIsInvitateCode("true");
			}else{
				String yqcode = userInfo.getYqcode();
				if("0".equals(yqcode) || "1".equals(yqcode)){
					List<UserAccAuth> accauthList = userAuthService.queryUserAuth(userId,AccUserType.OWNER);
					if(accauthList.size()>0){
						yqcode = "2";
					}
				}
				if("2".equals(yqcode)){
					re.setIsInvitateCode("false");
				}else{
					re.setIsInvitateCode("true");
				}
			}
			
			//如果注册已经填写邀请码则返回
			JSONObject getInvitationJson = personalService.getAllInvitationRecordByToId(userId);
			String result = getInvitationJson.getString("result");
			String invitateCode = getInvitationJson.getString("invitateCode");
			if("1".equals(result) && StringUtils.isNotBlank(invitateCode)){
				re.setInvitateCode(invitateCode);
				re.setIsInvitateCode("false");
			}
			// 判断是否有本人开户状态
			if (StringUtils.isNotBlank(realName)&& StringUtils.isNotBlank(idNo)) {
				stockAccountStatusList = stockAccountStatusService.queryAllStockOpenStatusByUserId(userId);
			}
			if (CollectionUtils.isNotEmpty(stockAccountStatusList)) {
				re.setIsOpen("true");
			} else {
				re.setIsOpen("false");
			}
			re.setRealName(realName);
			re.setIdNumber(idNo);
			re.setMobileNo(mobileNo);
			re.setServicePhone("4001661188");
			re.setBrokerType(brokerType);
			re.setTradeWay(getTradeWay(brokerId));
			return OpenResult.ok().add("data", re).buildJson();
		} catch (ServiceException e) {
			log.error("passport login ServiceException" + e.getErrorInfo(), e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
		} catch (StockServiceException e) {
			log.error("passport login ServiceException" + e.getRetcode(), e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		} catch (UnsupportedEncodingException e) {
			log.error("passport login ServiceException" + e.getMessage(), e);
			return OpenResult.serviceError(-1, "").buildJson();
		}
	}

	
}