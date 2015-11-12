package com.cfo.stock.web.rest.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.exception.StockServiceException;

@Service
public class RegistService extends AbstractStockService {
	
	/** 注册 必传参数
	 * @param mobile
	 * @param password
	 * @param validcode
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject  mobileRegist(String mobile, String password
			,String validcode)throws StockServiceException{
		if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)||StringUtils.isEmpty(validcode)){
			return null;
		}		
			String regInfo  = userStockApi.userRegist(mobile, password,validcode);
			if(StringUtils.isBlank(regInfo)){
				return new JSONObject();
			}
			if (log.isDebugEnabled()) {
				log.debug("====注册结果！==" + regInfo);
			}
			return parsePassPortResult(regInfo);
	}
	
	/** 注册 全部参数
	 * @param mobile
	 * @param password
	 * @param validcode
	 * @param clientip
	 * @param clientinfo
	 * @param usedefaulttemplate
	 * @param smstemplate
	 * @param cccode
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject  mobileRegistParamAll(String mobile, String password,String validcode,String clientip, String clientinfo,String cccode)throws StockServiceException{
		if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)||StringUtils.isEmpty(validcode)){
			return null;
		}		
			String regInfo  = userStockApi.userRegistParamaAll(mobile, password, validcode, clientip,
					clientinfo,cccode);
			if(StringUtils.isBlank(regInfo)){
				return new JSONObject();
			}
			if (log.isDebugEnabled()) {
				log.debug("====注册结果！==" + regInfo);
			}
			return parsePassPortResult(regInfo);
	}
	
	/** 获取验证码
	 * @param mobileno
	 * @param codetype
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject getIdentifyingCode(String mobileno) throws StockServiceException{
		String rs = userStockApi.getCode(mobileno);
		if(StringUtils.isBlank(rs)){
			return new JSONObject();
		}
		if (log.isDebugEnabled()) {
			log.debug("get mobile code--->" + rs);
		}
		return parsePassPortResult(rs);
	}
	
	/** 手机号唯一性
	 * @param mobile
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject mobileUnique(String mobile)throws StockServiceException{
		String rs = userStockApi.mobileUnique(mobile);
		if(StringUtils.isBlank(rs)){
			return new JSONObject();
		}
		if (log.isDebugEnabled()) {
			log.debug("mobile Exist--->" + rs);
		}
		return parsePassPortResult(rs);
	}
	
	/**检验验证码
	 * @param mobile
	 * @param codeType
	 * @param validateCode
	 * @return
	 * @throws StockServiceException
	 */
	public JSONObject checkIdentifyingCode(String mobile, String codeType,String validateCode)throws StockServiceException{
		String rs = userStockApi.checkIdentifyingCode(mobile,"",validateCode);
		log.info("check Identifying Code--->"+rs);
		if(StringUtils.isBlank(rs)){
			return new JSONObject();
		}
		return parsePassPortResult(rs);
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
	 * @throws StockServiceException
	 */
	public JSONObject updateUserInfo(String userid, String validdate,String postcode,String regioncode, String address,
			int sex,String description,String reservedinfo)throws StockServiceException{
		String rs = userStockApi.updateUserInfo(userid, validdate, postcode, regioncode, address, sex,
				description,reservedinfo);
		log.info("updateUserInfo==>"+rs);
		if(StringUtils.isBlank(rs)){
			return new JSONObject();
		}
		return parsePassPortResult(rs);
	}
	
	/** 校验邮编
	 * @param postCode
	 * @return
	 */
	public Boolean checkPostCode(String postCode){
		String regex = "^[1-9][0-9]{5}(?!\\d)$";
		Pattern pattern  =  Pattern.compile(regex);
		Matcher matcher = pattern.matcher(postCode);
		Boolean flag = matcher.matches();
		return flag;
	}
	
	private JSONObject parsePassPortResult(String result){
		if(StringUtils.isEmpty(result)){
			return new JSONObject();
		}
		JSONObject json = JSONObject.parseObject(result);
		if (log.isDebugEnabled()) {
			log.debug("--->" + json);
		}
		String msg = json.getString("resultMsg");
		int retcode = json.getIntValue("resultCode");
		json.put("msg", msg);
		json.put("retcode", retcode);
		return json;
	}
	
	public static void main(String[] args) {
//		String str ="100010";
//		boolean flag = checkPostCode(str);
//		System.out.println(flag);
	}
}
