package com.cfo.stock.web.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.exception.StockRestException;
import com.cfo.stock.web.rest.result.BrokerEx;
import com.cfo.stock.web.rest.result.ConfigPassWordResult;
import com.cfo.stock.web.rest.result.LoginResult;
import com.cfo.stock.web.rest.result.ModelConfigResult;
import com.cfo.stock.web.rest.service.PersonalService;
import com.cfo.stock.web.rest.utils.ConstantVariable;
import com.cfo.stock.web.rest.utils.ListUtils;
import com.cfo.stock.web.rest.utils.Md5Utils;
import com.cfo.stock.web.rest.vo.JRJLoginVo;
import com.cfo.stock.web.rest.vo.PassportLoginVo;
import com.jrj.common.cache.memcached.MemcachedCache;
import com.jrj.common.utils.DateUtil;
import com.jrj.common.utils.ListUtil;
import com.jrj.common.utils.RandomUtil;
import com.jrj.stocktrade.api.account.AccountService;
import com.jrj.stocktrade.api.account.vo.BindInfo;
import com.jrj.stocktrade.api.account.vo.Broker;
import com.jrj.stocktrade.api.account.vo.FundAccount;
import com.jrj.stocktrade.api.account.vo.UserAccAuth;
import com.jrj.stocktrade.api.banktrans.vo.BankTransfer;
import com.jrj.stocktrade.api.banktrans.vo.HistoryBankTransfer;
import com.jrj.stocktrade.api.common.BrokerId;
import com.jrj.stocktrade.api.common.ExchangeType;
import com.jrj.stocktrade.api.common.MainFlag;
import com.jrj.stocktrade.api.common.SHStockAccountStatus;
import com.jrj.stocktrade.api.common.SZStockAccountStatus;
import com.jrj.stocktrade.api.exception.ServiceException;
import com.jrj.stocktrade.api.helper.BrokerHelper;
import com.jrj.stocktrade.api.opstatus.StockAccountStatusService;
import com.jrj.stocktrade.api.opstatus.vo.StockAccountStatus;
import com.jrj.stocktrade.api.stock.vo.HistoryBusiness;
import com.jrj.stocktrade.api.stock.vo.HistoryFundStockEx;
import com.jrj.stocktrade.api.stock.vo.StockAccount;

/**
 * 
 * @className：StockBaseRest
 * @classDescription：
 * 
 * @author：kecheng.Li
 * 
 * @dateTime：2014年4月22日 下午8:56:12
 */
public abstract class StockBaseRest {
	protected Log log = LogFactory.getLog(this.getClass());
	public static final String baseuri = "/sapi";

	@Autowired
	protected MemcachedCache sessionMemCache;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private StockAccountStatusService stockAccountStatusService;
	
	@Autowired
	private PersonalService personalService;

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	@Autowired
	protected BrokerHelper brokerHelper;
	
	/**
	 * 需要记录在日志里的手机信息字段，通过header传递
	 */
	static final String[] MO_HEADERS = { "devid", "paltid", "appver", "model",
			"localizedModel", "systemName", "systemVersion", "productid",
			"apps" };

	private static final String MO_SESSION_KEY_PRE = "MO_SESSION_KEY_PRE";
	private static final String MO_FINDPWD_SESSION_KEY_PRE = "MO_FINDPWD_SESSION_KEY_PRE";
	private static final int MO_SESSION_TIMEOUT_MIN = 15;// 15分钟超时
	private static final int MO_FINDPWD_SESSION_TIMEOUT_MIN = 15;// 15分钟超时
	private static final String MO_SECCODE = "STOCKNB";
	private static final String MO_USERID = "MO_USERID";

	private static final String MO_SECURITES_INFO = "MO_SECURITES_INFO";

