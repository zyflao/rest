package com.cfo.stock.web.rest.deprecated;

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
import com.cfo.stock.web.rest.AccountRest;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.cfo.stock.web.rest.captcha.processor.ImageCaptchaValidator;
import com.cfo.stock.web.rest.common.CodeType;
import com.cfo.stock.web.rest.common.InfoMasker;
import com.cfo.stock.web.rest.common.UserHelp;
import com.cfo.stock.web.rest.exception.StockRestException;
import com.cfo.stock.web.rest.exception.StockServiceException;
import com.cfo.stock.web.rest.result.FullUserResult;
import com.cfo.stock.web.rest.result.LoginResult;
import com.cfo.stock.web.rest.result.MobileCodeResult;
import com.cfo.stock.web.rest.result.NoPwdResult;
import com.cfo.stock.web.rest.result.UserRegInfoResult;
import com.cfo.stock.web.rest.service.LoginOutService;
import com.cfo.stock.web.rest.service.PersonalService;
import com.cfo.stock.web.rest.service.RegistService;
import com.cfo.stock.web.rest.utils.IPUtils;
import com.cfo.stock.web.rest.utils.ValidateUtil;
import com.cfo.stock.web.rest.vo.RealNameIDVo;
import com.jrj.stocktrade.api.account.AccountService;
import com.jrj.stocktrade.api.account.UserInfoService;
import com.jrj.stocktrade.api.account.vo.BindInfo;
import com.jrj.stocktrade.api.account.vo.Broker;
import com.jrj.stocktrade.api.account.vo.UserInfo;
import com.jrj.stocktrade.api.common.UserStatus;
import com.jrj.stocktrade.api.exception.ServiceException;
/**
 * 
 * 类名称：UserRest 类描述： 用户相关rest接口 创建人：kecheng.Li
 * 
 * 创建时间：2014年4月19日 上午11:50:00
 */
@Path(StockBaseRest.baseuri + "/user")
@Controller
public class UserOldRest extends StockBaseRest{

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

