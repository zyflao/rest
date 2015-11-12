package com.cfo.stock.web.rest.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.StockBaseRest;
import com.cfo.stock.web.rest.common.InfoMasker;
import com.cfo.stock.web.rest.result.CurrentPostionResult;
import com.cfo.stock.web.rest.result.StockAccountStatusResult;
import com.cfo.stock.web.rest.service.LoginOutService;
import com.cfo.stock.web.rest.service.PersonalService;
import com.cfo.stock.web.rest.utils.BigDecimalUtils;
import com.cfo.stock.web.rest.utils.ConstantVariable;
import com.cfo.stock.web.rest.utils.ListUtils;
import com.cfo.stock.web.rest.vo.AccountVo;
import com.jrj.common.utils.DateUtil;
import com.jrj.common.utils.PropertyManager;
import com.jrj.stocktrade.api.account.AccountQueryService;
import com.jrj.stocktrade.api.account.AccountService;
import com.jrj.stocktrade.api.account.UserAccountService;
import com.jrj.stocktrade.api.account.UserAuthService;
import com.jrj.stocktrade.api.account.UserInfoService;
import com.jrj.stocktrade.api.account.vo.BindInfo;
import com.jrj.stocktrade.api.account.vo.FundAccount;
import com.jrj.stocktrade.api.account.vo.UserAccAuth;
import com.jrj.stocktrade.api.common.AccUserType;
import com.jrj.stocktrade.api.common.AccountStatus;
import com.jrj.stocktrade.api.common.BrokerId;
import com.jrj.stocktrade.api.deposit.FundService;
import com.jrj.stocktrade.api.deposit.vo.FundInfoEx;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.exception.UnAvailableException;
import com.jrj.stocktrade.api.opstatus.StockAccountStatusService;
import com.jrj.stocktrade.api.opstatus.vo.StockAccountStatus;
import com.jrj.stocktrade.api.pub.BranchQueryService;
import com.jrj.stocktrade.api.pub.vo.BranchQueryResp;
import com.jrj.stocktrade.api.stock.StockAccountQueryService;
import com.jrj.stocktrade.api.stock.vo.StockAccount;

public class AccountBase extends StockBaseRest {
	
	
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
	protected FundService fundService;

	@Autowired
	private UserInfoService userInfoService;

