package com.next.optimus.messagecenterservice.api.dto;

import java.util.List;
import org.hibernate.validator.constraints.NotBlank;

/**
 * KafkaDTO
 *
 * @author qindaorong
 * @date 2017/10/16
 */
public class KafkaDTO {
    
    @NotBlank
    private String topicId;
    @NotBlank
    private String content;

    private List<String> stringList ;

    public String getTopicId() {
        return topicId;
    }
    
    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    
    public List<String> getStringList() {
        return stringList;
    }
    
    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