	/**
	 * 登陆
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/login")
	@POST
	@Produces("application/json;charset=utf-8")
	public String login(@Context HttpHeaders headers,
			@Context HttpServletRequest request, String content) {
		if (StringUtils.isBlank(content)) {
			OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String loginName = json.getString("loginname");
		String passwd = json.getString("passwd");
		// 在header中新增 调用应用名称标识（以header 参数传输，名字有用户中心统一分配）app必传参数
		// 登录名类型 1：身份证,2：用户名，3：手机，4：邮箱
		// int nametype = json.getIntValue("nametype");
		String ip = IPUtils.getRemoteIpAdress(request);
		String clientinfo = json.getString("clientinfo");
		String cccode = json.getString("cccode");

		if (StringUtils.isBlank(loginName) || StringUtils.isBlank(passwd)) {
			return OpenResult.parameterError("参数不正确").buildJson();
		}
		// 校验登录名是否符合 手机号 用户名 身份证号 邮箱正确格式
		LoginResult loginResult = null;
		JSONObject result = null;
		try {
			// result = loginOutService.userLogin(loginName, passwd);
			result = loginOutService.userLoginParamAll(loginName, passwd, ip,
					clientinfo, cccode);
			if (result != null) {
				int retcode = result.getIntValue("retcode");
				String msg = result.getString("msg");

				if (retcode != 0) {
					return OpenResult.parameterError(retcode, msg).buildJson();
				}
				int failtimes = result.getIntValue("failtimes");
				String userId = result.getString("userid");
				// 输入错误次数大于13次 账户友好提示信息
				if ((failtimes == 0 && StringUtils.isEmpty(userId))) {
					return OpenResult.commonError(OpenResult.NOACCESS_ERROR,
							UserHelp.LOGIN_TIMES_L_13).buildJson();
				} else if (failtimes > 0) {
					if (failtimes < 13) {
						return OpenResult.commonError(
								OpenResult.NOACCESS_ERROR,
								UserHelp.LOGIN_TIMES_L_13).buildJson();
					} else {
						return OpenResult.commonError(
								OpenResult.NOACCESS_ERROR,
								UserHelp.LOGIN_TIMES_13).buildJson();
					}
				} else {
					loginResult = new LoginResult();
					loginResult.setUserid(userId);
					loginResult.setUniquecode(result.getString("uniquecode"));
					loginResult.setCompanyuser(result
							.getIntValue("companyuser"));
					loginResult.setRegtime(result.getString("regtime"));
					loginResult.setUserstatus(result.getIntValue("userstatus"));
					loginResult.setFailtimes(result.getIntValue("failtimes"));
					loginResult.setFrozenremainseconds(result
							.getIntValue("frozenremainseconds"));
					loginResult.setLastsuccesstime(result
							.getString("lastsuccesstime"));

					JSONObject userContrInfo = personalService
							.getUserInfo(userId);
					String mobileNo = null;
					String idnumber = null;
					String realname = null;
					if (userContrInfo != null) {
						retcode = userContrInfo.getIntValue("retcode");
						msg = userContrInfo.getString("msg");
						if (retcode != 0) {
							return OpenResult.parameterError(retcode, msg)
									.buildJson();
						}
						mobileNo = userContrInfo.getJSONObject("user")
								.getString("mobileno");

						idnumber = userContrInfo.getJSONObject("user")
								.getString("idnumber");
						realname = userContrInfo.getJSONObject("user")
								.getString("realname");
					} else {
						return OpenResult.unknown("服务异常").buildJson();
					}

					String sessionId = generateSessionId(loginResult);
					loginResult.setSessionId(sessionId);

					// 将中信证券所需的 mobileno devid 存入session中
					String devId = getDevId(headers);
					JSONObject securitiesInfo = setSecuritiesInfoJson(devId,
							mobileNo);
					boolean securitiesFlag = setSecuritiesInfo(sessionId,
							securitiesInfo.toJSONString());
					if (!securitiesFlag) {
						log.debug("登陆时SecuritiesInfo放入缓存结果--" + securitiesFlag);
					}
					mobileNo = InfoMasker.masker(mobileNo, 3, 4, "*", 1);
					loginResult.setMobileno(mobileNo);

					// 将userId放入缓存中
					boolean sign = setMemcacheUserId(sessionId, userId);
					if (!sign) {
						log.debug("登陆时userId放入缓存结果--" + sign);
					}
					String deafultBroker = "ZXZQ";
					// 是否绑定券商
					List<Broker> brokers = accountService
							.queryBindedBrokers(userId);
					int bindStatus = 0;
					if (CollectionUtils.isEmpty(brokers)) {
						bindStatus = 1;
					} else {
						bindStatus = 2;
					}
					// 资金账号
					String fundAccount = "";
					BindInfo bindInfo = accountService.getBindInfo(userId,
							deafultBroker);
					if (bindInfo != null) {
						fundAccount = bindInfo.getFundAccount();
					}

					// 是否填写 身份证 真实姓名 1未绑定
					int bindId = 0;
					if (StringUtils.isEmpty(idnumber)
							|| StringUtils.isEmpty(realname)) {
						bindId = 1;
					} else {
						bindId = 2;
					}
					loginResult.setBindStatus(bindStatus);
					loginResult.setBindId(bindId);
					loginResult.setFundAccount(fundAccount);
					loginResult.setDeafultBroker(deafultBroker);
					String str = OpenResult.ok().add("data", loginResult)
							.buildJson();
					return str;
				}

			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
		} catch (StockServiceException e) {
			log.error("登录异常：" + e);
			return OpenResult.parameterError(result.getIntValue("retcode"),
					result.getString("msg")).buildJson();
		} catch (ServiceException e) {
			log.error("登录异常：" + e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
		} catch (Exception e) {
			log.error("登录异常：" + e);
			return OpenResult.unknown(e.getMessage()).buildJson();
		}

	}

	/**
	 * 注册
	 * 
	 * @param headers
	 * @param request
	 * @param content
	 * @return
	 */
	@Path("/regist")
	@POST
	@Produces("application/json;charset=utf-8")
	public String mobileRegist(@Context HttpHeaders headers,
			@Context HttpServletRequest request, String content) {
		if (StringUtils.isBlank(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String mobileno = json.getString("mobileno");
		String passwd = json.getString("passwd");
		String validcode = json.getString("validcode");
		// 以上参数是必传参数
		if (StringUtils.isEmpty(mobileno) || StringUtils.isEmpty(passwd)
				|| StringUtils.isEmpty(validcode)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		// 以下参数为非必传参数
		// String clientinfo = json.getString("clientinfo");
		// String ip = IPUtils.getRemoteIpAdress(request);
		// int usedefaulttemplate = 1;
		// String smstemplate = "";
		// String cccode = json.getString("cccode");
		try {
			// 验证手机号格式是否正确
			boolean flag = ValidateUtil.isMobile(mobileno);
			if (!flag) {
				return OpenResult.serviceError(10119, "手机号码有误").buildJson();
			}
			// 检验手机号是否被注册
			JSONObject result = registService.mobileUnique(mobileno);

			if (result != null) {
				if (result.getIntValue("retcode") != 0) {
					return OpenResult.parameterError(
							result.getIntValue("retcode"),
							result.getString("msg")).buildJson();
				}
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}

			// 检验验证码
			JSONObject codeJson = registService.checkIdentifyingCode(mobileno,
					CodeType.REGISTER.type, validcode);

			if (codeJson != null) {
				Integer retcode = codeJson.getInteger("retcode");
				String msg = codeJson.getString("msg");
				if (retcode != 0) {
					return OpenResult.parameterError(retcode, msg).buildJson();
				}
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
			// JSONObject re = registService.mobileRegistParamAll(mobileno,
			// passwd, validcode, ip, clientinfo,cccode);
			JSONObject re = registService.mobileRegist(mobileno, passwd,
					validcode);
			if (re != null) {
				if (re.getInteger("retcode") != 0) {
					return OpenResult.parameterError(re.getIntValue("retcode"),
							re.getString("msg")).buildJson();
				}
				String userId = re.getString("userid");
				LoginResult loginResult = new LoginResult();
				loginResult.setUserid(userId);
				loginResult.setMobileno(mobileno);
				loginResult.setUserstatus(re.getInteger("userstatus"));
				loginResult.setRegtime(re.getString("regtime"));

				String sessionId = generateSessionId(loginResult);
				loginResult.setSessionId(sessionId);

				UserRegInfoResult infoResult = new UserRegInfoResult();
				infoResult.setMobileno(mobileno);
				infoResult.setRegtime(re.getString("regtime"));
				infoResult.setSessionId(sessionId);
				infoResult.setUserid(userId);
				infoResult.setUserstatus(re.getInteger("userstatus"));

				// 将中信证券所需的 mobileno devid 存入session中
				String devId = getDevId(headers);
				JSONObject securitiesInfo = setSecuritiesInfoJson(devId,
						mobileno);
				;
				boolean securitiesFlag = setSecuritiesInfo(sessionId,
						securitiesInfo.toJSONString());
				if (!securitiesFlag) {
					log.debug("注册时SecuritiesInfo放入缓存结果--" + securitiesFlag);
				}

				// 将userId放入缓存中
				boolean sign = setMemcacheUserId(sessionId, userId);
				if (!sign) {
					log.debug("注册时userId放入缓存结果--" + sign);
				}

				return OpenResult.ok().add("data", infoResult).buildJson();
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
		} catch (StockServiceException e) {
			log.error("注册异常：" + e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		} catch (Exception e) {
			log.error("注册异常：" + e);
			return OpenResult.unknown(e.getMessage()).buildJson();
		}
	}

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

		// 手机注册验证码：201；手机找回密码：205，重置密码：253
		String codetype = json.getString("codetype");
		if (StringUtils.isEmpty(mobileno) || StringUtils.isEmpty(codetype)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		// 验证手机号格式是否正确
		boolean flag = ValidateUtil.isMobile(mobileno);
		if (!flag) {
			return OpenResult.serviceError(10119, "手机号码有误").buildJson();
		}
		// 手机号是否已被注册
		JSONObject result = registService.mobileUnique(mobileno);

		if (result != null) {
			if (result.getIntValue("retcode") != 0) {
				return OpenResult.serviceError(result.getIntValue("retcode"),
						result.getString("msg")).buildJson();
			}
		} else {
			return OpenResult.unknown("服务异常").buildJson();
		}
		try {
			// 获取手机验证码
			result = registService.getIdentifyingCode(mobileno);
			if (result != null) {
				int retcode = result.getIntValue("retcode");
				String msg = result.getString("msg");
				if (retcode != 0) {
					return OpenResult.serviceError(retcode, msg).buildJson();
				}
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
			MobileCodeResult mobileCodeResult = new MobileCodeResult();
			Long expiredtime = result.getLong("expiredtime");
			mobileCodeResult.setExpiredtime(expiredtime);
			return OpenResult.ok().add("data", mobileCodeResult).buildJson();
		} catch (StockServiceException e) {
			log.error("注册时获取手机验证码异常：" + e);
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
		if (StringUtils.isBlank(mobileno) || StringUtils.isBlank(codetype)
				|| StringUtils.isBlank(validcode)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		try {

			JSONObject result = registService.checkIdentifyingCode(mobileno,
					codetype, validcode);
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

	/**
	 * 更新用户信息
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/update/userInfo")
	@POST
	@Produces("application/json;charset=utf-8")
	public String updateUserInfo(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String validdate = json.getString("validdate");
		String postcode = json.getString("postcode");
		String regioncode = json.getString("regioncode");
		String address = json.getString("address");
		Integer sex = json.getInteger("sex");
		String description = json.getString("description");
		String reservedinfo = json.getString("reservedinfo");
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		// 校验性别
		if (sex != null) {
			if (sex > 1 || sex < 0) {
				return OpenResult.parameterError("性别参数错误").buildJson();
			}
		}
		if (StringUtils.isNotEmpty(postcode)) {
			// 校验邮政编码
			boolean flag = registService.checkPostCode(postcode);
			if (!flag) {
				return OpenResult.parameterError("请输入正确的邮编").buildJson();
			}
		}
		if (checkSessionId(userId, sessionId)) {
			try {
				JSONObject userRes = registService.updateUserInfo(userId,
						validdate, postcode, regioncode, address, sex,
						description, reservedinfo);
				if (userRes != null) {
					int retcode = userRes.getIntValue("retcode");
					String msg = userRes.getString("msg");
					if (retcode != 0) {
						return OpenResult.parameterError(retcode, msg)
								.buildJson();
					}
					return userRes.toJSONString();
				} else {
					return OpenResult.unknown("服务异常").buildJson();
				}
			} catch (StockServiceException e) {
				return OpenResult.serviceError(e.getRetcode(), e.getMsg())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}

	/**
	 * 验证用户的 真实姓名 身份证号码
	 * 
	 * @param headers
	 * @param content
	 * @return
	 */
	@Path("/update/realname")
	@POST
	@Produces("application/json;charset=utf-8")
	public String updateRealName(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userId");
		String sessionId = json.getString("sessionId");
		String idnumber = json.getString("idnumber");
		String realname = json.getString("realname");
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)
				|| StringUtils.isEmpty(idnumber)
				|| StringUtils.isEmpty(realname)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		int retcode = 0;
		String msg = "";
		JSONObject result = null;
		// 验证身份证号格式
		Boolean flag = ValidateUtil.isIdNumber(idnumber);
		if (!flag) {
			retcode = 10103;
			msg = "身份证号有误，请正确填写您的18位身份证号";
			return OpenResult.serviceError(retcode, msg).buildJson();
		}
		try {
			if (checkSessionId(userId, sessionId)) {
				// 验证身份证唯一性
				result = personalService.checkUniqueIdnumber(idnumber);
				if (result != null) {
					retcode = result.getIntValue("retcode");
					if (retcode != 0) {
						return result.toJSONString();
					}
				} else {
					return OpenResult.unknown("服务异常").buildJson();
				}
				// 验证身份证 真实姓名
				result = personalService.validRealNameId(idnumber, realname);
				if (result != null) {
					retcode = result.getIntValue("retcode");
					if (retcode != 0) {
						return result.toJSONString();
					}
				} else {
					return OpenResult.unknown("服务异常").buildJson();
				}
				// 填写用户的真实姓名 身份证
				result = personalService.updateRealNameIDNumber(userId, idnumber,
						realname,"");
				if (result != null) {
					if (result.getInteger("retcode") != 0) {
						return result.toJSONString();
					}
					RealNameIDVo vo = new RealNameIDVo();
					vo.setCompanyuser(result.getIntValue("companyuser"));
					vo.setUncommonword(result.getIntValue("uncommonword"));
					vo.setIdchecked(result.getIntValue("idchecked"));
					return OpenResult.ok().add("data", vo).buildJson();
				} else {
					return OpenResult.unknown("服务异常").buildJson();
				}
			} else {
				return OpenResult.noAccess("未授权").buildJson();
			}

		} catch (StockServiceException e) {
			log.error("填写用户身份证号码和真实姓名异常：" + e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		}

	}

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
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(sessionId)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		boolean flag = checkSessionId(userId, sessionId);
		if (flag) {
			return OpenResult.ok().buildJson();
		} else {
			return OpenResult.noAccess("未授权").buildJson();
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
		if (StringUtils.isEmpty(idNumber) || StringUtils.isEmpty(captcha)
				|| StringUtils.isEmpty(uuId)) {
			return OpenResult.parameterError("参数错误").buildJson();
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

	@Path("/findpwd/getcode")
	@POST
	@Produces("application/json;charset=utf-8")
	public String getCodeForFindPwd(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String idNumber = json.getString("idNumber");
		if (StringUtils.isEmpty(idNumber)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		try {
			boolean flag = checkIdNumber(idNumber);
			if (log.isDebugEnabled()) {
				log.debug("找回密码自延期" + flag);
			}
			JSONObject result = null;
			String userId = null;
			String mobileno = null;
			json = getUser(idNumber);
			if (json != null) {
				userId = json.getString("userid");
				mobileno = json.getString("mobileno");
			} else {
				JSONObject info = personalService.queryUserInfo(idNumber);
				if (info != null) {
					mobileno = info.getString("mobileno");
					userId = info.getString("userid");
				} else {
					return OpenResult.parameterError("请输入注册身份证号码").buildJson();
				}
			}
			result = personalService.getCodeForfindPasswd(userId, mobileno);
			if (result != null) {
				if (result.getInteger("retcode") != 0) {
					return result.toJSONString();
				}
				MobileCodeResult codeResult = new MobileCodeResult();
				codeResult.setExpiredtime(result.getLong("expiredtime"));
				return OpenResult.ok().add("data", codeResult).buildJson();
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
		} catch (StockServiceException e) {
			log.error("找回密码时获取验证码异常：" + e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		}
	}

	@Path("/findpwd/validcode")
	@POST
	@Produces("application/json;charset=utf-8")
	public String validCodeForFindPwd(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String userId = json.getString("userid");
		String mobileno = json.getString("mobileno");
		String validcode = json.getString("validcode");
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(mobileno)
				|| StringUtils.isEmpty(validcode)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		try {
			JSONObject result = personalService.validCodeForFindPasswd(userId,
					mobileno, validcode);
			if (result != null) {
				if (result.getInteger("retcode") != 0) {
					return result.toJSONString();
				}
				MobileCodeResult codeResult = new MobileCodeResult();
				codeResult.setExpiredtime(result.getLong("expiredtime"));
				return OpenResult.ok().add("data", codeResult).buildJson();
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
		} catch (StockServiceException e) {
			log.error("找回密码时验证验证码异常：" + e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		}
	}

	@Path("/findpwd/modifypwd")
	@POST
	@Produces("application/json;charset=utf-8")
	public String modifyPwd(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String validcode = json.getString("validcode");
		String passwd = json.getString("passwd");
		String idNumber = json.getString("idNumber");
		try {
			// session自动延期
			boolean flag = checkIdNumber(idNumber);
			if (log.isDebugEnabled()) {
				log.debug("找回密码自延期" + flag);
			}
			JSONObject result = null;
			// 获取存放在json中的用户信息
			json = getUser(idNumber);
			String userId = null;
			String mobileno = null;
			if (json != null) {
				userId = json.getString("userid");
				mobileno = json.getString("mobileno");
			} else {
				JSONObject info = personalService.queryUserInfo(idNumber);
				if (info != null) {
					mobileno = info.getString("mobileno");
					userId = info.getString("userid");
				} else {
					return OpenResult.parameterError("请输入注册身份证号码").buildJson();
				}
			}
			if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(mobileno)
					|| StringUtils.isEmpty(validcode)
					|| StringUtils.isEmpty(passwd)) {
				return OpenResult.parameterError("参数错误").buildJson();
			}
			result = personalService.validCodeForFindPasswd(userId, mobileno,
					validcode);
			int retcode = result.getInteger("retcode");
			if (retcode != 0) {
				if (retcode == 10202) {
					return OpenResult.serviceError(retcode, "验证码错误!")
							.buildJson();
				} else {
					return result.toJSONString();
				}
			}
			/*JSONObject re = personalService.modifyPasswd(userId, mobileno,
					validcode, passwd);
			if (re != null) {
				return re.toJSONString();
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}*/
			return OpenResult.ok().buildJson();
		} catch (StockServiceException e) {
			log.error("修改密码异常：" + e);
			return OpenResult.serviceError(e.getRetcode(), e.getMsg())
					.buildJson();
		}
	}

	/**
	 * 完善用户信息 依次校验 身份证唯一性 手机号唯一性 验证码是否正确 身份证真实姓名是否匹配
	 * 然后授权 更新用户信息
	 * 其中校验 身份证号码格式 唯一性 和真实姓名是否匹配 已经在
	 * {@link AccountRest#isYLBAccount(String)} 中校验
	 * @return
	 */
	@Path("/full/userInfo")
	@POST
	@Produces("application/json;charset=utf-8")
	public String fullUserMsg(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String realName = json.getString("realName");
		String idNumber = json.getString("idNumber");
		String mobileNo = json.getString("mobileNo");
		String code = json.getString("code");
		String ssoId = json.getString("UID");
		String userId = ssoId;
		boolean flag = ValidateUtil.isMobile(mobileNo);
		if (!flag) {
			return OpenResult.serviceError(10119, "手机号码有误").buildJson();
		}
		try {
			// 检验手机号是否被注册
			JSONObject result = registService.mobileUnique(mobileNo);
			if (result != null) {
				if (result.getIntValue("retcode") != 0) {
					return OpenResult.parameterError(
							result.getIntValue("retcode"),
							result.getString("msg")).buildJson();
				}
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}

			// 检验验证码
			JSONObject codeResult = registService.checkIdentifyingCode(
					mobileNo, CodeType.REGISTER.type, code);
			if (codeResult != null) {
				Integer retcode = codeResult.getInteger("retcode");
				String msg = codeResult.getString("msg");
				if (retcode != 0) {
					return OpenResult.parameterError(retcode, msg).buildJson();
				}
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
			//注册盈利宝
//			String passwd = generatePassword(8);
//			记得改成通行证注册 2014-10-20
	/*		JSONObject regResult  = registService.mobileRegist(mobileNo,passwd,code);
			if (regResult != null) {
				if (regResult.getIntValue("retcode") != 0) {
					return OpenResult.parameterError(
							regResult.getIntValue("retcode"),
							regResult.getString("msg")).buildJson();
				}
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
			
			String userId = regResult.getString("userid");*/
			//授权		
//			accountAuthService.authorize(userId, ssoId, AuthType.JRJSSO);
//			
			UserInfo userInfo = userInfoService.queryUserInfo(userId);
			UserInfo info = null;		
			if(userInfo == null){
				info = userInfoService.createUserInfo(userId, UserStatus.COMPETE);
				if(info == null){
					OpenResult.serviceError("-1", "完善信息失败").buildJson();
				}
			}else{
				if(userInfo.getStatus() == UserStatus.INCOMPLETE.status){
					info = userInfoService.updateUserInfo(userId, UserStatus.COMPETE);
					if(info == null){
						OpenResult.serviceError("-1", "完善信息失败").buildJson();
					}
				}
				if(userInfo.getStatus() == UserStatus.COMPETE.status){
					String errorNo = "-1";
					String errorInfo = "您已完善信息";
					return OpenResult.serviceError(errorNo, errorInfo).buildJson();
				}
			}
			//更新用户信息
			JSONObject IDRealresult = personalService.updateRealNameIDNumber(ssoId, idNumber,
					realName,mobileNo);
			if (IDRealresult != null) {
				if (IDRealresult.getInteger("retcode") != 0) {
					return IDRealresult.toJSONString();
				}
			} else {
				return OpenResult.unknown("服务异常").buildJson();
			}
			FullUserResult user = new FullUserResult();
			user.setUserId(ssoId);
			return OpenResult.ok().add("data", user).buildJson();
		} catch (ServiceException e) {	
			log.error("Full user authorize ServiceException -->"+e.getMessage(),e);
			return OpenResult
					.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
		} catch (StockServiceException e) {
			log.error("Full user  StockServiceException -->"+e.getMessage(),e);
			return OpenResult
					.serviceError(e.getRetcode(),e.getMsg())
					.buildJson();
		} catch (Exception e) {
			log.error("Full user Exception -->"+e.getMessage(),e);
			return OpenResult.serviceError("-1",e.getMessage())
					.buildJson();
		}
	}
}
