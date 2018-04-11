package com.next.kafkaconsumer.core.elasticsearch.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author : Donald
 * @description :
 * @date : 2017/9/28 10:18.
 */
public class IndexDTO {
    
    @NotEmpty
    private String indexName;
    
    private String type;
    
    @NotNull
    private List<String> mapping;
    
    public List<String> getMapping() {
        return mapping;
    }
    
    public void setMapping(List<String> mapping) {
        this.mapping = mapping;
    }
    
    public String getIndexName() {
        return indexName;
    }
    
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
