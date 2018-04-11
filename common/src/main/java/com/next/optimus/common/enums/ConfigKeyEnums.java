package com.next.optimus.common.enums;

/**
 * Created by Michael on 12/12/2017.
 */
public enum ConfigKeyEnums {
    CACHE_SERVICE_URL_KEY("authKeyListUrl"),
    INTERVAL_COUNT("intervalCount"),
    INTERVAL("interval")
    ;
    
    private String key;
    
    ConfigKeyEnums(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return key;
    }
}
