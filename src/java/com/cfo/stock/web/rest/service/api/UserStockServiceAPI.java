package com.cfo.stock.web.rest.service.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.springframework.stereotype.Service;

import com.cfo.stock.web.rest.globals.StockGlobals;
import com.cfo.stock.web.rest.service.api.param.FindPasswdParam;
import com.cfo.stock.web.rest.service.api.param.ModifyUserParam;
import com.cfo.stock.web.rest.service.api.param.RegParam;
import com.cfo.stock.web.rest.service.api.param.UpdateNameParam;
import com.cfo.stock.web.rest.service.api.param.UserLoginParam;
import com.cfo.stock.web.rest.service.api.param.UserQueryInfoParam;

/**
 * 
 * @className：UserServiceAPI
 * @classDescription：
 * 
 * @author：kecheng.Li
 * 
 * @dateTime：2014年4月19日 下午2:12:32
 */
@Service("userStockApi")
public class UserStockServiceAPI extends StockServiceAPI {

	// 用户登录
	private final static String USER_LOGIN_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
			+ "/user/login";

	// 用户注册
	private final static String USER_RSGIST_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
			+ "/user/reg";

	// 通行证Id获取用户信息
	private final static String USER_INFO_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
//			+ "/user/info/$userid";
			+"/userPassportById.jsp";

	// 手机号唯一性
	private final static String MOBILE_UNIQUE_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
//			+ "/user/unique_mobile";
			+ "/mobileExist.jsp";

	// 获取验证码
	private final static String GET_IDENTIFYING_CODE_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
//			+ "/sms/getcode";
			+"/sendVerifyCode.jsp";

	// 验证验证码
	private final static String CHECK_IDENTIFYING_CODE_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
//			+ "/sms/valid";
			+"/verifyCode.jsp";
	
//	// 获取用户受控信息
//	private final static String USER_CONTROLLED_INFO_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
//				+ "/user/controlled/info/$userid";

	// 修改用户信息
	private final static String MODIFY_USER_INFO_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
			+ "/user/update";
	
    //填写用户姓名、身份证
	private final static String UPDATE_USER_USERNAME_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
//			+ "/user/updatename";
			+"/updatePassport.jsp";
	
	//身份证唯一性验证	
	private final static String UNIQUE_IDNUMBER_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
//			+ "/user/unique_idnumber";
			+"/certificateExist.jsp";
	
	//验证身份证 真实姓名	
		private final static String VALID_IDNUMBER_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
//				+ "/user/valid_idnumber";
				+"/certificateValid.jsp";
		
	//无密码获取用户手机、邮箱
	    private final static String USER_QUERYINFO_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
						+ "/user/queryinfo";	
	    	    
	 //手机获取修改密码验证码
	   private final static String FINDPASSWD_GETCODE_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
						+ "/user/findpasswd/getvalidcode_bymobile";	
	   
	 //验证验证码
	   private final static String FINDPASSWD_VALIDCODE_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
						+ "/user/findpasswd/validcode_bymobile";	 
	   
	 //修改密码
	   private final static String FINDPASSWD_MODIFY_PASSWD_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
						+ "/user/findpasswd/modify_bymobile";
	 //用户身份证和真实姓名获取通行证Id
	   private final static String GET_USERID_BYIDNUMBER_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
						+ "/getPassportIdByIdCard.jsp";
	   
	   
	   //用户身份证和真实姓名获取通行证Id
	   private final static String CHECK_ACCESS_TOKEN_URI = StockGlobals.STOCK_REST_USER_CENTER_SERVICE
						+ "/checkAccessToken.jsp";
	   
	 //邀请码验证
	   private final static String VERIFY_INVIATION_CODE_URI = StockGlobals.STOCK_REST_USER_CENTER_INVITATION
						+ "/verifyInvitationCode.jsp";
	  
	 //被邀请人id获取邀请人及其邀请码（机构&个人）
	   private final static String GET_ALL_INVIATION_RECORD_BY_TOID = StockGlobals.STOCK_REST_USER_CENTER_INVITATION
						+ "/getAllInvitationRecordByToId.jsp";
	/**
	 * 用户登录 必传参数
	 * 
	 * @param loginname
	 * @param type
	 * @param password
	 * @return
	 */
	public String userLogin(String loginname, int type, String password) {
		return _post(USER_LOGIN_URI, new UserLoginParam(loginname, type,
				password));
	}
	
