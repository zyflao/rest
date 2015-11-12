package com.cfo.stock.web.rest.deprecated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.springframework.stereotype.Controller;

import com.cfo.stock.web.rest.OpenResult;
import com.cfo.stock.web.rest.captcha.processor.ImageCaptchaCreator;
import com.cfo.stock.web.rest.captcha.sigleton.CaptchaServiceSingleton;
import com.cfo.stock.web.rest.captcha.utils.InitData;
import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBean;
import com.cfo.stock.web.rest.result.ImgValidCodeResult;
import com.cfo.stock.web.rest.StockBaseRest;

@Path(StockBaseRest.baseuri + "/captcha")
@Controller
public class CaptchaOldRest extends StockBaseRest{
	
	@Path("/getImgcode")
	@GET
	@Produces("application/json;charset=utf-8")
	public String  getCaptcha(@Context HttpServletRequest request,
			@Context HttpServletResponse response){
		String url="";
		try{			
			VerifyCodeBean verifyCodeBean = InitData.getVerifyCodeBean(url);
			if(log.isDebugEnabled()){
				log.debug("used id:" + verifyCodeBean.getId() + " , userd typeName:" + verifyCodeBean.getName() + ", verifycode type:" + verifyCodeBean.getVerifyCodeEnum());
			}
			ImageCaptchaCreator verify = new ImageCaptchaCreator(CaptchaServiceSingleton.getInstance(verifyCodeBean),verifyCodeBean);
			ImgValidCodeResult result= verify.returnbyImage(request, response);
			return OpenResult.ok().add("data", result).buildJson();
		}catch(Exception e){
			log.error("获取图片验证码异常：",e);
			return OpenResult.serviceError(1, "captcha ERROR").buildJson();
		}
	}
}
