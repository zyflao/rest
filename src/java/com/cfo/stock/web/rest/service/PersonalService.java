package com.cfo.stock.web.rest.service;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.common.UserNameType;
import com.cfo.stock.web.rest.exception.StockServiceException;
import com.cfo.stock.web.rest.utils.ValidateUtil;

@Service
public class PersonalService extends AbstractStockService {

	/**
	 * 通行证Id获取用户信息
	 * 
	 * @param userId
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject getUserInfo(String userId)
			throws StockServiceException {

		String result = userStockApi.getUserInfo(userId);
		if (StringUtils.isEmpty(result)) {
			return new JSONObject();
		}
		if (log.isDebugEnabled()) {
			log.debug("用户信息--->" + result);
		}
		return parsePassPortResultArray(result);
	}
	
	
	/** 验证用户身份者号 真实姓名
	 * @param idnumber
	 * @param realname
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject validRealNameId(String idnumber,
			String realname) throws StockServiceException {
		if (StringUtils.isEmpty(idnumber)|| StringUtils.isEmpty(realname)) {
			return null;
		}
		String result = userStockApi.validRealNameId(idnumber, realname);
		if (StringUtils.isEmpty(result)) {
			return new JSONObject();
		}
//		将用户中心的 resultCode  resultMsg 替换为  retcode  msg
		return parsePassPortResult(result);
	}
	
	/** 验证用户身份者号 真实姓名
	 * @param idnumber
	 * @param realname
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject sureRealNameId(String idnumber,
			String realname,String idvalid) throws StockServiceException {
		if (StringUtils.isEmpty(idnumber)|| StringUtils.isEmpty(realname)) {
			return null;
		}
		String result = userStockApi.sureRealNameId(idnumber, realname,idvalid);
		if (StringUtils.isEmpty(result)) {
			return new JSONObject();
		}
		return parsePassPortResult(result);
	}

	/** 填写用户的 真实姓名 身份证号码
	 * @param userId
	 * @param idnumber
	 * @param realname
	 * @param idvalid
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject updateRealNameIDNumber(String userId, String idnumber,
			String realname,String mobileno) throws StockServiceException {
		if (StringUtils.isEmpty(userId) || 
				(idnumber == null && realname == null && mobileno == null)) {
			return null;
		}
		String result = userStockApi.updateRealNameID(userId, idnumber,
				realname,mobileno);
		if (StringUtils.isEmpty(result)) {
			return new JSONObject();
		}
		return parsePassPortResult(result);
	}

	/** 验证身份证号码的唯一性
	 * @param idnumber
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject checkUniqueIdnumber(String idnumber)
			throws StockServiceException {
		if (StringUtils.isEmpty(idnumber)) {
			return null;
		}
		String result = userStockApi.checkUniqueIdnumber(idnumber);
		if (StringUtils.isEmpty(result)) {
			return new JSONObject();
		}
		if (log.isDebugEnabled()) {
			log.debug("====验证身份证号码的唯一性结果！==" + result);
		}
		return parsePassPortResult(result);
	}
	
	/**
	 * @param loginname
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject queryUserInfo(String loginname)throws StockServiceException{
		if(StringUtils.isEmpty(loginname)){
			return null;
		}
		int nametype = 0;
		if(ValidateUtil.isIdNumber(loginname)){
			nametype = UserNameType.IDCARD.type;
		}else if(ValidateUtil.isUserName(loginname)){
			nametype = UserNameType.USERNAME.type;
		}else if(ValidateUtil.isMobile(loginname)){
			nametype = UserNameType.MOBILE.type;
		}else if(ValidateUtil.isEmail(loginname)){
			nametype = UserNameType.EMAIL.type;
		}
		if (nametype == 0) {
			return null;
		}
		String result = userStockApi.queryUserInfo(loginname, nametype);
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}
		return parsePassPortResult(result);
	}
	
	public JSONObject getCodeForfindPasswd(String userid,String mobileno)throws StockServiceException{
		if(StringUtils.isEmpty(userid)||StringUtils.isEmpty(mobileno)){
			return null;
		}
		String result = userStockApi.getCodeForfindPasswd(userid, mobileno);
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}
		
		return parsePassPortResult(result);
	}
	
	public JSONObject validCodeForFindPasswd(String userid,String mobileno,String validcode)throws StockServiceException{
		if(StringUtils.isEmpty(userid)||
				StringUtils.isEmpty(mobileno)
				||StringUtils.isEmpty(validcode)){
			return null;
		}
		String result = userStockApi.validCodeForFindPasswd(userid, mobileno, validcode);
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}
		
		return parsePassPortResult(result);
	}
	

	
	/*public JSONObject modifyPasswd(String userid,String mobileno,String validcode,String passwd)throws StockServiceException{
		if(StringUtils.isEmpty(userid)||StringUtils.isEmpty(mobileno)
			||StringUtils.isEmpty(validcode)||StringUtils.isEmpty(passwd)	){
			return null;
		}
		String result = userStockApi.modifyPasswd(userid, mobileno, validcode, passwd);
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}		
		return parsePassPortResult(result);
	}*/
	
