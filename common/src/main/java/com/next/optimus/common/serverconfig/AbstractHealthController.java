package com.next.optimus.common.serverconfig;


/**
 * AbstractHealthController
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public abstract class AbstractHealthController {
    
    /**
     * 健康检查通过，返回“0”
     */
    public static String HEALTH_CHECK_OK = "0";

    /**
     * 健康检查抽象类
     * @return
     */
    public abstract String healthCheck();
  
}
