package com.cfo.stock.web.rest.deprecated;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.cfo.stock.web.rest.result.JRJLoginResult;
import com.cfo.stock.web.rest.result.ModelConfigResult;
import com.cfo.stock.web.rest.service.PersonalService;
import com.cfo.stock.web.rest.vo.JRJLoginVo;
import com.jrj.common.utils.DateUtil;
import com.jrj.sso.util.MobileUserValidator;
import com.jrj.stocktrade.api.account.AccountService;
import com.jrj.stocktrade.api.account.UserAccountService;
import com.jrj.stocktrade.api.account.UserAuthService;
import com.jrj.stocktrade.api.account.UserInfoService;
import com.jrj.stocktrade.api.account.vo.Broker;
import com.jrj.stocktrade.api.account.vo.UserAccAuth;
import com.jrj.stocktrade.api.account.vo.UserAccount;
import com.jrj.stocktrade.api.account.vo.UserInfo;
import com.jrj.stocktrade.api.common.AccUserStatus;
import com.jrj.stocktrade.api.common.BrokerId;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.opstatus.StockAccountStatusService;
import com.jrj.stocktrade.api.opstatus.vo.StockAccountStatus;
import com.jrj.stocktrade.api.stock.StockAccountQueryService;
import com.jrj.stocktrade.api.stock.vo.StockAccount;

@Path(StockBaseRest.baseuri + "/jrjsso")
@Controller
public class JRJSSOAccountOldRest extends StockBaseRest {


	@Autowired
	private PersonalService personalService;

	@Autowired
	private StockAccountStatusService stockAccountStatusService;

	@Autowired
	private StockAccountQueryService stockAccountQueryService;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private UserAuthService userAuthService;
	
	@Autowired
	private UserAccountService userAccountService;

