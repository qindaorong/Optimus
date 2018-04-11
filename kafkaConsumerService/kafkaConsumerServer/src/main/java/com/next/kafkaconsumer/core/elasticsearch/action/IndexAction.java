package com.next.kafkaconsumer.core.elasticsearch.action;

import com.next.kafkaconsumer.core.elasticsearch.dto.IndexDTO;
import com.next.kafkaconsumer.core.elasticsearch.service.ElasticSearchTemplate;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : Donald
 * @description :
 * @date : 2017/9/28 9:45.
 */
@Controller
@RequestMapping("/api")
public class IndexAction {

    private Logger log = LoggerFactory.getLogger(IndexAction.class);

    @Autowired
    private ElasticSearchTemplate elasticSearchTemplate;

    @PutMapping("/index")
    public ResponseEntity<Void> createIndex( @Valid @RequestBody IndexDTO indexDTO) {
        if (StringUtils.isEmpty(indexDTO.getType())) {
            throw new IllegalArgumentException("Index type is empty.");
        }
        log.info("Rest : Request to create an index. Mapping :  {}.", indexDTO.toString());

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @DeleteMapping("/index")
    public ResponseEntity<Void> deleteIndex(@RequestParam String indexName) {
        log.debug("Rest : Request to delete an index.");
        elasticSearchTemplate.deleteIndex(indexName);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/index/recovery")
    public ResponseEntity<Void> recoveryIndex(@RequestParam String indexName) {

        log.debug("Rest : Request to recovery an index.");
        elasticSearchTemplate.recoveryIndex(indexName);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/index")
    public ResponseEntity<Void> updateIndex(@Valid  @RequestBody List<String> mapping) {

        log.debug("Rest : Request to update an index. Mapping :  {}.", mapping);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/index")
    public ResponseEntity<Void> getIndex() {

        log.debug("Rest : Request to get an index.");

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/index/close")
    public ResponseEntity<Void> closeIndex(@RequestParam String indexName) {

        log.debug("Rest : Request to close an index.");
        elasticSearchTemplate.closeIndex(indexName);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @GetMapping("/index/open")
    public ResponseEntity<Void> openIndex(@RequestParam String indexName) {

        log.debug("Rest : Request to open an index.");
        elasticSearchTemplate.openIndex(indexName);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/index/clearCache")
    public ResponseEntity<Void> clearCacheIndex(@RequestParam String indexName) {

        log.debug("Rest : Request to close an index.");
        elasticSearchTemplate.clearCacheIndex(indexName);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/index/defaultSetting")
    public ResponseEntity<Void> defaultSetting(@RequestParam String indexName) {

        log.debug("Rest : Request to defaultSetting an index.");
        elasticSearchTemplate.defaultSetting(indexName);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/index/setting")
    public ResponseEntity<Void> setting(@RequestParam String indexName) {

        log.debug("Rest : Request to defaultSetting an index.");
        elasticSearchTemplate.getSetting(indexName);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }




}