	/** 登陆 全部参数
	 * @param loginname
	 * @param type
	 * @param password
	 * @param clientip
	 * @param clientinfo
	 * @param cccode
	 * @return
	 */
	public String userLoginParamAll(String loginname, int type, String password,String clientip, String clientinfo, String cccode) {
		return _post(USER_LOGIN_URI, new UserLoginParam(loginname, type,
				password,clientip,clientinfo,cccode));
	}

	/**获取用户信息
	 * @param userId
	 * @return
	 */
	public String getUserInfo(String userId) {
//		String uri = USER_INFO_URI.replaceAll("\\$userid", userId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("passportIds", userId);
		return _postPassPort(USER_INFO_URI,transMapToString(map));
	}
	
	/**校验手机传递到后台的AccessToken
	 * @param userId
	 * @param accessToken
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String checkAccessToken(String userId,String accessToken) throws UnsupportedEncodingException {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("passportIds", userId);
//		map.put("accessToken", accessToken);
		return _get(CHECK_ACCESS_TOKEN_URI+"?passportId="+URLEncoder.encode(userId, "UTF-8")+"&accessToken="+URLEncoder.encode(accessToken, "UTF-8"));
	}
	
	/** 获取用户受控信息
	 * @param userId
	 * @return
	 */
//	public String getUserControlledInfo(String userId) {
//		String uri = USER_CONTROLLED_INFO_URI.replaceAll("\\$userid", userId);
//		return _get(uri);
//		
//	}


	/**
	 * 获取验证码
	 * 
	 * @param mobileno
	 * @param codetype
	 * @return
	 */
	public String getCode(String mobileno) {
		Map<String, Object>  map = new HashMap<String, Object>();
		map.put("mobile", mobileno);
		map.put("appParam", "admin");
		return _postPassPort(GET_IDENTIFYING_CODE_URI,transMapToString(map));
	}

