/**
 * 
 */
package com.cfo.stock.web.rest.session;

import java.util.Date;
import java.util.List;

/**  
 * Session管理
 * @author yuanlong.wang 2013-4-25 
 *  
 */

public interface SessionClient {
	/**
	 * 获取属性
	 * @param sessionId
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Object getAttribute(String sessionId,String key)throws Exception;
	/**
	 * 获取所有属性
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<AttributeBean> getAttributeBean(String sessionId)throws Exception;
	/**
	 * 获取所有属性名
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public List<String> getAttributeNames(String sessionId)throws Exception;
	/**
	 * 设置属性
	 * @param sessionId
	 * @param attribute
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public boolean setAttribute(String sessionId, AttributeBean attribute)throws Exception;
	/**
	 * 移除属性
	 * @param sessionId
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public boolean removeAttribute(String sessionId,String key)throws Exception;
	/**
	 * 移除session
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public boolean delete(String sessionId)throws Exception;
	/**
	 * 刷新session
	 * @param sessionId
	 * @return
	 */
	public boolean refresh(String sessionId)throws Exception;
	/**
	 * 上次访问时间
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public Long lastAccessTime(String sessionId)throws Exception;

	/**
	 * 单独 存储某值并会过期
	 * @param sessinId
	 * @param key
	 * @param value
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public boolean putValue(String sessionId,String key,Object value,Date time)throws Exception;
	/**
	 * 取单独存储的值
	 * @param sessinId
	 * @param key
	 * @param clazz
	 * @return
	 */
	public Object getValue(String sessionId,String key);
	/**
	 * 删除单独存储的值
	 * @param sessionId
	 * @param key
	 */
	public void removeValue(String sessionId,String key);
}