	public JSONObject queryUserIdByIdNumber(String realName,String idNumber)throws StockServiceException{
		if(StringUtils.isEmpty(idNumber)){
			return null;
		}
		String result = userStockApi.queryPassPortIdByIdNumber(realName, idNumber);
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}		
		return parsePassPortResult(result);
	}
	
	/** 校验AccessToken
	 * @param userId
	 * @param accessToken
	 * @return
	 * @throws StockServiceException
	 * @throws UnsupportedEncodingException 
	 */
	public JSONObject checkAccessToken(String userId,String accessToken)throws StockServiceException, UnsupportedEncodingException{
		if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(accessToken)){
			return new JSONObject();
		}
		String result = userStockApi.checkAccessToken(userId, accessToken);
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}	
		if (log.isDebugEnabled()) {
			log.debug("check Access Token-->" + result);
		}
		return parsePassPortResult(result);
	}
	
	/**邀请码 验证
	 * @param userId
	 * @param inviteCode
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject verifyInvitationCode(String userId,String inviteCode)throws StockServiceException{
		if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(inviteCode)){
			return new JSONObject();
		}
		String result = userStockApi.verifyInvitationCode(userId, inviteCode);
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}	
		log.info("verify  Invitation  Code-->" + result);
		if (log.isDebugEnabled()) {
			log.debug("verify  Invitation  Code-->" + result);
		}
		
		return parsePassPortInvitationResult(result);
	}
	/**
	 * 根据userid获取所有已填邀请码
	 * @author: jianchao.zhao
	 * @date: 2015年2月28日 上午10:36:32
	 * @mail: jianchao.zhao@jrj.com.cn
	 * @param userId
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject getAllInvitationRecordByToId(String userId)throws StockServiceException{
		if(StringUtils.isEmpty(userId)){
			return new JSONObject();
		}
		String result = userStockApi.getAllInvitationRecordByToId(userId);
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}	
		log.info("verify all Invitation  Code-->" + result);
		if (log.isDebugEnabled()) {
			log.debug("verify all Invitation  Code-->" + result);
		}
		return getInvitationRecordByToIdResult(result,userId);
	}
	
	/** 通行证用户基础信息 返回结果封装  
	 *  resultCode为0代表成功 非0失败
	 * @param result
	 * @return
	 */
	private static JSONObject parsePassPortResult(String result){
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}
		JSONObject json = JSONObject.parseObject(result);
		String msg = json.getString("resultMsg");
		int retcode = json.getIntValue("resultCode");
		String userId = json.getString("passportId");
		
		if(StringUtils.isEmpty(msg)){
			msg = "";
		}
		json.put("msg", msg);
		json.put("retcode", retcode);
		json.put("userId", userId);
		return json;
	}
	
	/** 通行证返回结果封装 返回结果为 数组形式
	 * @param result
	 * @return
	 */
	private static JSONObject parsePassPortResultArray(String result){
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}
		String msg = "";
		int retcode = 0;
		JSONArray jsonArray = JSONObject.parseArray(result);
		JSONObject json = new JSONObject();
		if(jsonArray != null && jsonArray.size()>0){
			json = jsonArray.getJSONObject(0);
			String userId = json.getString("passportId");
			json.put("msg", msg);
			json.put("retcode", retcode);
			json.put("userId", userId);
			return json;
		}else{
			retcode = -1;
			msg = "用户信息为空";
			json.put("msg", msg);
			json.put("retcode", retcode);
			return json;
		}
		
	}
	
	/** 通行证用户邀请码信息 返回结果封装  
	 *  resultCode 为1代表成功
		(-1:参数为空
		-2:无对应邀请码
		-3:邀请码不正确
		-4:邀请人已经邀请过被邀请人
		0:校验失败
		1:校验成功)
	 * @param result
	 * @return
	 */
	private static JSONObject parsePassPortInvitationResult(String result){
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}		
		JSONObject json = JSONObject.parseObject(result);
		String msg = json.getString("resultMsg");
		//int retcode = json.getIntValue("resultCode");
		String retcode = json.getString("result");
		String userId = json.getString("passportId");
		//System.out.println("邀请码验证结果retcode"+retcode);
		if(StringUtils.isEmpty(msg)){
			msg = "";
		}
		if("1".equals(retcode)){
			json.put("retcode", 0);
			json.put("msg","");
		}else if("0".equals(retcode)){
			json.put("retcode", -1);
			json.put("msg", "校验失败");
		}else{
			json.put("retcode", retcode);
			json.put("msg", msg);
		}		
		json.put("userId", userId);
		return json;
	}
	/** 通过userid获取  获取所有已填邀请码信息  返回结果封装  
	 * 赵建超
	 * @param 
	 * @return result -1:参数为空0:无邀请信息1:成功2:异常    remark 1：注册 2：完善信息3：绑户  4：申请投顾 5：申请达人
	 */
	private static JSONObject getInvitationRecordByToIdResult(String result,String userId){
		if(StringUtils.isEmpty(result) || StringUtils.isEmpty(userId)){
			return new JSONObject();
		}		
	
		JSONObject json = JSONObject.parseObject(result);
		String msg = json.getString("resultMsg");
		String retcode = json.getString("result");
		String passportId = json.getString(userId);
		JSONArray passportIdJson = JSONArray.parseArray(passportId); 
		if(passportIdJson!=null){
			if(passportIdJson.size()>=0){
				for(int i=0;i<passportIdJson.size();i++){
					JSONObject job = passportIdJson.getJSONObject(i);
					String passportFromId = job.getString("passportFromId");
					String code = job.getString("code");
					String remark = job.getString("remark");
					if("1".equals(remark)){
						json.put("invitateCode", code);
						json.put("passportFromId", passportFromId);
						break;
					}
				}
			}
		}else{
			json.put("invitateCode","");
		}
	
		//System.out.println("邀请码验证结果retcode"+retcode);
		if(StringUtils.isEmpty(msg)){
			msg = "";
		}
		json.put("retcode", retcode);
		json.put("msg", msg);
		json.put("userId", userId);
		return json;
	}
	
	public static void main(String[] args) {
//		String result = "{'passportId':'000822010000046691'}";
		String result = "{'resultMsg':'ppp','resultCode':11}";
		//String result = '{"resultMsg":"成功","result":"1",[{"passportId":"141222010009788524","code":"TYU76K"},{"passportId":"141217010020947173","code":"TYU76K"}]}';
		//JSONObject json = parsePassPortResult(result);
		//System.out.println(json);
//		PersonalService p=new PersonalService();
//		p.getInvitationRecordByToId("141017010039806379");
//		String str = "[{name:'a',value:'aa'},{name:'b',value:'bb'},{name:'c',value:'cc'},{name:'d',value:'dd'}]" ;  // 一个未转化的字符串
//		JSONArray json = JSONArray.parseArray(str); // 首先把字符串转成 JSONArray  对象
//		if(json.size()>0){
//		  for(int i=0;i<json.size();i++){
//		    JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
//		    System.out.println(job.get("name")+"=") ;  // 得到 每个对象中的属性值
//		  }
//		}
	}
}
