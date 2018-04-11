package com.next.optimus.common.util.httputil;

import java.util.Map;
import java.util.Set;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Michael on 12/11/2017.
 * @author Michael
 */
public class CacheServiceClient {
    public Map<String,Set<String>> getBlackOrWhiteListFromCache(String url) {
        String result = HttpClientUtil.sendGetRequest(url);
        Map<String,Set<String>> map = null;
        if(StringUtils.isNotBlank(result)){
            map = (Map<String,Set<String>>) JSONObject
                .toBean(JSONObject.fromObject(result), Map.class);
        }
        return map;
    }
}
