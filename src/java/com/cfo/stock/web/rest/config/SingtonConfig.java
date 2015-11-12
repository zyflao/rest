/**
 * 
 */
package com.cfo.stock.web.rest.config;

import java.io.File;
import java.net.URL;

import org.apache.log4j.helpers.Loader;

/**  
 *   
 *   
 * @history  
 * <PRE>  
 * ---------------------------------------------------------  
 * VERSION       DATE            BY       CHANGE/COMMENT  
 * ---------------------------------------------------------  
 * 1.0           2012-5-17    yuanlong.wang     create  
 * ---------------------------------------------------------  
 * </PRE>  
 *  
 */

public abstract class SingtonConfig {
	protected static SingtonConfig m_instance;
	protected String PFILE;
	protected File m_file = null;
	protected long m_lastModifiedTime = 0;
	/**
	 * 初始化
	 * @param file
	 */
	public void init(String file){
		if (PFILE == null) {
			URL url = Loader.getResource(file);
			PFILE = url.getPath();
			PFILE = PFILE.replaceAll("%20", " ");
		}
		m_file = new File(PFILE);
		m_lastModifiedTime = m_file.lastModified();
		if (m_lastModifiedTime == 0) {
			System.err.println(PFILE + " file does not exist!");
		}
		try {
			load(new File(PFILE));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读取文件
	 * @param file
	 */
	protected abstract void load(File file); 
	
	/**
	 * 检查文件
	 */
	public void checkFile(){
		long newTime = m_file.lastModified();
		if (newTime == 0) {
			if (m_lastModifiedTime == 0) {
				System.err.println(PFILE + " file does not exist!");
			} else {
				System.err.println(PFILE + " file was deleted!!");
			}
		} else if (newTime > m_lastModifiedTime) {
			clear();
			try {
				load(new File(PFILE));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		m_lastModifiedTime = newTime;
	}
	/**
	 * 清除变量
	 */
	protected abstract void clear();
}