	public String queryAccountStatusBase(String userId,
			String sessionId,
			String brokerId,
			long accountId) {
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(sessionId)) {
			return OpenResult.parameterError("参数错误").buildJson();
		}
		if (checkSessionId(userId, sessionId)) {
			StockAccountStatus stockAccountStatus = null;
			StockAccountStatusResult result = new StockAccountStatusResult();

			String realName = null;
			int idType = 0;// 证件类型
			String idNo = "";// 证件号码
			String accountStatus = AccountStatus.ENTER_OPEN.getValue();// 账户状态
			String broker = "";// 券商Id
			String createTime = "";// 创建时间
			String openTime = "";// 开户成功时间
			String fundId = "";// 资金账号
			String customerNo = "";// 客户号
			int accountType = 0;
			String tradeableTime = "";
			String szStockAccount = "";
			String shStockAccount = "";
			String brokerName = "";
			String szStockAccountStatus = "";
			String shStockAccountStatus = "";
			try {
				JSONObject json = getUserBaseInfo(userId);
				String real = json.getString("realName");
				String idNumber = json.getString("idNumber");
				log.info("<--realName:"+realName+"<--idNumber:"+idNumber);
				stockAccountStatus = stockAccountStatusService
							.queryStockOpenStatus(userId,real,idNumber, brokerId);
				if(stockAccountStatus == null){
					List<StockAccountStatus> stockAccountStatusList = stockAccountStatusService
							.queryAllStockOpenStatus(userId,real,idNumber);
					if (CollectionUtils.isNotEmpty(stockAccountStatusList)) {
						stockAccountStatus = stockAccountStatusList.get(0);
					}					
				}

				if (stockAccountStatus != null) {
					accountStatus = stockAccountStatus.getAccountStatus()
							.getValue();
					broker = stockAccountStatus.getBrokerId();
					brokerName = brokerHelper.getBrokerName(broker);
					szStockAccountStatus = getStockAccountStatus(broker, "SZ",
							stockAccountStatus.getSzStackAccountStatus());
					shStockAccountStatus = getStockAccountStatus(broker, "SH",
							stockAccountStatus.getShStackAccountStatus());
					createTime = DateUtil.format(
							stockAccountStatus.getCreateTime(), "yyyy年MM月dd日");
					customerNo = stockAccountStatus.getCustomerNo();
					fundId = stockAccountStatus.getFundId();
					idNo = stockAccountStatus.getIdNo();
					idType = stockAccountStatus.getIdType().type;
					openTime = DateUtil.format(
							stockAccountStatus.getOpenTime(), "yyyy年MM月dd日");
					realName = stockAccountStatus.getRealName();
					accountType = stockAccountStatus.getType().type;
					tradeableTime = DateUtil.format(
							stockAccountStatus.getTradeableTime(),
							"yyyy年MM月dd日");
					BindInfo bindInfo = accountService.getBindInfo(userId,
							stockAccountStatus.getBrokerId());
					if (bindInfo != null) {
						List<StockAccount> list = stockAccountQueryService
								.clientStockAccountQuery(userId, accountId);
						if (CollectionUtils.isNotEmpty(list)) {
							for (StockAccount stAccount : list) {
								log.info("login get StockAccount-->"
										+ JSONObject.toJSONString(stAccount));
								String value = stAccount.getExchangeType()
										.getValue();
								if ("1".equalsIgnoreCase(value)) {
									shStockAccount = stAccount
											.getStockAccount();
								}
								if ("2".equalsIgnoreCase(value)) {
									szStockAccount = stAccount
											.getStockAccount();
								}
							}
						}
					}

				}
				result.setAccountStatus(accountStatus);
				result.setBrokerId(broker);
				result.setCreateTime(createTime);
				result.setCustomerNo(customerNo);
				result.setFundId(fundId);
				result.setIdNo(idNo);
				result.setIdType(idType);
				result.setOpenTime(openTime);
				result.setRealName(realName);
				result.setAccountType(accountType);
				result.setTradeableTime(tradeableTime);
				result.setSzStockAccount(szStockAccount);
				result.setShStockAccount(shStockAccount);
				result.setBrokerName(brokerName);
				result.setShStockAccountStatus(shStockAccountStatus);
				result.setSzStockAccountStatus(szStockAccountStatus);
				return OpenResult.ok().add("data", result).buildJson();
			} catch (ServiceException e) {
				log.error("查询开户状态异常" + e.getMessage(), e);
				return OpenResult
						.serviceError(e.getErrorNo(), e.getErrorInfo())
						.buildJson();
			}
		} else {
			return OpenResult.noAccess("未授权").buildJson();
		}
	}
	
	/** 用户userId 获取用户基本信息
	 * @param userId
	 * @return
	 */
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
	
	/** 账户管理列表 获取按钮是否置灰标识
	 * @param brokerId
	 * @return
	 */
	public String getBindButtonFlag(String brokerId){
		String flag = "1";
		String strBrokerId = PropertyManager.getString("hand_bind_button_list");
		List<String> buttonBrokerList = ListUtils.stringToList(strBrokerId, ",");
		String autoBindBrokerId = PropertyManager.getString("auto_bind_button_list");
		List<String> autoButtonBrokerList = ListUtils.stringToList(autoBindBrokerId, ",");
		if(CollectionUtils.isNotEmpty(buttonBrokerList)){
			boolean b = buttonBrokerList.contains(brokerId);
			if(b){
				flag = "-1";
			}			
		}
		if(CollectionUtils.isNotEmpty(autoButtonBrokerList)){
			boolean b = autoButtonBrokerList.contains(brokerId);
			if(b){
				flag = "-2";
			}			
		}
		return flag ;
	}
	
	/**
	 * @param paltid
	 * @param brokerId
	 * @return
	 */
	public String getOpenTransferFlag(String paltid,String brokerId,String appver){
		String flag = "1";
		int result = compareVersion(appver, "2.1.0");
			if(paltid.equalsIgnoreCase("IOS")){  //IOS
				String openTransferBrokerId = PropertyManager.getString("open_transfer_button_list");
				List<String> autoButtonBrokerList = ListUtils.stringToList(openTransferBrokerId, ",");
//				IOS如果版本小于等于2.0.1 长城 中金不能继续开户   大于等于2.1.0长城能继续开户
				if(result<0){
					if(CollectionUtils.isNotEmpty(autoButtonBrokerList)){
						if(autoButtonBrokerList.contains(brokerId)){
							flag = "-1";
						}			
					}
				}else{
					if(BrokerId.ZJZQ_SECURITIES.equals(brokerId)){
						flag = "-1";
					}
				}
			}else{  //Andriod
				if (result<0){ //如果版本小于2.1.0 长城不能继续开户
					if(BrokerId.CGWS_SECURITIES.equals(brokerId)){
						flag = "-1";
					}
				}
			}
				
		return flag ;
	}
	
	/** 设置当前账户页的 账户基本信息
	 * @param res
	 * @param userAccAuth
	 * @return
	 * @throws ServiceException
	 */
	public CurrentPostionResult setPostionBaseInfo(CurrentPostionResult res,UserAccAuth userAccAuth)
			throws ServiceException{
		if(res == null){
			res = new CurrentPostionResult();
		}
		String fundAccount = "";
		if (userAccAuth != null) {
			String brokerId = userAccAuth.getBrokerId();
			String brokerName = brokerHelper.getBrokerName(brokerId);
			String brokerLogo = ConstantVariable.brokerLogoUrl + "/" + brokerId+ "_mobile.png";
			JSONObject j = getUserBaseInfo(userAccAuth.getAccUserId());
			String realName = j.getString("realName");
//			获取资金账号
			List<FundAccount> fundAccountList = accountQueryService.fundAccountQuery(userAccAuth.getUserId(), userAccAuth.getAccountId());
			if(CollectionUtils.isNotEmpty(fundAccountList)){
				FundAccount fundAccountInfo = getFundAccount(fundAccountList, userAccAuth.getUserId(), userAccAuth.getAccountId());
				if(fundAccountInfo != null){
					fundAccount = fundAccountInfo.getFundAccount();
				}
//				如果是别人的账号只显示前4位
				if(userAccAuth.getType() != null &&userAccAuth.getType().intValue() == AccUserType.OTHER.type){					
					if(StringUtils.isNotEmpty(fundAccount)&&fundAccount.length()>4){
						fundAccount = InfoMasker.masker(fundAccount, 4, fundAccount.length()-4, "*", 1);
					}				
				}
			}
			res.setFundId(fundAccount);
			res.setBrokerId(brokerId);
			res.setBrokerName(brokerName);
			res.setBrokerLogo(brokerLogo);
			res.setRealName(realName);
		}		
		return res;
	}
	
	/** 设置 当前账户页的   总资产 今日盈亏 
	 * @param userId
	 * @param accountId
	 * @return
	 * @throws ServiceException
	 */
	public CurrentPostionResult setPostionAsset(String userId,Long accountId) throws ServiceException{
		CurrentPostionResult res = new CurrentPostionResult();
		FundInfoEx fundInfoEx = fundService.clientFundAllQuery(
				userId, accountId);
		BigDecimal todayGenLose = fundService.caculateTodayGenLoseRate(userId, accountId, fundInfoEx);
		res.setTodayGenLose(todayGenLose.toString());
		String percent = BigDecimalUtils.processDecimalFraction(
				todayGenLose).toString();
		res.setPercent(percent + "%");
		if (fundInfoEx != null) {
			BigDecimal assetBalance = fundInfoEx.getAssetBalance();
			res.setAssetBalance(assetBalance.toString());
		}
		return res;
	}
	
	/**
	 * 判断待绑定状态是否已被绑定 过滤 列表记录
	 */
	public boolean isBindForOpenUser(StockAccountStatus stockAccountStatus,
			List<UserAccAuth> accauthList) {
		for (UserAccAuth userAccAuth : accauthList) {
			if (userAccAuth.getUserId().equals(stockAccountStatus.getUserId())
					&& userAccAuth.getBrokerId().equals(
							stockAccountStatus.getBrokerId())
					&& userAccAuth.getType() != null
					&& userAccAuth.getType().intValue() == AccUserType.OWNER.type) {
				return true;
			}
		}
		return false;
	}
