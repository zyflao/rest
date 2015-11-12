package com.cfo.stock.web.rest.captcha.processor;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;

import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBean;
import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBeans;


/**
 * 验证码系统的xml解析规则
 */
public class VerifyCodeRuleSet extends RuleSetBase {

	@Override
	public void addRuleInstances(Digester digester) {
		digester.addObjectCreate("apps", VerifyCodeBeans.class);  
        digester.addObjectCreate("apps/app", VerifyCodeBean.class);  
        digester.addSetNext("apps/app", "addPermit");  
        digester.addBeanPropertySetter("apps/app/id","id"); 
        digester.addBeanPropertySetter("apps/app/name","name");  
        digester.addBeanPropertySetter("apps/app/url","url"); 
        digester.addBeanPropertySetter("apps/app/verifyCode","verifyCode"); 
        digester.addBeanPropertySetter("apps/app/params/text/wordDict","wordDict"); 
        digester.addBeanPropertySetter("apps/app/params/text/minLength","textMinLength"); 
        digester.addBeanPropertySetter("apps/app/params/text/maxLength","textMaxLength"); 
        digester.addBeanPropertySetter("apps/app/params/text/textColor","textColor"); 
        digester.addBeanPropertySetter("apps/app/params/background/width","bgWidth"); 
        digester.addBeanPropertySetter("apps/app/params/background/height","bgHeight"); 
        digester.addBeanPropertySetter("apps/app/params/background/backcolor","backcolor"); 
        digester.addBeanPropertySetter("apps/app/params/font/minSize","fontMinSize"); 
        digester.addBeanPropertySetter("apps/app/params/font/maxSize","fontMaxSize"); 
        digester.addBeanPropertySetter("apps/app/params/font/fonttype","fonttype");
	}

}
