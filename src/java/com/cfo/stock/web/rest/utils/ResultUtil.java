package com.cfo.stock.web.rest.utils;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.exception.StockServiceException;
import com.cfo.stock.web.rest.result.Result;

/**
 * Result工具类
 * @author yuanlong.wang
 *
 */
@SuppressWarnings("rawtypes")
public class ResultUtil {
	/**
	 * 指定转换成Result的子类
	 * @param result
	 * @param clazz
	 * @return
	 */
	public static <T extends Result> T parseResult(String result,Class<T> clazz)throws StockServiceException{
		T  t=null;
		if(!StringUtils.isBlank(result)){
			t=JSONObject.parseObject(result, clazz);
		}
		if(t==null){
			throw new StockServiceException(-1, "The Data Is Empty");
		}
		if(t.getRetcode()!=0){
			throw new StockServiceException(t.getRetcode(), t.getMsg());
		}
		return t;
	}
	/**
	 * 指定转换成Result的子类 不检验异常
	 * @param result
	 * @param clazz
	 * @return
	 */

	public static <T extends Result> T parseResultNoE(String result,Class<T> clazz){
		T  t=null;
		if(!StringUtils.isBlank(result)){
			t=JSONObject.parseObject(result, clazz);
		}
		return t;
	}
	
	public static Result parseResult(String result){
		return JSONObject.parseObject(result, Result.class);
	}
	
	
	public static Result parseResultE(String result){
		Result t=null;
		if(!StringUtils.isBlank(result)){
			t=JSONObject.parseObject(result,Result.class);
		}
		if(t==null){
			throw new StockServiceException(-1, "The Data Is Empty");
		}
		if(t.getRetcode()!=0){
			throw new StockServiceException(t.getRetcode(), t.getMsg());
		}
		return t;
	}
}
