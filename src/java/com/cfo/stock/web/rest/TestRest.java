package com.cfo.stock.web.rest;

//import java.util.List;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.BasicConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.cfo.stock.web.rest.logger.Ioc9Loggor;
import com.cfo.stock.web.rest.logger.MobileLoggor;
import com.cfo.stock.web.rest.utils.CountByLogAndMemcacheUtil;
import com.jrj.common.cache.memcached.client.MemCachedClient;

@Controller
@Path(StockBaseRest.baseuri+"/test")
public class TestRest {
	
//	@Autowired
//	private DemoService demoService;
//	@Autowired
//	private MemCachedClient memCachedClient;
	/** 测试链接
	 * @param uri
	 * @return
	 */
	@GET
	@Path("/")
	@Produces("application/json;charset=utf-8")
	public String test(
			@Context UriInfo uri) {
		JSONObject json=new JSONObject();
		json.put("action", "test");
		return json.toJSONString();
	}
	
//	@GET
//	@Path("/dubbotest")
//	@Produces("application/json;charset=utf-8")
//	public String testd(@Context String content){
//		String hello = demoService.sayHello("tom");
//		System.out.println(hello);
//		JSONObject json = new JSONObject();
//		@SuppressWarnings("unchecked")
//		List<User> list = demoService.getUsers();
//		if (list != null && list.size() > 0) {
//			for (int i = 0; i < list.size(); i++) {
//				json.put(list.get(i).getName(),list.get(i).getAge());
//			}
//		}
//		return json.toJSONString();
//	}
	public static ApplicationContext context =null;
	private MemCachedClient memCachedClient;
	public static void main(String[] args) {
//		String brokerId = "HTZQ";
//		boolean flag = isSureBroker(brokerId);
//		System.out.println(flag);
	
//		Date date = new Date();
//		String ss = DateUtil.format(date, DateUtil.DateFormat_yyyyMMdd);
		double  l = 0.11;
		DecimalFormat df = new DecimalFormat("#.00");
		BigDecimal  dd = new  BigDecimal(l);
//		dd = BigDecimal.ZERO;
//		dd.setScale(2, BigDecimal.ROUND_HALF_UP);
//		df.format(dd);
		System.out.println(df.format(dd));
		int d = dd.compareTo(BigDecimal.ZERO);
		System.out.println(d);
		BasicConfigurator.configure();  
	    MobileLoggor.info("Hello World, This is an information message.");  
		Ioc9Loggor.error("Hello World, This is a error message.");  
		Ioc9Loggor.info("Hello World, This is a fatal message.");  
       // System.exit(0);  */
        
    	context = new ClassPathXmlApplicationContext("classpath*:spring/spring-app.xml");
    	MemCachedClient memCachedClient = (MemCachedClient) context.getBean("memCachedClient");
    	CountByLogAndMemcacheUtil countByLogAndMemcacheUtil = (CountByLogAndMemcacheUtil) context.getBean("countByLogAndMemcacheUtil");
    	Date date =new Date(60*1000);
    	System.out.println(date);
    	/*memCachedClient.delete("PAZQIOS9REST");
    	memCachedClient.delete("PAZQANDROIDREST");
    	memCachedClient.delete("PAZQIOSREST");
    	memCachedClient.delete("CJZQIOS9REST");
    	memCachedClient.delete("CJZQIOSREST");
    	memCachedClient.delete("CJZQANDROIDREST");*/
    	//memCachedClient.get("IOS9REST");
    	//memCachedClient.add("IOS9REST","1",0);IOS,9.0,CJZQ
    countByLogAndMemcacheUtil.JudgeBrokerId("IOS","9.0","CJZQ");
    	//memCachedClient.incr("IOS9REST", 1);
//    	memCachedClient.add("IOS9REST","1",date);
    	System.out.println(memCachedClient.get("PAZQIOS9REST"));
    	System.out.println(memCachedClient.get("PAZQANDROIDREST"));
    	System.out.println(memCachedClient.get("PAZQIOSREST"));
    	System.out.println(memCachedClient.get("CJZQIOS9REST"));
    	 
    	System.out.println(memCachedClient.get("CJZQIOSREST"));
    	System.out.println(memCachedClient.get("CJZQANDROIDREST"));
//		double d = 3.1465926;
//		String result = String.format("%.2f", d);
//		System.out.println(result); 
    	
    	
    	
    	
    	
    	
		
	}
}
