package com.cfo.stock.web.rest.mobile.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import test.NewSpringJunitClassRunner;


@RunWith(NewSpringJunitClassRunner.class)
@ContextConfiguration(locations = "/spring/spring-app.xml")
public abstract class BaseMobileTest {
	protected Log log = LogFactory.getLog(getClass());
}
