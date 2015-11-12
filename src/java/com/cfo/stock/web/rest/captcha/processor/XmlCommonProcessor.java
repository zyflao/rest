package com.cfo.stock.web.rest.captcha.processor;


/**
 * 抽象类，实现加载和重新加载xml的操作
 *
 */
public abstract class XmlCommonProcessor {
	private String configPath; 

	protected boolean init(String path){
		boolean bool = false;
		configPath = path;
		if(parseXML(path)){
			bool = true;
		}else{
			bool = false;
		}
		return bool;
	}
	
	/**
	 * 不重启直接加载xml的方法
	 * @return
	 */
	protected boolean reset(){
		boolean bool = false;
		if(parseXML(configPath)){
			bool = true;
		}else{
			bool = false;
		}
		
		return bool;
	}
	
	/**
	 * 解析xml
	 * @param path
	 * @return
	 */
	protected abstract boolean parseXML(String path);
		
}
