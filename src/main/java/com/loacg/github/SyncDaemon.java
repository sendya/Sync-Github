package com.loacg.github;

import com.loacg.bootstrap.AppEnv;
import com.loacg.dao.OwnerDao;
import com.loacg.entity.FileDown;
import com.loacg.entity.Owner;
import com.loacg.github.entity.Asset;
import com.loacg.github.entity.Release;
import com.loacg.utils.Downloads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
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

    @Autowired
    OwnerDao ownerDao;

    @Autowired
    Downloads downloads;

    @Autowired
    AppEnv appEnv;

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

    public void sync() {
        ResponseEntity<String> entity = restTemplate.getForEntity("http://api.ipaddress.com/myip?parameters", String.class);
        String ip = entity.getBody();

        logger.info("当前使用 IP: {} 进行访问", ip);

        List<Owner> ownerList = ownerDao.getAll();
        for (Owner owner: ownerList) {
            logger.info("Query github api.. github.com/{}/{}", owner.getUserName(), owner.getRepoName());
            Release[] releases = this.getList(owner.getUserName(), owner.getRepoName());
            for (Release release : releases) {
                List<Asset> assets = release.getAssets();
                if (owner.getSyncSource()) {
                    if (assets == null || assets.size() <= 0) {
                        // archive
                        String fileName = release.getTagName() + ".zip";
                        String path = appEnv.getSavePath() + owner.getUserName() + File.separator +
                                owner.getRepoName() + File.separator +
                                release.getTagName() + File.separator + fileName;
                        FileDown fileDown = new FileDown(fileName, -1L, release.getZipballUrl(), path);
                        if (!downloads.add(fileDown))
                            logger.warn("Add download queue failed , {} file exists", fileName);
                    }
                } else {
                    assets.stream().filter(asset -> !StringUtils.isEmpty(asset.getBrowserDownloadUrl()))
                            .forEach(asset -> {
                                FileDown fileDown = new FileDown(asset.getName(), asset.getSize(), asset.getBrowserDownloadUrl(),
                                        appEnv.getSavePath() + owner.getUserName() + File.separator + owner.getRepoName() + File.separator +
                                                release.getTagName() + File.separator + asset.getName());
                                if (!downloads.add(fileDown))
                                    logger.warn("Add download queue failed , {} file exists", asset.getName());
                            });
                }
            }
        }

        downloads.start();
    }

}
