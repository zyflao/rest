/**
 * 
 */
package com.cfo.stock.web.rest.session;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;



public class AttributeBean<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private T value;
	private long t;
	public AttributeBean() {
	}
	public AttributeBean( String key, T value) {
		if(key!=null){
			this.key = key;	
		}else{
			this.key="";
		}
		this.value = value;
		this.t=System.currentTimeMillis();
	}
	@JSONField(serialize = false) //不序列化到memcache
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	public long getT() {
		return t;
	}
	public void setT(long t) {
		this.t = t;
	}


	
}