	/*
	 * protected void logHeaders(HttpHeaders headers) {
	 * 
	 * if (headers != null) { JSONObject jsonObject = new JSONObject(); for
	 * (String headerName : MO_HEADERS) { List<String> drivers =
	 * headers.getRequestHeader(headerName); if
	 * (CollectionUtils.isNotEmpty(drivers)) { jsonObject.put(headerName,
	 * ListUtil.join(drivers, ",")); } } if (jsonObject != null &&
	 * jsonObject.size() > 0) { MobileLoggor.info(jsonObject.toJSONString()); }
	 * } }
	 */

	/**
	 * 检验session有效性
	 * 
	 * @param userid
	 * @param sessionId
	 * @return
	 */

	// protected boolean checksSessionId(String userid, String sessionId) {
	// if (log.isDebugEnabled()) {
	// log.debug("checkSessionId:" + userid + "," + sessionId);
	// }
	// if (StringUtils.isBlank(userid) || StringUtils.isBlank(sessionId))
	// return false;
	// String key = MO_SESSION_KEY_PRE + "_" + sessionId;
	// String j = (String) sessionMemCache.get(key);
	// if (log.isDebugEnabled()) {
	// log.debug("checkSessionId,key:" + key + ",value:" + j);
	// }
	// if (j == null)
	// return false; // 自动延期
	// Boolean putok = (Boolean) sessionMemCache.set(key, j,
	// DateUtil.getAddMin(new Date(), MO_SESSION_TIMEOUT_MIN));
	// if (!putok) {
	// log.error("session 延期失败");
	// }
	// JSONObject json = JSONObject.parseObject(j);
	// String memUserid = json.getString("userid");
	// if (memUserid != null && memUserid.equals(userid)) {
	// return true;
	// }
	// return false;
	// }

