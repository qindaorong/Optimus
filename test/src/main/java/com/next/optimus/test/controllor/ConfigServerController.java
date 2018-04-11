package com.next.optimus.test.controllor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConfigServerController
 *
 * @author qindaorong
 * @create 2017-12-11 11:44 AM
 **/
@RestController
// 使用该注解的类，会在接到SpringCloud配置中心配置刷新的时候，自动将新的配置更新到该类对应的字段中。
@RefreshScope
public class ConfigServerController {
    
    @Value("${file.fileName}")
    private String fileName;

    @RequestMapping("/fileName")
    public String fileName() {
        return this.fileName;
    }
    
    
}
