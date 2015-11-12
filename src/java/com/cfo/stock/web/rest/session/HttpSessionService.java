/**
 * 
 */
package com.cfo.stock.web.rest.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.cfo.stock.web.rest.session.utils.StringUtil;


/**
 * 
 * @author yuanlong.wang 2013-4-25
 * 
 */

public class HttpSessionService {
	private final static Logger logger = Logger
			.getLogger(HttpSessionService.class);

	private static HttpSessionService httpSessionService;
	private SessionClient sessionClient;

	/**
	 * 初始化
	 * 
	 * @return
	 */
	public static HttpSessionService getInstance() {
		if (httpSessionService == null) {
			logger.error("[httpSessionService] need initialize in spring config");
			throw new RuntimeException(
					"[httpSessionService] have not initialized!");
		}
		return httpSessionService;
	}

	/**
	 * 删除session
	 * 
	 * @param sessionId
	 */
	public void deleteSession(String sessionId) {
		try {
			sessionClient.delete(sessionId);
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"delete session:session ID:", sessionId));
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * 获取sessin值
	 * 
	 * @param sessionId
	 * @param name
	 * @return
	 */
	public Object getAttribute(String sessionId, String name) {
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"get attribute:session ID:", sessionId,
						";attribute name:", name));
			}
			return sessionClient.getAttribute(sessionId, name);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
	}
	/**
	 * 获取所有session属性值
	 * @param sessionId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<AttributeBean> getAttributes(String sessionId){
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"get attribute:session ID:", sessionId,
						";"));
			}
			return sessionClient.getAttributeBean(sessionId);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ArrayList<AttributeBean>();
		}
	}
	
	/**
	 * 移除session值
	 * 
	 * @param sessionId
	 * @param name
	 */
	public void removeAttribute(String sessionId, String name) {
		if (name == null || name.trim().length() == 0) {
			return;
		}
		try {
			sessionClient.removeAttribute(sessionId, name);
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"remove attribute:session ID:", sessionId,
						";attribute name:", name));
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	/**
	 * 获取所有属性key
	 * 
	 * @param sessionId
	 * @return
	 */
	public List<String> getAttributeNames(String sessionId) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"get attributeNames:session ID:", sessionId));
			}
			return sessionClient.getAttributeNames(sessionId);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
	}

	/**
	 * 设置属性值
	 * 
	 * @param sessionId
	 * @param key
	 * @param obj
	 */
	public void setAttribute(String sessionId, String key, Object obj) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		AttributeBean attribute = new AttributeBean( key, obj);
		try {
			sessionClient.setAttribute(sessionId, attribute);
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"set attribute:session ID:", sessionId,
						";attribute name:", key));
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}
	
	/**
	 * 刷新session主要是更新最后访问时间
	 * @param sessionId
	 */
	public void refreshSession(String sessionId){
		try {
			sessionClient.refresh(sessionId);
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"refresh session ID:", sessionId,
						";"));
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}
	
	public void setHttpSessionService(HttpSessionService httpSessionService) {
		HttpSessionService.httpSessionService = httpSessionService;
	}

	public void setSessionClient(SessionClient sessionClient) {
		this.sessionClient = sessionClient;
	}
	/**
	 * 获取最后访问时间
	 * @return
	 */
	public Long getLastAccessdTime(String sessionId) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"get lastAccessTime session ID:", sessionId,
						";"));
			}
			return sessionClient.lastAccessTime(sessionId);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
	}

	public Object getValue(String sessionId, String name) {
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		try {
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"get Value:session ID:", sessionId,
						";Value name:", name));
			}
			return sessionClient.getValue(sessionId, name);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return null;
		}
	}

	public void putValue(String sessionId, String name, Object value,Date time) {
		try {
			sessionClient.putValue(sessionId, name, value, time);
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"put Value:session ID:", sessionId,
						";attribute name:", name));
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	public void removeValue(String sessionId, String name) {
		if (name == null || name.trim().length() == 0) {
			return;
		}
		try {
			sessionClient.removeValue(sessionId, name);
			if (logger.isDebugEnabled()) {
				logger.debug(StringUtil.composeString(
						"remove Value:session ID:", sessionId,
						";Value name:", name));
			}

		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	
}
