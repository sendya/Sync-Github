package com.loacg.github;

import com.loacg.github.entity.Release;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Project: SyncGithub
 * Author: Sendya <18x@loacg.com>
 * Date: 11/22/2016 4:56 PM
 */
@Component
public class SyncDaemon {

    @Autowired
    RestTemplate restTemplate;

    private static Logger logger = LoggerFactory.getLogger(SyncDaemon.class);

    /**
     * get repo releases
     *
     * @param owner
     * @param repo
     * @return
     */
    public Release[] getList(String owner, String repo) {
        ResponseEntity<Release[]> responseEntity = restTemplate.getForEntity("https://api.github.com/repos/{owner}/{repo}/releases", Release[].class, owner, repo);
        Release[] releases = responseEntity.getBody();
        MediaType contentType = responseEntity.getHeaders().getContentType();
        HttpStatus statusCode = responseEntity.getStatusCode();
        return releases;
    }

}
