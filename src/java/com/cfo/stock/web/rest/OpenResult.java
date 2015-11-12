/**
 * 
 */
package com.cfo.stock.web.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 开放平台返回对象基础工具
 * 
 * @author coldwater
 * 
 */
public class OpenResult {
	public static final int OK = 0;
	public static final int UNKNOWN_ERROR = 10001;
	public static final int NOTOPEN_ERROR = 10124;
	public static final int NOAVAILABLE_ASSET_ERROR = 10125;
	public static final int NOEXIST_ERROR = 10002;
	public static final int PARAMETER_ERROR = 10003;
	public static final int NOACCESS_ERROR = 10004;
	public static final int USERDEFINED_ERROR = 10005;
	public static final int NICKNAMEEXIST_ERROR = 10104;
	public static final int SIGNED_ERROR = 10105;
	public static final int INCOMPLETE_ERROR = 10006;
	public static final int NULLOBJECT_ERROR = 10007;

	private static final String OK_MSG = "";
	private static final String UNKNOWN_MSG = "未知异常";
	private static final String NOEXIST_MSG = "未找到对应资源";
	private static final String PARAMETER_MSG = "参数不合法";
	private static final String NOACCESS_MSG = "未授权";
	private static final String NICKNAMEEXIST_MSG = "昵称重复";
	private static final String SIGNED_MSG = "重复注册";
	private static final String NULLOBJECT_MSG = "对象为空";
	private static final int[] retcodes = new int[]{10004,0};
	
	public static final String VERIFY_INVATITAION_CODE = "请输入正确的邀请码";
	
	public static final String SERVER_BUSY_MSG = "服务器正忙，请稍后再试！";
	private int retcode;
	private String msg;
	private Map<String, Object> adds = new HashMap<String, Object>();

	private String errorNo;
	private String errorInfo;

	public OpenResult(String errorNo, String errorInfo) {
		super();
		this.errorNo = errorNo;
		this.errorInfo = errorInfo;
	}

