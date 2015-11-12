/**
 * 
 */
package com.cfo.stock.web.rest.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.jrj.common.cache.memcached.MemcachedCache;

/**  
 * 使用memcache做session管理
 * 
 * 暂时不考虑多线程问题
 * 
 * @author yuanlong.wang 2013-4-25 
 *  
 */

public class MemcacheSessionClient implements SessionClient{
	Logger log=Logger.getLogger(getClass());
	@Autowired
	private MemcachedCache sessionMemCache;
	
	private final static String UNDELINE="_";
	public final static String TIMESTMP="t";

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAttribute(String sessionId, String key) throws Exception {
		JSONObject obj = getSessionData(sessionId);
		AttributeBean bean=obj.getObject(key, AttributeBean.class);
		if (log.isDebugEnabled()){
			log.debug(JSONObject.toJSONString(bean));
		}
		return bean.getValue();
	}

	@Override
	public List<String> getAttributeNames(String sessionId) throws Exception {
		JSONObject obj = getSessionData(sessionId);
		return new ArrayList<String>(obj.keySet());
	}

	private JSONObject getSessionData(String sessionId) {
		String sessionData=(String)sessionMemCache.get(sessionId);
		if (log.isDebugEnabled()){
			log.debug(sessionData);
		}
		JSONObject obj=JSONObject.parseObject(sessionData);
		return obj!=null?obj:new JSONObject();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean setAttribute(String sessionId, AttributeBean attribute)
			throws Exception {
		try {
			JSONObject obj = getSessionData(sessionId);
			obj.put(attribute.getKey(), attribute);
			sessionMemCache.put(sessionId, obj.toJSONString());
			refresh(sessionId);
			return true;
		} catch (Exception e) {
			log.error("setAttribut ERROR",e);
			return false;
		}
	}

	@Override
	public boolean removeAttribute(String sessionId, String key)
			throws Exception {
		try {
			JSONObject obj = getSessionData(sessionId);
			obj.remove(key);
			sessionMemCache.put(sessionId, obj.toJSONString());
			return true;
		} catch (Exception e) {
			log.error("removeAttribute ERROR",e);
			return false;
		}
	}

	@Override
	public boolean delete(String sessionId) throws Exception {
		try {
			sessionMemCache.remove(sessionId);
			sessionMemCache.remove(sessionTimestmp(sessionId));
			return true;
		} catch (Exception e) {
			log.error("delete Session ERROR!",e);
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<AttributeBean> getAttributeBean(String sessionId)
			throws Exception {
		JSONObject obj = getSessionData(sessionId);
		Set<String> keys=obj.keySet();
		List<AttributeBean> list=new ArrayList<AttributeBean>();
		for(String key:keys){
			AttributeBean bean=obj.getObject(key, AttributeBean.class);
			bean.setKey(key);
			list.add(bean);
		}
		return list;
	}

	@Override
	public boolean refresh(String sessionId) throws Exception {
		try {
			sessionMemCache.put(sessionTimestmp(sessionId), new Date().getTime());
			return true;
		} catch (Exception e) {
			log.error("refresh Session ERROR!",e);
			return false;
		}
	}
	private String sessionTimestmp(String sessionId){
		return new StringBuffer().append(sessionId).append(UNDELINE).append(TIMESTMP).toString();
	}

	@Override
	public Long lastAccessTime(String sessionId) throws Exception {
		try {
			return (Long)sessionMemCache.get(sessionTimestmp(sessionId));
		} catch (Exception e) {
			log.error("get lastAccessTime ERROR!",e);
			return null;
		}
	}

	@Override
	public boolean putValue(String sessionId, String key, Object value,
			Date time) throws Exception {
		if(time==null){
			sessionMemCache.set(genKey(sessionId,key), value);
		}else{
			sessionMemCache.set(genKey(sessionId,key), value,time);
		}
		return true;
	}
	
	private String genKey(String sessionId,String key){
		return new StringBuffer().append(sessionId).append(UNDELINE).append(key).toString();
	}

	@Override
	public Object getValue(String sessionId, String key) {
		return sessionMemCache.get(genKey(sessionId,key));
	}

	@Override
	public void removeValue(String sessionId, String key) {
		sessionMemCache.remove(genKey(sessionId, key));
	}
}