	/**
	 * 手机号唯一性
	 * 
	 * @param mobile
	 * @param apps
	 * @return
	 */
	public String mobileUnique(String mobile) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mobile", mobile);
		return _postPassPort(MOBILE_UNIQUE_URI,transMapToString(map));
	}

	/**
	 * 用户注册
	 * 
	 * @param mobile
	 * @param password
	 * @param ip
	 * @param from
	 * @param apps
	 * @return
	 */
	public String userRegist(String mobile, String password, String ip,
			String from) {
		return _post(USER_RSGIST_URI, new RegParam(mobile, password, ip, from));
	}
	
	/**用户注册 必传参数
	 * @param mobile
	 * @param password
	 * @param validcode
	 * @return
	 */
	public String userRegist(String mobile, String password,String validcode) {
		return _post(USER_RSGIST_URI, new RegParam(mobile, password,validcode));
	}

	/** 用户注册 全部参数
	 * @param mobile
	 * @param password
	 * @param validcode
	 * @param clientip
	 * @param clientinfo
	 * @param usedefaulttemplate
	 * @param smstemplate
	 * @param cccode
	 * @return
	 */
	public String userRegistParamaAll(String mobile, String password,String validcode,String clientip, String clientinfo,String cccode) {
		return _post(USER_RSGIST_URI, new RegParam(mobile, password,validcode,clientip,clientinfo,cccode));
	}
	/**
	 * 验证验证码
	 * 
	 * @param mobile
	 * @param validateCode
	 * @return
	 */
	public String checkIdentifyingCode(String mobile,String codeType,
			String validateCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", validateCode);
		map.put("mobile", mobile);
		map.put("appParam", "admin");
		return _postPassPort(CHECK_IDENTIFYING_CODE_URI,transMapToString(map));
	}

	/** 更新用户信息
	 * @param userid
	 * @param validdate
	 * @param postcode
	 * @param regioncode
	 * @param address
	 * @param sex
	 * @param description
	 * @param reservedinfo
	 * @return
	 */
	public String updateUserInfo(String userid, String validdate,
			String postcode, String regioncode, String address, int sex,
			String description, String reservedinfo) {
		return _post(MODIFY_USER_INFO_URI, new ModifyUserParam(userid,
				validdate, postcode, regioncode, address, sex, description,
				reservedinfo));
	}
	
	/** 验证用户手机号 真实姓名
	 * @param idnumber
	 * @param realname
	 * @return
	 */
	public String validRealNameId(String idnumber,
			String realname) {		
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("certificateId", idnumber);
		params.put("certificateName", realname);
		params.put("appParam", "ZQT");		
		return _postPassPort(VALID_IDNUMBER_URI,transMapToString(params));
	}
	
	/** 验证用户手机号 真实姓名
	 * @param idnumber
	 * @param realname
	 * @return
	 */
	public String sureRealNameId(String idnumber,
			String realname,String idvalid) {
		return _post(VALID_IDNUMBER_URI, new UpdateNameParam(
				idnumber, realname,idvalid));
	}
	
	/** 填写用户真实姓名、身份证
	 * @param userid
	 * @param idnumber
	 * @param realname
	 * @return
	 */
	public String updateRealNameID(String userid, String idnumber,String realname
			,String mobileno) {
		Map<String, Object>  data = new HashMap<String, Object>();
		data.put("passportId", userid);
		if(idnumber != null) data.put("idCard", idnumber);
		if(realname != null) data.put("trueName", realname);
		if(mobileno != null) data.put("mobile", mobileno);
		data.put("bizSource", "ZQT");
		return _postPassPort(UPDATE_USER_USERNAME_URI,transMapToString(data));
	}
	
	/** 用户身份证唯一性 验证
	 * @param idnumber
	 * @return
	 */
	public String checkUniqueIdnumber(String idnumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("certificateId", idnumber);
		return _postPassPort(UNIQUE_IDNUMBER_URI,transMapToString(map));
//				_postPassPort(UNIQUE_IDNUMBER_URI, new UpdateNameParam(idnumber));
	}
	
	/**
	 * @param loginname
	 * @param nametype
	 * @return
	 */
	public String queryUserInfo(String loginname,int nametype){
		return _post(USER_QUERYINFO_URI, new UserQueryInfoParam(loginname, nametype));
	}
	
	/** 找回密码 获取验证码
	 * @param userid
	 * @param mobileno
	 * @return
	 */
	public String getCodeForfindPasswd(String userid,String mobileno){
		return _post(FINDPASSWD_GETCODE_URI, new FindPasswdParam(userid, mobileno));
	}
	
	/** 找回密码 验证验证码
	 * @param userid
	 * @param mobileno
	 * @param validcode
	 * @return
	 */
	public String validCodeForFindPasswd(String userid,String mobileno,String validcode){
		return _post(FINDPASSWD_VALIDCODE_URI, new FindPasswdParam(userid, mobileno,validcode));
	}
	
	/** 找回密码 修改密码
	 * @param userid
	 * @param mobileno
	 * @param validcode
	 * @param passwd
	 * @return
	 *//*
	public String modifyPasswd(String userid,String mobileno,String validcode,String passwd){
		return _postPassPort(FINDPASSWD_MODIFY_PASSWD_URI, new FindPasswdParam(userid, mobileno,validcode,passwd));
	}*/
	public String queryPassPortIdByIdNumber(String realName,String idNumber){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idCard",idNumber);
		map.put("trueName", realName);
		
		return _postPassPort(GET_USERID_BYIDNUMBER_URI,transMapToString(map));
	}
	
	/** 邀请码验证
	 * @param userId
	 * @param invitatecode
	 * @return
	 */
	public String verifyInvitationCode(String userId,String inviteCode){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("passportToId",userId);
		map.put("code", inviteCode);
		map.put("bizSource", "ZQT");
		map.put("remark", "2"); //1:注册2:完善信息3:绑户4:申请投顾5:申请达人
		return _postPassPort(VERIFY_INVIATION_CODE_URI,transMapToString(map));
	}
	/**
	 * 被邀请人id获取邀请人及其邀请码（机构&个人）
	 * @author: jianchao.zhao
	 * @date: 2015年2月27日 下午3:31:15
	 * @mail: jianchao.zhao@jrj.com.cn
	 * @return result -1:参数为空0:无邀请信息1:成功2:异常
	 */
	public String getAllInvitationRecordByToId(String userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("passportId",userId);
		//map.put("bizSource", "ZQT");
		return _postPassPort(GET_ALL_INVIATION_RECORD_BY_TOID,transMapToString(map));
	}
	/** 
	 * 方法名称:transMapToString 
	 * 传入参数:map 
	 * 返回值:String 形如 username=chenziwen&password=1234 
	*/  
	private String transMapToString(Map map){  
		NameValuePair[] data = new NameValuePair[map.size()]; 
		Set set = map.entrySet();
        Iterator iterator = set.iterator(); 
        int i=0;  
        while (iterator.hasNext()) {  
            Map.Entry entry = (Map.Entry) iterator.next();
            data[i]=new NameValuePair((String)entry.getKey(),(String)entry.getValue());  
            i++;  
        }  
        log.info("data--->"+data);
		return EncodingUtil.formUrlEncode(data, "UTF-8");
	}
	
	
}