	public String getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(String errorNo) {
		this.errorNo = errorNo;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	/**
	 * 返回成功的返回
	 * 
	 * @return
	 */
	public static OpenResult ok() {
		return new OpenResult(OK, OK_MSG);
	}

	/**
	 * 返回“未知错误”的返回
	 * 
	 * @return
	 */
	public static OpenResult unknown(String msg) {
		if (StringUtils.isBlank(msg)) {
			msg = "";
		}
		return new OpenResult(UNKNOWN_ERROR, UNKNOWN_MSG.concat(":")
				.concat(msg));
	}

	/**
	 * 返回“未找到对应资源错误”的返回对象
	 * 
	 * @return
	 */
	public static OpenResult noExist(String msg) {
		return new OpenResult(NOEXIST_ERROR, NOEXIST_MSG.concat(":")
				.concat(msg));
	}

	public static OpenResult nullObject(String msg) {
		return new OpenResult(NULLOBJECT_ERROR, NULLOBJECT_MSG.concat(":")
				.concat(msg));
	}

	/**
	 * 返回“参数不合法”的返回对象
	 * 
	 * @return
	 */
	public static OpenResult parameterError(String msg) {
		return new OpenResult(PARAMETER_ERROR, msg);
	}

	/**
	 * 返回“未授权”的返回对象
	 * 
	 * @return
	 */
	public static OpenResult noAccess(String msg) {
		return new OpenResult(NOACCESS_ERROR, NOACCESS_MSG.concat(":").concat(
				msg));
	}

	/**
	 * 返回“自定义”的返回对象
	 * 
	 * @param ret
	 * @param msg
	 * @return
	 */
	public static OpenResult userDefined(String msg) {
		return new OpenResult(USERDEFINED_ERROR, msg);
	}

	/**
	 * 返回“昵称重复”的返回对象
	 * 
	 * @return
	 */
	public static OpenResult nickNameExist(String msg) {
		return new OpenResult(NICKNAMEEXIST_ERROR, NICKNAMEEXIST_MSG
				.concat(":").concat(msg));
	}

	/**
	 * 返回 重复注册
	 * 
	 * @param msg
	 * @return
	 */
	public static OpenResult signedError(String msg) {
		return new OpenResult(SIGNED_ERROR, SIGNED_MSG.concat(":").concat(msg));
	}

	public OpenResult(int retcode, String msg) {
		this.retcode = retcode;
		this.msg = msg;
	}

	/**
	 * 添加返回内容
	 * 
	 * @param key
	 * @param value
	 */
	public OpenResult add(String key, Object value) {
		adds.put(key, value);
		return this;
	}

	/**
	 * 生成json字符串
	 * 
	 * @return
	 */
	public String buildJson() {
		Map<String, Object> build = new HashMap<String, Object>();
		build.put("msg", msg);
		build.put("retcode", retcode);
		build.putAll(adds);
		return JSONObject.toJSONString(build);
	}

	/**
	 * 生成json字符串
	 * 
	 * @return
	 */
	// 注释：这个方法里JSONObject.toJSONString(build,filter);在jenkins里build不过去，又没人用，所以先注释了
	// public String buildJson(PropertyPreFilter filter) {
	// Map<String, Object> build = new HashMap<String, Object>();
	// build.put("msg", msg);
	// build.put("ret", ret);
	// build.putAll(adds);
	// return JSONObject.toJSONString(build,filter);
	// }

	/**
	 * 服务异常
	 * 
	 * @param msg
	 * @return
	 */
	public static OpenResult serviceError(int retcode, String msg) {
		return new OpenResult(retcode, msg);
	}

	/**
	 * 服务异常
	 * 
	 * @param msg
	 * @return
	 */
	public static OpenResult serviceError(String errorNo, String errorInfo) {
		if(StringUtils.isBlank(errorNo)){
			errorNo = "-1";
		}
		int retcode = 0;
		String msg = "";
		String regExStr = "^[\u4e00-\u9fa5_a-zA-Z0-9]+$";
		String regChinese = "^[\u4e00-\u9fa5]+$";
		String regExNum = "^[-+]?[0-9]+$";
		Pattern patternStr = Pattern.compile(regExStr);
		Pattern patternNum = Pattern.compile(regExNum);
		Pattern patternChinese = Pattern.compile(regChinese);
		Boolean flagStr = patternStr.matcher(errorNo).matches();
		Boolean flagNum = patternNum.matcher(errorNo).matches();

		if (flagStr) {
			retcode = -1;
		}
		if (flagNum) {
			retcode = Integer.parseInt(errorNo);
		}
//		为了避免 券商返回的retcode和自定义的冲突  对券商返回的做统一处理 不够五位的补全五位 自定义的不做处理
		if(!ArrayUtils.contains(retcodes, retcode)){//统一 处理成五位
			if(errorNo.length()<5){
				errorNo = StringUtils.substring(errorNo+"00000", 0, 5);
			}
		}
		retcode = Integer.parseInt(errorNo);
		msg = errorInfo;
		/*try {
			if (StringUtils.isNotBlank(errorInfo)) {
				if (errorInfo.startsWith("[")) {
					String[] strArray = errorInfo.split("]");
					if (strArray.length > 2) {
						msg = strArray[1].trim().substring(1);
					}
					// msg = errorInfo.split("]")[0].trim().substring(1)
					// +"] ["+errorInfo.split("]")[1].trim().substring(1);
				} else {
					String[] strs = errorInfo.split("\\[");
					if (strs.length > 1) {
						msg = strs[0].trim();
						Boolean flagChinese= patternChinese.matcher(msg).matches();					
						if(!flagChinese){
							for(String str:strs){
								if(str.contains("]")){
									msg = str.substring(str.indexOf("]")+1).trim();
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			return new OpenResult(retcode, msg);
		}*/
		return new OpenResult(retcode, msg);
	}

	
	/**
	 * 输入错误提示
	 * 
	 * @param msg
	 * @return
	 */
	public static OpenResult parameterError(int retcode, String msg) {
		return new OpenResult(retcode, msg);
	}

	/**
	 * 输入错误提示
	 * 
	 * @param msg
	 * @return
	 */
	public static OpenResult commonError(int retcode, String msg) {
		return new OpenResult(retcode, msg);
	}

	public static void main(String[] args) {
//		String errorNo = "10004";
		int errorNo = 0;
//		String errorInfo1 = "可取现金不足[fund_account = 880000001295,money_type = 0,occur_balance = 5000.00,fetch_cash = 564.52]";
		String errorInfo2 = "-990240280[-990240280]委托价格超过涨停价格";
//		String errorInfo3 = "[14702][资产账号记录表不存在][3423423fundAccount=12345]";
		OpenResult openRe = serviceError(errorNo, errorInfo2);
		System.out.println(openRe.getErrorInfo()+"--"+openRe.getErrorNo());
		String ss = StringUtils.substring(errorNo+"0000", 0, 5);
		System.out.println("ss-->"+ss);
		// String dateStr = "20140918";
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// try {
		// Date date = sdf.parse(dateStr);
		// System.out.println(date);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
