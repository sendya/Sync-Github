package com.loacg.github;

import com.loacg.github.entity.Release;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Project: SyncGithub
 * Author: Sendya <18x@loacg.com>
 * Date: 11/22/2016 4:56 PM
 */
public class SyncDaemon {

    @Autowired
    RestTemplate restTemplate;

    private static Logger logger = LoggerFactory.getLogger(SyncDaemon.class);

    public void getList() {
        RestTemplate restTemplate = new RestTemplate();
        //String json = restTemplate.getForObject("https://api.github.com/repos/sendya/shadowsocks-panel/releases", String.class);
        //List releases = restTemplate.getForObject("https://api.github.com/repos/sendya/shadowsocks-panel/releases", List.class);
        ResponseEntity<Release[]> responseEntity = restTemplate.getForEntity("https://api.github.com/repos/sendya/shadowsocks-panel/releases", Release[].class);
        Release[] releases = responseEntity.getBody();
        MediaType contentType = responseEntity.getHeaders().getContentType();
        HttpStatus statusCode = responseEntity.getStatusCode();
        logger.info("Releases : {}", releases);
    }

    public static void main(String[] args) {
        SyncDaemon daemon = new SyncDaemon();
        daemon.getList();
    }
}
