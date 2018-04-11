package com.next.optimus.test.util;

import java.io.IOException;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * FileUtil
 *
 * @author qindaorong
 * @create 2017-12-06 11:29 AM
 **/
public class XMLUtil {
    public static void parserXml(String fileName) {
        SAXBuilder builder = new SAXBuilder(false);
        try {
            Document document = builder.build(fileName);
            Element employees = document.getRootElement();
            List employeeList = employees.getChildren("file");
            
            for(int i = 0; i < employeeList.size();i++){
                Element employee = (Element) employeeList.get(i);
    
                String path = employee.getAttribute("path").getValue();
                System.out.println("<file> path is ["+path+"]");
                
                List<Element> employeeInfo = employee.getChildren("properties");
                
                for(Element properties : employeeInfo){
                    String propertiesValue =  properties.getValue();
                    String replaceKey =  properties.getAttribute("name").getValue();
    
    
                    System.out.println("propertiesValue is ["+propertiesValue+"]");
                    System.out.println("replaceKey is ["+replaceKey+"]");
                }
    
                System.out.println("************************************************");
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) throws Exception {
        String path="C:\\Users\\QDR\\git\\Optimus\\eureka\\project_server.xml";
        XMLUtil.parserXml(path);
    }
}
