package com.citrix.g2w.webdriver.pages.emails;

public enum EmailDuration {
        
    ONE_HOUR("ONE_HOUR"), ONE_DAY("ONE_DAY"), ONE_WEEK("ONE_WEEK");
    
    private String key;
    private EmailDuration(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }
 
}
