package com.next.backend.websocketcenter;

import com.next.websocketcenter.api.WebSocketClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * MessageCenterRest
 *
 * @author qindaorong
 * @date 2017/10/16
 */
@RestController
public class WebsocketRest {
    
    WebSocketClient webSocketClient = new WebSocketClient();
    
}
