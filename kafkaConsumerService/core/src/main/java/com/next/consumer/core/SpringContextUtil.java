package com.next.consumer.core;

import com.next.optimus.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringContextUtil
 *
 * @author qindaorong
 * @create 2017-10-16 3:29 PM
 **/
@Component
public class SpringContextUtil implements ApplicationContextAware {
    
    private static Logger logger = LoggerFactory.getLogger(SpringContextUtil.class);
    /**
     * Spring应用上下文环境
     */
    private static ApplicationContext applicationContext;
    
    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }
    
    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    /**
     * 获取对象
     * 这里重写了bean方法，起主要作用
     * @param name
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
    
        Object object = null;
        try {
            if (StringUtil.isEmpty(name)) {
                throw new IllegalArgumentException("className cannot be empty.");
            }
        
            object = applicationContext.getBean(Class.forName(name));
        } catch (ClassNotFoundException e) {
            logger.error("Cannot found Class : {}.{}", name, e.getMessage());
        }
        return object;
    }
}
