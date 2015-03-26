package com.citrix.g2w.webdriver;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Custom HtmlUnitDriver to override default features of htmlunitdriver
 * @author ankitag1
 *
 */
public class CustomHtmlUnitDriver extends HtmlUnitDriver {
    
    private boolean strictJSError;
    
    public CustomHtmlUnitDriver(boolean enableJS, boolean strictJSError) {
        super(enableJS);
        this.strictJSError = strictJSError;
    }
    
    /**
     * control test failures on js error
     */
    @Override
    protected WebClient modifyWebClient(WebClient client) {
        WebClient webClient =  super.modifyWebClient(client);
        webClient.getOptions().setThrowExceptionOnScriptError(this.strictJSError);
        return webClient;
    }

}
