package com.next.optimus.common.util;

import com.next.optimus.common.common.CommonConstants;
import com.next.optimus.common.common.MessageManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Michael on 12/12/2017.
 * @author Michael
 */
public class ConfigurationManager extends MessageManager {
    
    private Properties configInfo;
    
    private ConfigurationManager(){
        readConfig();
    }
    
    private static class RateLimitManagerHolder{
        private static ConfigurationManager instance = new ConfigurationManager();
    }
    
    public static ConfigurationManager getInstance(){
        return RateLimitManagerHolder.instance;
    }
    
    public Properties getConfigInfo() {
        return configInfo;
    }
    
    public void setConfigInfo(Properties configInfo) {
        this.configInfo = configInfo;
    }
    
    private void readConfig(){
        configInfo = new Properties();
        InputStream in = loadProperties(CommonConstants.CACHE_SERVICE_URL_PROPERTIES);
        try {
            configInfo.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
