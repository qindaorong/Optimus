package com.next.publish.util;

import com.next.publish.constants.FileConstants;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * FileUtil
 *
 * @author qindaorong
 * @create 2017-12-06 2:36 PM
 **/
public class FileUtil {
    

    private static boolean checkFile(String path){
        File file=new File(path);
        if(!file.exists()){
            return false;
        }
        return true;
    }
    

    private static void midifyFile(String path,Map<String,String> propertiesMap)throws IOException{
        List<String> list ;
        Path targetPath = Paths.get(path);
        try (Stream<String> streamLines = Files.lines(targetPath)) {
            list = streamLines.collect(Collectors.toList());
            
            for(int i = 0; i < list.size();i++ ){
                String line=replace(list.get(i),propertiesMap);
                list.set(i,line);
            }
    
            Stream<String> newStreamLines =list.parallelStream();
            Files.write(targetPath, (Iterable<String>)newStreamLines::iterator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private static String replace(String line ,Map<String,String> propertiesMap){
        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
            String searchText= entry.getKey();
            String replaceText= entry.getValue();
            line = line.replace(searchText, replaceText);
        }
        return line;
    }
    

    public static void process(Map<String,Object> fileMap){
        String path = String.valueOf(fileMap.get(FileConstants.FILE_PATH));
        Map<String,String> propertiesMap = (Map<String, String>) fileMap.get(FileConstants.FILE_PROPERTIES);
        
        boolean existBoolean = checkFile(path);
        
        if(existBoolean){
            try {
                midifyFile(path,propertiesMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