//	券商通过ITN已绑定 又点击改券商开户 过滤掉开户记录
	public Boolean filterBindedByITN(StockAccountStatus stockAccountStatus,
			List<UserAccAuth> accauthList){
		boolean flag = false;
		for(UserAccAuth userAccAuth: accauthList ){
			if(userAccAuth.getUserId().equals(stockAccountStatus.getUserId())
			   &&userAccAuth.getBrokerId().equalsIgnoreCase("ITN_"+stockAccountStatus.getBrokerId())
			   &&userAccAuth.getType() != null
			   &&userAccAuth.getType()== AccUserType.OWNER.type
			   ){
				flag = true;
			}
		}
		return flag;
	}

	//
	protected AccountVo getFundStockAccount(String userId, long accountId,
			String idNumber)throws UnAvailableException,Exception {
		AccountVo accountVo = new AccountVo();

		List<FundAccount> fundAccountList = new ArrayList<FundAccount>();
		try {
			fundAccountList = accountQueryService.fundAccountQuery(userId,
					accountId);
			if (CollectionUtils.isNotEmpty(fundAccountList)) {
				FundAccount fundAccount = getFundAccount(fundAccountList,
						userId, accountId);
				if (fundAccount != null) {
					String fundId = fundAccount.getFundAccount();
					accountVo.setFundAccount(fundId);
					String branchNo = fundAccount.getBranchNo();
					if (StringUtils.isNotEmpty(idNumber)) {
						BranchQueryResp branchQueryResp = branchQueryService
								.branchQuery(userId, accountId, branchNo,
										idNumber);
						String branchName = branchQueryResp.getBranchName();
						accountVo.setBranchName(branchName);
					}
				}
			}
		} catch (ServiceException e) {
			log.error("get FundStockAccount  ServiceException" + e.getErrorInfo(),e);
		}

		return accountVo;
	}
	
}