	@Path("/getstatus")
	@POST
	@Produces("application/json;charset=utf-8")
	public String getJRJLoginStatus(String content) {
		if (StringUtils.isEmpty(content)) {
			return OpenResult.parameterError("无参数").buildJson();
		}
		JSONObject json = JSONObject.parseObject(content);
		String UID = json.getString("UID");
		String token = json.getString("token");
		String userName = json.getString("userName");
		boolean flag = false;
		if (StringUtils.isEmpty(UID) || StringUtils.isEmpty(token)
				|| StringUtils.isEmpty(userName)) {
			return OpenResult.parameterError("参数不正确").buildJson();
		}
		flag = MobileUserValidator.validate(UID, token);
		if (!flag) {
			return OpenResult.serviceError(10004, "金融界用户ID和token不匹配")
					.buildJson();
		}

		String userId = "";
		String sessionId = "";
		String mobileNo = "";
		String fundAccount = "";
		String status = "";
		String realName = "";
		String stockAccount = "";
		String exchangeType = "";
		String szStockAccount = "";
		String shStockAccount = "";
		String createTime = "";
		String openTime = "";
		int accountType = 0;
		String tradeableTime = "";
		String brokerId = "";
		String szStockAccountStatus  = "";
		String shStockAccountStatus = "";
		long accountId = 0L;
		JRJLoginVo loginVo = new JRJLoginVo(userName, UID);
		List<UserAccAuth> userAccAuthlist = new ArrayList<UserAccAuth>();
		try {
//			UserAuth userAuth = accountAuthService.getAuthInfo(UID,
//					AuthType.JRJSSO);
			UserInfo userInfo = userInfoService.queryUserInfo(UID);
			if (userInfo == null) {
				status = "-11";
				// 此处设置 setUserId是为了 后面检验session使用
				loginVo.setUserId(UID + "-" + UID);
			} else {
				userId = userInfo.getUserId();
				loginVo.setUserId(userId);				
				status = "-10";
				// 判断用户是否绑定 如果已绑定直接进入交易
				JSONObject j = getUserBaseInfo(userId);
				mobileNo = j.getString("mobileNo");
				realName = j.getString("realName");
				List<Broker> brokerList = accountService
						.queryBindedBrokers(userId);
				userAccAuthlist = userAuthService.queryAccessAble(userId);
				log.info("userAccAuthlist-->"+JSONObject.toJSONString(userAccAuthlist));	
								
				if(CollectionUtils.isNotEmpty(userAccAuthlist)){
//                   判断可交易账户中是否有自己的账户					
					 flag   = isSelfAccount(userAccAuthlist);
					 if(!flag){
						 return OpenResult.serviceError("-1", "尊敬的用户，手机目前只支持本人的中山或中信证券交易，您可以登录证券通网站（https://t.jrj.com.cn/stock）进行其他交易")
									.buildJson(); 
					 }
//					判断可交易列表中是否有默认账户		 
					 for (UserAccAuth userAccAuth : userAccAuthlist){
							boolean isDef = userAccAuth.isDef();
							if(isDef){
								accountId = userAccAuth.getAccountId();
								brokerId = userAccAuth.getBrokerId();
						}
//					默然账户必须是自己的账户
					if(accountId != 0L){
						UserAccount userAccount = userAccountService.queryAccount(userId, accountId);						
//						UserAccAuth u = userAuthService.queryUserAuth(userId, accountId);
						log.info("userAccount---->"+JSONObject.toJSONString(userAccount));
						if(userAccount == null){
							return OpenResult.serviceError("-2", "尊敬的用户，手机目前只支持本人的中山或中信证券交易，您可以登录证券通网站（https://t.jrj.com.cn/stock）进行其他交易")
									.buildJson();
						}
					}		
					}
//					没有默认券商  取第一个
					if(accountId == 0L){
						UserAccAuth usuth = userAccAuthlist.get(0);					
						accountId = usuth.getAccountId();	
						brokerId = usuth.getBrokerId();

					}			 
				}else{
					userAccAuthlist = userAuthService.queryUserAuth(userId);
					if(CollectionUtils.isNotEmpty(userAccAuthlist)){
						accountId = userAccAuthlist.get(0).getAccountId();
						brokerId = userAccAuthlist.get(0).getBrokerId();

					}
				}
				
				if (CollectionUtils.isNotEmpty(brokerList)) {
//					brokerId = brokerList.get(0).getBrokerId();
					status = "9";					
					List<StockAccount> list = stockAccountQueryService
							.clientStockAccountQuery(userId,accountId);
					if (CollectionUtils.isNotEmpty(list)) {
						for (StockAccount stAccount : list) {
							log.info("login get StockAccount-->"
									+ JSONObject.toJSONString(stAccount));
							String value = stAccount.getExchangeType()
									.getValue();
							if ("1".equalsIgnoreCase(value)) {
								shStockAccount = stAccount.getStockAccount();
							}
							if ("2".equalsIgnoreCase(value)) {
								szStockAccount = stAccount.getStockAccount();
							}
						}
					}
					// 如果 未绑定 查看用户当前所处状态
				} else {
					
					StockAccountStatus stockAccountStatus = null;
					List<StockAccountStatus> stockAccountStatusList = stockAccountStatusService
							.queryAllStockOpenStatus(userId, realName,
									j.getString("idNumber"));
					if (CollectionUtils.isNotEmpty(stockAccountStatusList)) {
						stockAccountStatus = stockAccountStatusList.get(0);
					}
					if(stockAccountStatus != null){
						if(StringUtils.isEmpty(brokerId)){
							brokerId = stockAccountStatus.getBrokerId();
						}
						szStockAccountStatus = getStockAccountStatus(brokerId,"SZ",stockAccountStatus.getSzStackAccountStatus());						
						shStockAccountStatus = getStockAccountStatus(brokerId,"SH",stockAccountStatus.getShStackAccountStatus());
						
						status = stockAccountStatus.getAccountStatus().getValue();
						createTime = DateUtil.format(stockAccountStatus.getCreateTime(), "yyyy年MM月dd日");
						accountType = stockAccountStatus.getType().type;
						tradeableTime = DateUtil.format(stockAccountStatus.getTradeableTime(),"yyyy年MM月dd日");
						openTime = DateUtil.format(stockAccountStatus.getOpenTime(), "yyyy年MM月dd日");
						fundAccount = stockAccountStatus.getFundId();
					}
					
				}
//				判断券商是否是中山或者中信证券
				if(StringUtils.isNotBlank(brokerId)){
					flag = isSureBroker(brokerId);
					if(!flag){
						return OpenResult.serviceError("-3", "尊敬的用户，手机目前只支持本人的中山或中信证券交易，您可以登录证券通网站（https://t.jrj.com.cn/stock）进行其他交易")
								.buildJson();
					}
				}
				
			}

			// 根据金融界登陆参数生成sessionId
			sessionId = generateSessionId(loginVo);
			ModelConfigResult configResult = setModelConfig(brokerId);
			JRJLoginResult result = new JRJLoginResult(configResult);
			result.setUserId(userId);
			result.setStatus(status);
			result.setBrokerId(brokerId);
			result.setFundAccount(fundAccount);
			result.setRealName(realName);
			result.setSessionId(sessionId);
			result.setExchangeType(exchangeType);
			result.setStockAccount(stockAccount);
			result.setMobileNo(mobileNo);
			result.setSzStockAccount(szStockAccount);
			result.setShStockAccount(shStockAccount);
			result.setCreateTime(createTime);
			result.setOpenTime(openTime);
			result.setAccountType(accountType);
			result.setTradeableTime(tradeableTime);
			result.setSzStockAccountStatus(szStockAccountStatus);
			result.setShStockAccountStatus(shStockAccountStatus);
			
			return OpenResult.ok().add("data", result).buildJson();
		} catch (ServiceException e) {
			log.error("JRJ login ServiceException---" + e.getMessage(), e);
			return OpenResult.serviceError(e.getErrorNo(), e.getErrorInfo())
					.buildJson();
		} catch (Exception e) {
			log.error("JRJ login Exception--->" + e.getMessage(), e);
			return OpenResult.unknown(e.getMessage()).buildJson();
		}
	}

