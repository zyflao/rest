package com.cfo.stock.web.rest.captcha.engine;

import java.awt.Color;
import java.awt.Font;

import com.cfo.stock.web.rest.captcha.vo.VerifyCodeBean;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.image.gimpy.GimpyFactory;

/**
 * 自定义数字的验证码处理类
 *
 */
@SuppressWarnings("deprecation")
public class CustomNumberEngine extends CustomListImageCaptchaEngine {
	private VerifyCodeBean verifyCodeBean;
	
	public CustomNumberEngine(VerifyCodeBean verifyCodeBean) {
		this.verifyCodeBean = verifyCodeBean;
        buildInitialFactories();
        checkFactoriesSize();
	}
	
	@Override
	protected void buildInitialFactories() {
		WordGenerator wordGenerator = new RandomWordGenerator(verifyCodeBean.getWordDict());   
        RandomRangeColorGenerator cgen = new RandomRangeColorGenerator(
             new int[] {0, 100},
             new int[] {0, 100},
             new int[] {0, 100});
        TextPaster textPaster = new RandomTextPaster(verifyCodeBean.getTextMinLength(), verifyCodeBean.getTextMaxLength(), cgen, true);
        BackgroundGenerator backgroundGenerator = new UniColorBackgroundGenerator(verifyCodeBean.getBgWidth(), verifyCodeBean.getBgHeight(), Color.WHITE);
        Font[] fontsList = new Font[] {
            new Font("Arial", 0, 10),
        };
        FontGenerator fontGenerator = new RandomFontGenerator(verifyCodeBean.getFontMinSize(), verifyCodeBean.getFontMaxSize(), fontsList);
        WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);
        addFactory(new GimpyFactory(wordGenerator, wordToImage));
	}
	
}
