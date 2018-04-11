package com.next.publish.util;

import com.next.publish.constants.FileConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


/**
 * XMLUtil
 *
 * @author qindaorong
 * @create 2017-12-06 1:56 PM
 **/
public class XMLUtil {
    
    private static final String attributeName ="path";
    private static final String properties_attributeName ="name";
    

    public static List<Map<String,Object>> parserXml(String fileName) {
        SAXBuilder builder = new SAXBuilder(false);
    

        List<Map<String,Object>> xmlList = new ArrayList<Map<String,Object>>();
        
        try {
            Document document = builder.build(fileName);
            Element employees = document.getRootElement();
            List employeeList = employees.getChildren(FileConstants.FILE_ROOT_ELEMENT);
            
            for(int i = 0; i < employeeList.size();i++){
                Element employee = (Element) employeeList.get(i);
                String filePath = employee.getAttribute(FileConstants.FILE_PATH).getValue();


                Map<String,Object> fileMap = new HashMap<String,Object>(2);

                List<Element> employeeInfo = employee.getChildren(FileConstants.FILE_PROPERTIES);
    

                Map<String,String> propertiesMap = new HashMap<String,String>(employeeInfo.size());
                
                if(employeeInfo.size()> 0 ){
                    for(Element properties : employeeInfo){
                        String propertiesValue =  properties.getValue();
                        String replaceKey =  properties.getAttribute(properties_attributeName).getValue();
                        propertiesMap.put(replaceKey,propertiesValue);
                    }
                }
                
                fileMap.put(FileConstants.FILE_PATH,filePath);
                fileMap.put(FileConstants.FILE_PROPERTIES,propertiesMap);

                xmlList.add(fileMap);

            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return xmlList;
    }
}