	protected JSONObject getUserBaseInfo(String userId) {
		JSONObject json = new JSONObject();
		JSONObject j = personalService.getUserInfo(userId);
		if (j != null) {
			int retcode = j.getIntValue("retcode");
			if (retcode != 0) {
				return j;
			}
			String mobileNo = j.getString("mobile");
			String realName = j.getString("trueName");
			String idNumber = j.getString("idCard");

			json.put("mobileNo", mobileNo);
			json.put("realName", realName);
			json.put("idNumber", idNumber);
		}else{
			return new JSONObject();
		}
		return json;
	}

	
//	判断是否有本人的账户 
	private  boolean isSelfAccount(List<UserAccAuth> accessAbleList){
		boolean flag = false;
		List<Integer> statusList = new ArrayList<Integer>();
		for (UserAccAuth userAccAuth : accessAbleList){
			Integer status = userAccAuth.getStatus();
			statusList.add(status);						
		}
		Integer selfAccountStatus = AccUserStatus.OWNCONTROL.status;
		flag = statusList.contains(selfAccountStatus);		
		return flag;
	}
//	判断是否有中山或者中信证券
	private boolean isSureBroker(String brokerId){
		boolean flag = false;
		List<String>  brokerIdList = new ArrayList<String>();
		brokerIdList.add(BrokerId.CITIC_SECURITIES);
		brokerIdList.add(BrokerId.ZSZQ_SECURITIES);
		flag = brokerIdList.contains(brokerId);
		return flag;
	}
	
	public static void main(String[] args) {
		String UID = "110729010012435928";
		String token = "Qn9034bAhYbV0AxYH91XaQ6yKnVlMU9D+v+a6mObKaTeSeiJb2VSxHSfVnOGm/0m";
		boolean flag = MobileUserValidator.validate(UID, token);
		System.out.println(flag);
	}
}
