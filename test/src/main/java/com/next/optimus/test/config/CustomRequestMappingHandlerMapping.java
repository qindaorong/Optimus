package com.next.optimus.test.config;

import java.lang.reflect.Method;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * CustomRequestMappingHandlerMapping
 *
 * @author qindaorong
 * @create 2017-11-16 10:19 AM
 **/
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    
    /**
     * 获得类和方法级别的类注解
     * @param handlerType
     * @return
     */
    @Override
    protected RequestCondition<ApiVesrsionCondition> getCustomTypeCondition(Class<?> handlerType) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        return createCondition(apiVersion);
    }
    
    /**
     * 获得注解对应处理方法
     * @param method
     * @return
     */
    @Override
    protected RequestCondition<ApiVesrsionCondition> getCustomMethodCondition(Method method) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createCondition(apiVersion);
    }
    
    /**
     *
     * @param apiVersion
     * @return
     */
    private RequestCondition<ApiVesrsionCondition> createCondition(ApiVersion apiVersion) {
        return apiVersion == null ? null : new ApiVesrsionCondition(apiVersion.value());
    }
}
