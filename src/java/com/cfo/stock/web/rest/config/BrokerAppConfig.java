/**
 * 
 */
package com.cfo.stock.web.rest.config;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.result.BrokerEx;

public class BrokerAppConfig extends SingtonConfig{
	private static Map<String,Map<String,BrokerEx>>  appVersionMap=new HashMap<String, Map<String,BrokerEx>>();
	private static Map<String,String> miniVersionMap=new HashMap<String,String>();
	private static Map<String,String> brokerTipMap=new HashMap<String,String>();
	
	/**
	 * 实例化
	 */
	static {
		m_instance = new BrokerAppConfig();
		m_instance.init("./config/appconfig.xml");
	}
	/**
	 * 
	 */
	private BrokerAppConfig() {}
	@Override
	protected void load(File file) {
		try {
			if (!appVersionMap.isEmpty()) {
				appVersionMap.clear();
			}
			SAXReader reader = new SAXReader();
			Document doc = reader.read(file);
			Element templs = doc.getRootElement();
			Iterator<Element> i = templs.elementIterator("Apps");
			Element foo;
			while(i.hasNext()){
				foo=(Element)i.next();
				//
				String channelId =foo.attributeValue("channelId");
				Iterator<Element> app=foo.elementIterator("app");
				while(app.hasNext()){
					Element appElement=(Element)app.next();
					String appversion=appElement.attributeValue("version");
					List<Element> list=appElement.elements("Broker");
					String mapKey =createMapKey(channelId, appversion);
					Map<String,BrokerEx> brokerMap=appVersionMap.get(mapKey);
					if(brokerMap==null){
						brokerMap=new HashMap<String,BrokerEx>();
					}
					for(Element brokerEle:list){
						BrokerEx broker=new BrokerEx();
						String brokerId=brokerEle.attributeValue("id");
						broker.setBrokerId(brokerId);
						broker.setNewStr(brokerEle.attributeValue("newStr"));
						broker.setOrder(Integer.valueOf(brokerEle.attributeValue("order")));
						broker.setOpen(brokerEle.attributeValue("open"));
						broker.setTransfer(brokerEle.attributeValue("transfer"));
						broker.setBind(brokerEle.attributeValue("bind"));
						broker.setRecommend(Integer.valueOf(brokerEle.attributeValue("recommend")));
						broker.setBindOther(brokerEle.attributeValue("bindOther"));
						broker.setBindSelf(brokerEle.attributeValue("bindSelf"));
						Iterator<Element> business =brokerEle.elementIterator("business");
						while(business!=null&&business.hasNext()){
							Element b=business.next();
							String businessId=b.attributeValue("id");
							String miniVersion=b.attributeValue("miniversion");
							miniVersionMap.put(brokerId+"_"+businessId, miniVersion);
						}
						brokerMap.put(brokerId, broker);
					}
					appVersionMap.put(mapKey, brokerMap);
				}
			}
			
			Iterator<Element> brokers = templs.element("Brokers").elementIterator("Broker");
			while(brokers.hasNext()){
				foo=(Element)brokers.next();
				String broker =foo.attributeValue("id");
				brokerTipMap.put(broker,  foo.elementText("tip"));
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	private static String createMapKey(String channelId,String appversion){
		if(channelId==null)return "App_normal";
		return "App_"
				+ (channelId == null ? "" : channelId) + "_"
				+ (appversion == null ? "" : appversion);
	}
	/**
	 * 获取邮件模板
	 * @param type
	 * @return
	 */
	public static BrokerEx getBrokerEx(String broker,String channelId,String appversion,String business){
		m_instance.checkFile();
		if("appleStore".equals(channelId)){
			if(appversion==null){
				appversion="1.0.0";
			}
			String mapKey=createMapKey(channelId, appversion);
			Map<String,BrokerEx> map=appVersionMap.get(mapKey);
			if(map==null){
				mapKey=createMapKey(channelId, "higher");
				map=appVersionMap.get(mapKey);
			}
			if(map!=null){
				BrokerEx ex=map.get(broker);
				if(ex!=null){
					BrokerEx result=(BrokerEx) ex.clone();
					result.setTip(brokerTipMap.get(broker));
					return result;
				}
			}
		}else if("AppStorePro".equals(channelId)){
			if(appversion==null){
				appversion="2.3.0";
			}
			String mapKey=createMapKey(channelId, appversion);
			Map<String,BrokerEx> map=appVersionMap.get(mapKey);
			if(map==null){
				mapKey=createMapKey(channelId, "higher");
				map=appVersionMap.get(mapKey);
			}
			if(map!=null){
				BrokerEx ex=map.get(broker);
				if(ex!=null){
					BrokerEx result=(BrokerEx) ex.clone();
					result.setTip(brokerTipMap.get(broker));
					return result;
				}
			}
		}else {
			Map<String,BrokerEx> map=appVersionMap.get("App_normal");
			if(map!=null){
				BrokerEx ex=map.get(broker);
				if(ex!=null){
					BrokerEx result=(BrokerEx) ex.clone();
					result.setTip(brokerTipMap.get(broker));
					String miniVer=miniVersionMap.get(broker+"_"+business);
					if(appversion==null||miniVer==null)return result;
					if(appversion!=null&&compareVersion(appversion, miniVer)>0){
						return result;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
	 * @param version1
	 * @param version2
	 * @return
	 */
	public static int compareVersion(String version1, String version2)  {
		if (version1 == null || version2 == null) {
			throw new RuntimeException("compareVersion error:illegal params.");
		}
		String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
		String[] versionArray2 = version2.split("\\.");
		int idx = 0;
		int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
		int diff = 0;
		while (idx < minLength
				&& (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
				&& (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
			++idx;
		}
		//如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
		diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
		return diff;
	}
	
	@Override
	protected void clear() {
		appVersionMap.clear();
	}
	public static void main(String[] args) {
		System.out.println(JSONObject.toJSONString(getBrokerEx("HTZQ", "AppStorePro", "", "ITOUGU")));
		/*System.out.println(JSONObject.toJSONString(getBrokerEx("ZXZQ", "appleStore", "1.1.0", "ITOUGU")));
		System.out.println(JSONObject.toJSONString(getBrokerEx("HTZQ", "appleStore", "1.0.5", "ITOUGU")));
		System.out.println(JSONObject.toJSONString(getBrokerEx("ZJZQ", "", "1.1.0", "ITOUGU")));*/
		System.out.println(JSONObject.toJSONString(appVersionMap.get("AppStorePro")));
	}
}
