package com.next.publish;


import com.next.publish.util.FileUtil;
import com.next.publish.util.XMLUtil;
import java.util.List;
import java.util.Map;

/**
 * PublicTools
 *
 * @author qindaorong
 * @create 2017-12-06 1:43 PM
 **/
public class PublicTools {
    public static void main(String[] args) {
        int length = args.length;
        
        if(length> 0 ){
            String xmlPath = args[0];
            List<Map<String,Object>> xmlList = XMLUtil.parserXml(xmlPath);
            
            for(Map<String,Object> fileMap : xmlList ){
                FileUtil.process(fileMap);
            }
        }

    }
}
