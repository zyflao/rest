package com.cfo.stock.web.rest.captcha.engine;

import java.util.Arrays;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.engine.image.ImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptchaFactory;

public abstract class CustomListImageCaptchaEngine extends ImageCaptchaEngine {

    public CustomListImageCaptchaEngine(){
    }

    protected abstract void buildInitialFactories();

    @SuppressWarnings("unchecked")
	public boolean addFactory(ImageCaptchaFactory factory)
    {
        return factory != null && factories.add(factory);
    }

    @SuppressWarnings("unchecked")
	public void addFactories(ImageCaptchaFactory factories[])
    {
        checkNotNullOrEmpty(factories);
        this.factories.addAll(Arrays.asList(factories));
    }

    protected void checkFactoriesSize()
    {
        if(factories.size() == 0)
            throw new CaptchaException("This gimpy has no factories. Please initialize it properly with the buildInitialFactory() called by the constructor or the addFactory() mehtod later!");
        else
            return;
    }
}