	protected boolean checkSessionId(String userid, String sessionId) {
		if (log.isDebugEnabled()) {
			log.debug("checkSessionId:" + userid + "," + sessionId);
		}
		if (StringUtils.isBlank(userid) || StringUtils.isBlank(sessionId))
			return false;
		String key = MO_SESSION_KEY_PRE + "_" + sessionId;
		String j = (String) sessionMemCache.get(key);
		if (log.isDebugEnabled()) {
			log.debug("checkSessionId,key:" + key + ",value:" + j);
		}
		if (j == null)
			return false;
		// 自动延期
		Boolean putok = (Boolean) sessionMemCache.set(key, j,
				DateUtil.getAddMin(new Date(), MO_SESSION_TIMEOUT_MIN));
		if (!putok) {
			log.error("session 延期失败");
		}
		JSONObject json = JSONObject.parseObject(j);
		String memUserID = json.getString("userId");
		String UID = json.getString("uID");
		if (memUserID == null) {
			return false;
		}
		if (memUserID != null) {
			boolean flag1 = memUserID.equals(userid);
			boolean flag2 = (UID + "-" + UID).equals(memUserID);
			if (flag1 || flag2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据userId生成sessionId
	 * 
	 * @param userid
	 * @return
	 */
	protected String genSessionid(String userid) {
		String seed = String.valueOf(new Date().getTime())
				+ String.valueOf(RandomUtil.between(0, 100000));
		String sessionid = Md5Utils.getMD5String(userid + MO_SECCODE + seed);
		return sessionid;
	}

	/**
	 * 根据SSOId生成sessionId
	 * 
	 * @param SSOId
	 * @return
	 */
	protected String genSessionidBySSOId(String ssoId) {
		String seed = String.valueOf(new Date().getTime())
				+ String.valueOf(RandomUtil.between(0, 100000));
		String sessionid = Md5Utils.getMD5String(ssoId + MO_SECCODE + seed);
		return sessionid;
	}

	/**
	 * 生成移动端用到的sessionid，并放入memcached
	 * 
	 * @return
	 */
	protected String generateSessionId(LoginResult loginResult) {
		String sessionid = genSessionid(loginResult.getUserid());
		String key = MO_SESSION_KEY_PRE + "_" + sessionid;
		Boolean putok = (Boolean) sessionMemCache.put(key,
				JSONObject.toJSONString(loginResult),
				DateUtil.getAddMin(new Date(), MO_SESSION_TIMEOUT_MIN));
		log.info("memcached session key:" + key);
		if (putok) {
			return sessionid;
		} else {
			throw new StockRestException("登录缓存服务异常");
		}
	}

	protected String generateSessionId(JRJLoginVo jRJLoginVo) {
		String sessionid = genSessionid(jRJLoginVo.getUID());
		String key = MO_SESSION_KEY_PRE + "_" + sessionid;
		Boolean putok = (Boolean) sessionMemCache.put(key,
				JSONObject.toJSONString(jRJLoginVo),
				DateUtil.getAddMin(new Date(), MO_SESSION_TIMEOUT_MIN));
		log.info("memcached session key:" + key);
		if (putok) {
			return sessionid;
		} else {
			throw new StockRestException("登录缓存服务异常");
		}
	}

	protected String generateSessionId(PassportLoginVo passportLoginVo) {
		String sessionid = genSessionid(passportLoginVo.getUserId());
		String key = MO_SESSION_KEY_PRE + "_" + sessionid;
		Boolean putok = (Boolean) sessionMemCache.put(key,
				JSONObject.toJSONString(passportLoginVo),
				DateUtil.getAddMin(new Date(), MO_SESSION_TIMEOUT_MIN));
		log.info("memcached session key:" + key);
		if (putok) {
			return sessionid;
		} else {
			throw new StockRestException("登录缓存服务异常");
		}
	}

	// 以身份证号码作为key 以用户信息作为value存入session
	protected boolean setMemcacheJSON(String idNumber, JSONObject json) {
		if (StringUtils.isEmpty(idNumber) || json == null) {
			return false;
		}
		String key = MO_FINDPWD_SESSION_KEY_PRE + "_" + idNumber;
		Boolean flag = sessionMemCache.set(key, json,
				DateUtil.getAddMin(new Date(), MO_FINDPWD_SESSION_TIMEOUT_MIN));
		if (flag) {
			return true;
		} else {
			throw new StockRestException("登录缓存服务异常");
		}
	}

	protected boolean checkIdNumber(String idNumber) {
		if (log.isDebugEnabled()) {
			log.debug("checkIdNumber:" + idNumber);
		}
		if (StringUtils.isBlank(idNumber))
			return false;
		String key = MO_FINDPWD_SESSION_KEY_PRE + "_" + idNumber;
		JSONObject j = (JSONObject) sessionMemCache.get(key);
		if (log.isDebugEnabled()) {
			log.debug("checkIdNumber,key:" + key + ",value:" + j);
		}
		if (j == null)
			return false;
		// 自动延期
		Boolean flag = (Boolean) sessionMemCache.set(key, j,
				DateUtil.getAddMin(new Date(), MO_SESSION_TIMEOUT_MIN));
		if (!flag) {
			log.error("session 延期失败");
			return false;
		}
		return true;
	}

	protected JSONObject getUser(String idNumber) {
		if (StringUtils.isBlank(idNumber)) {
			return new JSONObject();
		}
		String key = MO_FINDPWD_SESSION_KEY_PRE + "_" + idNumber;
		JSONObject json = (JSONObject) sessionMemCache.get(key);
		return json;
	}

	// 将userId存入memecache session中
	protected boolean setMemcacheUserId(String sessionId, String userId) {
		if (StringUtils.isBlank(sessionId) || StringUtils.isBlank(userId)) {
			return false;
		}
		String key = sessionId + "_" + MO_USERID;
		boolean flag = sessionMemCache.set(key, userId,
				DateUtil.getAddMin(new Date(), MO_SESSION_TIMEOUT_MIN));
		if (flag) {
			return true;
		} else {
			throw new StockRestException("登录缓存服务异常");
		}
	}

	/**
	 * @param headers
	 * @return
	 */
	protected String getDevId(HttpHeaders headers) {
		String devid = "";
		if (headers != null) {
			for (String headerName : MO_HEADERS) {
				List<String> drivers = headers.getRequestHeader(headerName);
				if (CollectionUtils.isNotEmpty(drivers)) {
					if (headerName.equalsIgnoreCase("devid")) {
						devid = ListUtil.join(drivers, ",");
					}
				}
			}
		}
		return devid;
	}

	protected boolean setSecuritiesInfo(String sessionId, String json) {
		if (StringUtils.isBlank(sessionId)) {
			log.debug("sessionId---为空");
			return false;
		}
		String key = sessionId + "_" + MO_SECURITES_INFO;
		boolean flag = sessionMemCache.set(key, json,
				DateUtil.getAddMin(new Date(), MO_SESSION_TIMEOUT_MIN));
		if (flag) {
			return true;
		} else {
			throw new StockRestException("登录缓存服务异常");
		}
	}

	protected JSONObject setSecuritiesInfoJson(String devId, String mobileNo) {
		JSONObject securitiesInfo = new JSONObject();
		securitiesInfo.put("devId", devId);
		securitiesInfo.put("mobileNo", mobileNo);
		return securitiesInfo;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void reverseHistoryFundStockExList(List list) {
		// 将 historyFundStockExList 倒序排列 时间最近的排在最前面
		if (CollectionUtils.isNotEmpty(list)) {
			Collections.sort(list, new Comparator<HistoryFundStockEx>() {
				@Override
				public int compare(HistoryFundStockEx fundStockEx1,
						HistoryFundStockEx fundStockEx2) {
					Long date2 = new Long(fundStockEx2.getBusinessDate());
					Long date1 = new Long(fundStockEx1.getBusinessDate());
					int i = date2.compareTo(date1);
					if (i == 0) {
						Long time2 = new Long(fundStockEx2.getBusinessTime());
						Long time1 = new Long(fundStockEx1.getBusinessTime());
						int j = time2.compareTo(time1);
						return j;
					}
					return i;
				}
			});
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void reverseHistoryBussiness(List list) {
		// 将 获取的HistoryBusiness列表 倒序排列 时间最近的排在最前面
		if (CollectionUtils.isNotEmpty(list)) {
			Collections.sort(list, new Comparator<HistoryBusiness>() {
				@Override
				public int compare(HistoryBusiness b1, HistoryBusiness b2) {
					Long date2 = new Long(b2.getInitDate());
					Long date1 = new Long(b1.getInitDate());
					int i = date2.compareTo(date1);
					if (i == 0) {
						Long time2 = new Long(b2.getBusinessTime());
						Long time1 = new Long(b1.getBusinessTime());
						int j = time2.compareTo(time1);
						return j;
					}
					return i;
				}
			});
		}
	}

	protected void orderAvailableBrokerList(List<BrokerEx> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			Collections.sort(list, new Comparator<BrokerEx>() {
				@Override
				public int compare(BrokerEx o1, BrokerEx o2) {
					Integer order1 = o1.getOrder();
					Integer order2 = o2.getOrder();
					int i = 0;
					if (order1 != null && (order2 != null)) {
						i = order1.compareTo(order2);
					}
					return i;
				}
			});
		}
	}

	// 随机生成8位密码
	public static String generatePassword(int length) {
		String val = "";

		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

			if ("char".equalsIgnoreCase(charOrNum)) // 字符串
			{
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) // 数字
			{
				val += String.valueOf(random.nextInt(10));
			}
		}

		return val;
	}

	public ModelConfigResult setModelConfig(String brokerId) {
		ModelConfigResult result = new ModelConfigResult();
		String buysell = null; // 买卖
		String transfer = null;// 银证转账
		String withdraw = null;// 撤单
		String entrust = null;// 委托查询
		String business = null;// 成交查询
		String fund = null; // 资金流水

		if (BrokerId.ZSZQ_SECURITIES.equals(brokerId)) {
			buysell = "2";
			transfer = "2";
			withdraw = "2";
			entrust = "2";
			business = "2";
			fund = "2";
		} else {
			buysell = "1";
			transfer = "1";
			withdraw = "1";
			entrust = "1";
			business = "1";
			fund = "1";
		}
		result.setBuysell(buysell);
		result.setTransfer(transfer);
		result.setWithdraw(withdraw);
		result.setEntrust(entrust);
		result.setBusiness(business);
		result.setFund(fund);
		return result;
	}

	public String getStockAccountStatus(String brokerId, String exchangeType,
			String status) {
		if (StringUtils.isBlank(status)) {
			status = "1";
		}
		String accountStatus = "";
		if ("SZ".equals(exchangeType)) {
			if (SZStockAccountStatus.SZ_STOCKACCOUNT_SUCCESS.value
					.equals(status)) {
				accountStatus = "已开通";
			} else {
				accountStatus = "未开通";
			}
		}

		if ("SH".equals(exchangeType)) {
			if (SHStockAccountStatus.SH_STOCKACCOUNT_SUCCESS.equals(status)) {
				accountStatus = "已开通";
			} else {
				accountStatus = "未开通";
			}
		}

		return accountStatus;
	}

	public static void main(String[] args) {
		int max = Integer.MAX_VALUE;
		System.out.println(max);
	}

	protected FundAccount getFundAccount(List<FundAccount> fundAccountList,
			String userId, Long accountId) throws ServiceException {
		FundAccount fundAccount = null;
		if (CollectionUtils.isNotEmpty(fundAccountList)) {
			for (FundAccount fa : fundAccountList) {
				if (fa.getMainFlag() != null) {
					String mainValue = fa.getMainFlag().getValue();
					if (StringUtils.isNotBlank(mainValue)) {
						if (MainFlag.MAIN.getValue().equals(mainValue)) {
							fundAccount = fa;
						}
					}
				} else {
					fundAccount = fundAccountList.get(0);
				}
			}
		}
		return fundAccount;
	}

	protected Map<String, String> getStockAccount(List<StockAccount> list) {
		Map<String, String> map = new HashMap<String, String>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (StockAccount stockAccount : list) {
				if (ExchangeType.SH == stockAccount.getExchangeType()) {
					map.put("shStockAccount", stockAccount.getStockAccount());
				}

				if (ExchangeType.SZ == stockAccount.getExchangeType()) {
					map.put("szStockAccount", stockAccount.getStockAccount());
				}
			}

		}
		return map;
	}

	/**
	 * @param list
	 *            被过滤的券商
	 * @param brokerId
	 *            过滤券商的Id
	 * @return
	 */
	protected List<UserAccAuth> filterTradebleList(List<UserAccAuth> list,
			String brokerId) {
		List<UserAccAuth> newList = new ArrayList<UserAccAuth>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				String filterBrokerId = list.get(i).getBrokerId();
				if (!filterBrokerId.equalsIgnoreCase(brokerId)) {
					newList.add(list.get(i));
				}
			}
		}

		return newList;
	}

	/**
	 * @param list
	 *            被过滤的交易账户列表
	 * @param fiterBrokerList
	 *            被过滤的券商列表Id
	 * @return
	 */
	protected List<UserAccAuth> filterTradebleBrokerList(
			List<UserAccAuth> tradeblelist, List<String> fiterBrokerList) {
		if (CollectionUtils.isNotEmpty(tradeblelist)
				&& CollectionUtils.isNotEmpty(fiterBrokerList)) {
			Iterator<UserAccAuth> iterator = tradeblelist.iterator();
			while (iterator.hasNext()) {
				UserAccAuth userAccAuth = iterator.next();
				for (String brokerId : fiterBrokerList) {
					if (brokerId.equalsIgnoreCase(userAccAuth.getBrokerId())) {
						iterator.remove();
					}
				}
			}
		} else {
			tradeblelist = new ArrayList<UserAccAuth>();
		}

		return tradeblelist;
	}

	/**
	 * @param list
	 *            被过滤的券商
	 * @param brokerId
	 *            过滤券商的Id
	 * @return
	 */
	protected List<StockAccountStatus> filterStockAccountStatusList(
			List<StockAccountStatus> list, String brokerId) {
		List<StockAccountStatus> newList = new ArrayList<StockAccountStatus>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				String filterBrokerId = list.get(i).getBrokerId();
				if (!filterBrokerId.equalsIgnoreCase(brokerId)) {
					newList.add(list.get(i));
				}
			}
		}

