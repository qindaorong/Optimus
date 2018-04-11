package com.next.websocketcenter.core.controller;

import com.next.optimus.common.serverconfig.AbstractHealthController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * HeathCheckController
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@Controller
public class HeathCheckController extends AbstractHealthController {
    
    @RequestMapping("/healthCheck")
    @ResponseBody
    @Override
    public String healthCheck() {
        return HeathCheckController.HEALTH_CHECK_OK;
    }
    
}
