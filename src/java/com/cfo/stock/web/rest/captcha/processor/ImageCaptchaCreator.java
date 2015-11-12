package com.cfo.stock.web.rest.captcha.processor;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

//import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

import com.cfo.stock.web.rest.captcha.enums.CaptchaType;
import com.cfo.stock.web.rest.captcha.service.CustomImageCaptchaService;
import com.cfo.stock.web.rest.captcha.sigleton.CaptchaMemcacheSigleton;
import com.cfo.stock.web.rest.captcha.utils.Constant;
import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBean;
import com.cfo.stock.web.rest.result.ImgValidCodeResult;
import com.jrj.common.utils.PropertyManager;
import com.octo.captcha.Captcha;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 创建验证码
 * 
 */
public class ImageCaptchaCreator {
	private static Logger log = Logger.getLogger("VerifycodeLog");
	private CustomImageCaptchaService service;
	private VerifyCodeBean verifyCodeBean;

	public ImageCaptchaCreator(CustomImageCaptchaService service,
			VerifyCodeBean verifyCodeBean) {
		this.service = service;
		this.verifyCodeBean = verifyCodeBean;
	}

	/**
	 * 返回验证码图片
	 * 
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public ImgValidCodeResult returnbyImage(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		ServletOutputStream out = null;
		ByteArrayOutputStream bs = null;
		String imgstr = null;
		ImgValidCodeResult result = null;
		try {
			out = response.getOutputStream();
			// ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (service != null) {
				setResponsePara(response);
				// create uuid
				String uid = UUID.randomUUID().toString();
				CaptchaType type = verifyCodeBean.getVerifyCodeEnum();
				uid = uid + "-" + type.ordinal();
				log.info("created uid:" + uid);
				// return cookie
				// Added by Cao.Qiang
				// Add date：2012-05-22
				// 修改验证码功能，使验证码cookie的域名支持多个
				String sysid = request.getParameter("sysid");
				response.addCookie(addCookies(uid, sysid));
				Captcha captcha = service.getCaptchaForID(uid);
				// create the image with the text
				BufferedImage bi = service.getImageChallengeForID(captcha);
				// save image to tt
				saveVerifyCodeToTT(uid, captcha);
				log.info("验证码生成成功！");
				// write the data out
				out.flush();
//				ImageIO.write(bi, "jpg", out);

				bs = new ByteArrayOutputStream();
				JPEGImageEncoder imgEncoder = com.sun.image.codec.jpeg.JPEGCodec
						.createJPEGEncoder(bs);
				imgEncoder.encode(bi);
				bs.flush();
				byte[] bytes = bs.toByteArray();

				BASE64Encoder encoder = new BASE64Encoder();
				imgstr = encoder.encode(bytes);	
				result = new ImgValidCodeResult();
				result.setImgStr(imgstr);
				result.setUuId(uid);
//				log.info("imgstr"+imgstr);
			} else {
				log.info("验证码生成失败！");
			}
		} catch (IOException e){
			throw new IOException();	
		} catch (Exception e){
			throw new Exception();
		} finally {
			if(out != null){
				out.close();
			}
			if(bs!=null){
				bs.close();
			}
		}
		return result;
	}

	/**
	 * 将验证码和其ID一起存入TT
	 * 
	 * @param uuid
	 * @param captcha
	 */
	private void saveVerifyCodeToTT(String uuid, Captcha captcha) {
		CaptchaMemcacheSigleton.getInstance().add(
				Constant.SESSION_VERIFYCODE + uuid, captcha,
				new Date(System.currentTimeMillis() + 10 * 60 * 1000));
	}

	/**
	 * 将验证码ID加入到cookie中
	 * 
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unused")
	private Cookie addCookies(String uid) {
		Cookie cookie = new Cookie(Constant.VERIFYCODE_ID, uid);
		cookie.setPath("/");
		cookie.setDomain(PropertyManager.getString(Constant.COOKIE_DOMAIN));
		return cookie;
	}

	private Cookie addCookies(String uid, String sysid) {
		String key = "";
		if (StringUtils.isBlank(sysid)) {
			key = Constant.COOKIE_DOMAIN;
		} else {
			key = Constant.COOKIE_DOMAIN + "." + sysid;
		}

		Cookie cookie = new Cookie(Constant.VERIFYCODE_ID, uid);
		cookie.setPath("/");
		cookie.setDomain(PropertyManager.getString(key));
		return cookie;
	}

	private void setResponsePara(HttpServletResponse response) {
		// Set to expire far in the past.
		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
//		response.setContentType("image/jpeg");
		response.setContentType("application/json");
	}

}
