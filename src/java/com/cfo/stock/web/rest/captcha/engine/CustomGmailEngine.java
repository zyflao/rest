package com.cfo.stock.web.rest.captcha.engine;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBean;
import com.jhlabs.image.PinchFilter;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformationByBufferedImageOp;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.GlyphsPaster;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.GlyphsVisitors;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.OverlapGlyphsUsingShapeVisitor;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.TranslateAllToRandomPointVisitor;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.TranslateGlyphsVerticalRandomVisitor;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.word.FileDictionary;
import com.octo.captcha.component.word.wordgenerator.ComposeDictionaryWordGenerator;
import com.octo.captcha.image.gimpy.GimpyFactory;

/**
 * 自定义gamil的验证码处理类
 * 
 */
public class CustomGmailEngine extends CustomListImageCaptchaEngine {
	private VerifyCodeBean verifyCodeBean;
	
	public CustomGmailEngine(VerifyCodeBean verifyCodeBean) {
		this.verifyCodeBean = verifyCodeBean;
        buildInitialFactories();
        checkFactoriesSize();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void buildInitialFactories() {
		com.octo.captcha.component.word.wordgenerator.WordGenerator dictionnaryWords = new ComposeDictionaryWordGenerator(
				new FileDictionary("toddlist"));
		com.octo.captcha.component.image.textpaster.TextPaster randomPaster = new GlyphsPaster(
				verifyCodeBean.getTextMinLength(), verifyCodeBean.getTextMaxLength(),
				new RandomListColorGenerator(new Color[] {
						new Color(23, 170, 27), new Color(220, 34, 11),
						new Color(23, 67, 172) }), new GlyphsVisitors[] {
						new TranslateGlyphsVerticalRandomVisitor(1.0D),
						new OverlapGlyphsUsingShapeVisitor(3D),
						new TranslateAllToRandomPointVisitor() });
		com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator back = new UniColorBackgroundGenerator(
				verifyCodeBean.getBgWidth(), verifyCodeBean.getBgHeight(), Color.white);
		com.octo.captcha.component.image.fontgenerator.FontGenerator shearedFont = new RandomFontGenerator(
				verifyCodeBean.getFontMinSize(), verifyCodeBean.getFontMaxSize(), new Font[] {
						new Font("nyala", 1, 50), new Font("Bell MT", 0, 50),
						new Font("Credit valley", 1, 50) }, false);
		PinchFilter pinch = new PinchFilter();
		pinch.setAmount(-0.5F);
		pinch.setRadius(70F);
		pinch.setAngle(0.1963495F);
		pinch.setCentreX(0.5F);
		pinch.setCentreY(-0.01F);
		pinch.setEdgeAction(1);
		PinchFilter pinch2 = new PinchFilter();
		pinch2.setAmount(-0.6F);
		pinch2.setRadius(70F);
		pinch2.setAngle(0.1963495F);
		pinch2.setCentreX(0.3F);
		pinch2.setCentreY(1.01F);
		pinch2.setEdgeAction(1);
		PinchFilter pinch3 = new PinchFilter();
		pinch3.setAmount(-0.6F);
		pinch3.setRadius(70F);
		pinch3.setAngle(0.1963495F);
		pinch3.setCentreX(0.8F);
		pinch3.setCentreY(-0.01F);
		pinch3.setEdgeAction(1);
		java.util.List textDef = new ArrayList();
		textDef.add(new ImageDeformationByBufferedImageOp(pinch));
		textDef.add(new ImageDeformationByBufferedImageOp(pinch2));
		textDef.add(new ImageDeformationByBufferedImageOp(pinch3));
		com.octo.captcha.component.image.wordtoimage.WordToImage word2image = new DeformedComposedWordToImage(
				false, shearedFont, back, randomPaster, new ArrayList(),
				new ArrayList(), textDef);
		addFactory(new GimpyFactory(dictionnaryWords, word2image, false));
	}
}
