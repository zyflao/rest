// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   HotmailEngine.java

package com.cfo.stock.web.rest.captcha.engine;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBean;
import com.jhlabs.image.SwimFilter;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.SingleColorGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformationByBufferedImageOp;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.GlyphsPaster;
import com.octo.captcha.component.image.textpaster.glyphsdecorator.GlyphsDecorator;
import com.octo.captcha.component.image.textpaster.glyphsdecorator.RandomLinesGlyphsDecorator;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.GlyphsVisitors;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.HorizontalSpaceGlyphsVisitor;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.RotateGlyphsRandomVisitor;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.ShearGlyphsRandomVisitor;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.TranslateAllToRandomPointVisitor;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.TranslateGlyphsVerticalRandomVisitor;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.image.gimpy.GimpyFactory;

/**
 * 自定义的hotmail验证码处理类
 *
 */
public class CustomHotmailEngine extends CustomListImageCaptchaEngine {
	private VerifyCodeBean verifyCodeBean;
	
	public CustomHotmailEngine(VerifyCodeBean verifyCodeBean) {
		this.verifyCodeBean = verifyCodeBean;
        buildInitialFactories();
        checkFactoriesSize();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void buildInitialFactories() {
		com.octo.captcha.component.word.wordgenerator.WordGenerator dictionnaryWords = new RandomWordGenerator(verifyCodeBean.getWordDict());
		com.octo.captcha.component.image.textpaster.TextPaster randomPaster = new GlyphsPaster(verifyCodeBean.getTextMinLength(), verifyCodeBean.getTextMaxLength(), new SingleColorGenerator(new Color(0, 0, 80)), new GlyphsVisitors[] {
			new TranslateGlyphsVerticalRandomVisitor(5D), new RotateGlyphsRandomVisitor(0.098174770424681035D), new ShearGlyphsRandomVisitor(0.20000000000000001D, 0.20000000000000001D), new HorizontalSpaceGlyphsVisitor(4), new TranslateAllToRandomPointVisitor()
		}, new GlyphsDecorator[] {
			new RandomLinesGlyphsDecorator(1.2D, new SingleColorGenerator(new Color(0, 0, 80)), 2D, 25D), new RandomLinesGlyphsDecorator(1.0D, new SingleColorGenerator(new Color(238, 238, 238)), 1.0D, 25D)
		});
		com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator back = new UniColorBackgroundGenerator(verifyCodeBean.getBgWidth(), verifyCodeBean.getBgHeight(), new Color(238, 238, 238));
		com.octo.captcha.component.image.fontgenerator.FontGenerator shearedFont = new RandomFontGenerator(verifyCodeBean.getFontMinSize(), verifyCodeBean.getFontMaxSize(), new Font[] {
			new Font("Caslon", 1, 30)
		}, false);
		SwimFilter swim = new SwimFilter();
		swim.setScale(30F);
		swim.setStretch(1.0F);
		swim.setTurbulence(1.0F);
		swim.setAmount(2.0F);
		swim.setTime(0.0F);
		swim.setEdgeAction(1);
		java.util.List def = new ArrayList();
		def.add(new ImageDeformationByBufferedImageOp(swim));
		com.octo.captcha.component.image.wordtoimage.WordToImage word2image = new DeformedComposedWordToImage(false, shearedFont, back, randomPaster, new ArrayList(), new ArrayList(), def);
		addFactory(new GimpyFactory(dictionnaryWords, word2image, false));
	}
}