		return newList;
	}

	/**
	 * @param list  被过滤的券商
	 * @param fiterBrokerList  过滤券商的Id列表
	 * @return
	 */
	protected List<StockAccountStatus> filterStockAccountStatusList(
			List<StockAccountStatus> list, List<String> fiterBrokerList) {
		if (CollectionUtils.isNotEmpty(list)
				&& CollectionUtils.isNotEmpty(fiterBrokerList)) {
			Iterator<StockAccountStatus> iterator = list.iterator();
			while (iterator.hasNext()) {
				StockAccountStatus s = iterator.next();
				for (String brokerId : fiterBrokerList) {
					if (brokerId.equalsIgnoreCase(s.getBrokerId())) {
						iterator.remove();
					}
				}
			}
		} else {
			list = new ArrayList<StockAccountStatus>();
		}
		return list;
	}
	
	
	/** 未开户或者未绑定券商
	 * @param brokerId
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	protected boolean openOrBind(String brokerId,String userId) throws ServiceException{
		boolean flag = false;
		List<StockAccountStatus> stockAccountStatus = new ArrayList<StockAccountStatus>();
//		查询改用户是否绑定
		if(brokerId != null){
			BindInfo bindInfo = accountService.getBindInfo(userId,brokerId);
			if(bindInfo == null){
				flag = true;
			}
		}
//		查询该用户是否开户
		stockAccountStatus = stockAccountStatusService.queryAllStockOpenStatusByUserId(userId);
		if(CollectionUtils.isEmpty(stockAccountStatus)){
			flag = true;			
		}	
		return flag;
	}
	
	/**比较版本号 形如	  1.0.4   1.0.14
	 * @param newVersion  新版本
	 * @param baseVersion 基础版本
	 * @return
	 */
	protected static int compareVersion(String version,String baseVersion){
		int result = 0;
		if(StringUtils.isEmpty(baseVersion)){
			baseVersion = "1.0.4";
		}
		if(StringUtils.isNotBlank(version)){
			String[] v1 = version.split("\\.");
			String[] v2 = baseVersion.split("\\.");
		    int m = Integer.valueOf(v1[0]).intValue();
		    int n = Integer.valueOf(v2[0]).intValue();
//		       比较第一位
		    if(m>n){
		      result = 1;
		      return result;
		    }else if(m == n){
//		    	比较第二位		    	
		    	if(Integer.valueOf(v1[1]).intValue()
		    			>Integer.valueOf(v2[1]).intValue()){
		    		result = 1;
		    		return result;
		    	}else if(Integer.valueOf(v1[1]).intValue()
		    			== Integer.valueOf(v2[1]).intValue()){
//		    		比较第三位
		    		if(Integer.valueOf(v1[2]).intValue()
		    				>Integer.valueOf(v2[2]).intValue()){
		    			result = 1;
		    		}else if(Integer.valueOf(v1[2]).intValue()
		    				== Integer.valueOf(v2[2]).intValue()){
		    			result = 0;
		    		}else{
		    			result = -1;
		    		}
		    		
		    	}else{
		    		result = -1;
		    	}
		    }else{
		    	result = -1;
			    return result;
		    }
		}		
		return result;
		
	}
	
	/** 根据业务及不同版本过滤券商
	 * @param businessValue
	 * @param appver
	 * @param fiterBrokerList
	 * @param userAccAuthList
	 * @return
	 */
	protected List<UserAccAuth> filterUserAccAuthListByDemand(String businessValue,String appver,
			List<String> fiterBrokerList,List<UserAccAuth> userAccAuthList){
		if(CollectionUtils.isNotEmpty(userAccAuthList)){
//			爱投顾业务
			if(businessValue.equalsIgnoreCase("ITOUGU")){
//				版本号大于等于1.0.4 不做处理   小于1.0.4过滤恒泰
				if (compareVersion(appver, "1.0.4")>=0) {

				} else {
					userAccAuthList = filterTradebleList(userAccAuthList,
							BrokerId.CNHT_SECURITIES);
				}
//			炒股必备业务 过滤恒泰
			}else{
				userAccAuthList = filterTradebleList(userAccAuthList,
						BrokerId.CNHT_SECURITIES);
			}
//			web已上线券商 移动端未上线需过滤
			userAccAuthList = filterTradebleBrokerList(userAccAuthList,
					fiterBrokerList);
		}		
		return userAccAuthList;
	}
	
	/** 根据业务及不同版本过滤券商
	 * @param businessValue
	 * @param appver
	 * @param fiterBrokerList
	 * @param stockAccountStatusList
	 * @return
	 */
	protected List<StockAccountStatus> filterStockAccountStatusListByDemand(String businessValue,String appver,
			List<String> fiterBrokerList,List<StockAccountStatus> stockAccountStatusList){
		if(CollectionUtils.isNotEmpty(stockAccountStatusList)){
//			爱投顾业务
			if(businessValue.equalsIgnoreCase("ITOUGU")){
//				版本号大于等于1.0.4 不做处理   小于1.0.4过滤恒泰
				if (compareVersion(appver, "1.0.4")>=0) {

				} else {
					stockAccountStatusList = filterStockAccountStatusList(stockAccountStatusList,
							BrokerId.CNHT_SECURITIES);
				}
//			炒股必备业务 过滤恒泰
			}else{
				stockAccountStatusList = filterStockAccountStatusList(stockAccountStatusList,
						BrokerId.CNHT_SECURITIES);
			}
//			web已上线券商 移动端未上线需过滤
			stockAccountStatusList = filterStockAccountStatusList(stockAccountStatusList,fiterBrokerList);
		}
		return stockAccountStatusList;
	}
	
	/**
	 * 根据平台区分券商 比如Android上线某家券商 IOS不上线该券商
	 * "paltid":"android"  
	 *  "paltid":"IOS"
	 * @param paltid
	 * @param fiterBrokerList
	 * @param userAccAuthList
	 * @return
	 */
	protected List<UserAccAuth> filterUserAccAuthListByPlat(String paltid,
			List<String> fiterBrokerList,List<UserAccAuth> userAccAuthList){
		if(CollectionUtils.isNotEmpty(userAccAuthList)){
//			if(StringUtils.isNotBlank(paltid)){
//				if("IOS".equalsIgnoreCase(paltid)){
					userAccAuthList = filterTradebleBrokerList(userAccAuthList,
							fiterBrokerList);
//				}
//			}
					
		}		
		return userAccAuthList;
	}
	
	/** 1.1.1 更新比较多 此方法以1.1.1版本为比较基础
	 * @param paltid
	 * @param appver
	 * @param fiterBrokerList
	 * @param stockAccountStatusList
	 * @return
	 */
	protected List<StockAccountStatus> filterStockAccountStatusListByPlat(String paltid,String appver,
			List<String> fiterBrokerList,List<StockAccountStatus> stockAccountStatusList){		
		if (compareVersion(appver, "1.1.1")>=0) {

		} else {
			if(CollectionUtils.isNotEmpty(stockAccountStatusList)){
				stockAccountStatusList = filterStockAccountStatusList(stockAccountStatusList,fiterBrokerList);					
			}
		}
				
		return stockAccountStatusList;
	}
	/**
	 * 获取券商信息
	 * @param brokerId
	 * @return
	 */
	public Broker getBroker(String brokerId){
		return brokerHelper.getBroker(brokerId);
	}
	
	/** 过滤移动端不识别的交易类别
	 * @param list
	 * @param obj
	 * @return
	 */
	public List filterBussinessType(List list,Object obj){
		
		if(CollectionUtils.isNotEmpty(list)){
			if(obj instanceof HistoryBankTransfer){
				Iterator iterator= list.iterator();
				while(iterator.hasNext()){
					HistoryBankTransfer  h = (HistoryBankTransfer) iterator.next();
					String businessType = h.getBusinessType().toString();
					if("CGSFQR".equalsIgnoreCase(businessType)){
						iterator.remove();
					}
				}
			}
			
			if(obj instanceof BankTransfer){
				Iterator iterator= list.iterator();
				while(iterator.hasNext()){
					BankTransfer  b = (BankTransfer) iterator.next();
					String businessType = b.getBusinessType().toString();
					if("CGSFQR".equalsIgnoreCase(businessType)){
						iterator.remove();
					}
				}
			}
		}else{
			list = new ArrayList();
		}
		
		return list;
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
		} else {
			return new JSONObject();
		}
		return json;
	}
	
	public ConfigPassWordResult setPwdType(String brokerId,List<String> ITNList,List<String> txPwdList){
		ConfigPassWordResult result = new ConfigPassWordResult();
		
		String tradePassword="true"; //默认交易需要密码
		String tradeJyPassword="true";  //默认交易需要交易密码
		String tradeTxPassword="false"; //默认交易不需要通讯密码
		
		String isInitialize="false"; //是否需要重新初始化
		String initJyPassword="true"; //如果需要初始化 默认需要交易密码
		String initTxPassword="false";//如果需要初始化 默认不需要通讯密码
		String reLoginAuthType = "";
//		恒泰证券属于自接券商 需特殊判断
		if(StringUtils.equals(BrokerId.CNHT_SECURITIES, brokerId)){
			isInitialize="true"; 
			reLoginAuthType = "H5Type";
		}
		
		if(CollectionUtils.isNotEmpty(ITNList)){
			if(ITNList.contains(brokerId)){
				isInitialize="true"; 
				tradePassword="false";
				tradeJyPassword="false";
				reLoginAuthType="nativeType"; 
			}
		}
		if(CollectionUtils.isNotEmpty(txPwdList)){
			if(txPwdList.contains(brokerId)){
				initTxPassword="true";
			}
		}
		
		result.setIsInitialize(isInitialize);
		result.setInitJyPassword(initJyPassword);
		result.setInitTxPassword(initTxPassword);
		
		result.setTradePassword(tradePassword);
		result.setTradeJyPassword(tradeJyPassword);
		result.setTradeTxPassword(tradeTxPassword);
		result.setReLoginAuthType(reLoginAuthType);
		return result;
	}
//	设置 重新登录Type类型
	
	public String  setReLoginAuthType(String brokerId,List<String> ITNList,List<String> txPwdList){
		String reLoginAuthType = "";
//		恒泰证券属于自接券商 需特殊判断
		if(StringUtils.equals(BrokerId.CNHT_SECURITIES, brokerId)){
			reLoginAuthType = "H5Type";
		}
		if(CollectionUtils.isNotEmpty(ITNList)){
			if(ITNList.contains(brokerId)){
				reLoginAuthType="nativeType"; 
			}
		}
		return reLoginAuthType;
	}
	
	protected String getTradeWay(String brokerId){
		String tradeWay = "";
		List<String> tradeWayOwnList = ListUtils.stringToList(ConstantVariable.tradeWayOwn, ",");
		List<String> tradeWayITNList = ListUtils.stringToList(ConstantVariable.tradeWayITN, ",");
		if(tradeWayOwnList.contains(brokerId)){
			tradeWay = "0";
		}
		if(tradeWayITNList.contains(brokerId)){
			tradeWay = "1";
		}
		return tradeWay;
	}
}