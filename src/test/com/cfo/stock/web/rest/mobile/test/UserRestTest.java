package com.cfo.stock.web.rest.mobile.test;

import javax.ws.rs.core.HttpHeaders;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cfo.stock.web.rest.UserRest;


public class UserRestTest extends BaseMobileTest {
	
	@Autowired
	private UserRest userRest;
	
	@Test
	public void test_Login() {
//		{'loginname':'13811887954','passwd':'123456','nametype':'3','apps':'stockrest'}
		HttpHeaders headers = null;
//		String content = "{\"loginname\":\"liuyi3\",\"passwd\":\"liujing21\"}";
		String content = "{\"loginname\":\"13811887954\",\"passwd\":\"123456\",\"loginname\":\"13811887954\",\"passwd\":\"123456\"}";
		userRest.login(headers, content);
	}
	
	@Test
	public void test_GetCode() {
//		{'loginname':'13811887954','passwd':'123456','nametype':'3','apps':'stockrest'}
		HttpHeaders headers = null;
//		String content = "{\"loginname\":\"liuyi3\",\"passwd\":\"liujing21\"}";
		String content = "{'mobileno':'18500331300','codetype':'201'}";
		userRest.getCode(headers, content);
	}
}
