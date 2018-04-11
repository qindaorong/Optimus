package com.next.websocketcenter.core.controller;

import com.next.optimus.common.dataresponse.ServerHttpResponse;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael on 12/14/2017.
 */
@RestController
@RequestMapping("/ws/external")
public class NextWebSocketExternalController {
    @RequestMapping(value="/concurrentTest"
        ,method = RequestMethod.GET)
    public ServerHttpResponse get(){
        return new ServerHttpResponse(HttpStatus.SC_OK);
    }
}
