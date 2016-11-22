package com.loacg.tasks;

import com.loacg.bootstrap.App;
import com.loacg.dao.OwnerDao;
import com.loacg.entity.FileDown;
import com.loacg.entity.Owner;
import com.loacg.github.SyncDaemon;
import com.loacg.github.entity.Asset;
import com.loacg.github.entity.Release;
import com.loacg.utils.Downloads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/22/2016 10:57 PM
 */
@Component
@Configurable
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    SyncDaemon daemon;

    @Autowired
    OwnerDao ownerDao;

    @Autowired
    Downloads downloads;

    public void delayPullReleases() {
        List<Owner> ownerList = ownerDao.getAll();
        for (Owner owner: ownerList) {
            Release[] releases = daemon.getList(owner.getUserName(), owner.getRepoName());
            for (Release release : releases) {
                List<Asset> assets = release.getAssets();
                if (assets == null || assets.size() <= 0) {
                    continue;
                }
                assets.stream().filter(asset -> !StringUtils.isEmpty(
                        asset.getBrowserDownloadUrl())).forEach(asset -> {
                            FileDown fileDown = new FileDown();
                            fileDown.setFileName(asset.getName());
                            fileDown.setFileSize(asset.getSize());
                            fileDown.setUrl(asset.getBrowserDownloadUrl());
                            fileDown.setPath(App.DEFAULT_PATH);
                            downloads.add(fileDown);
                });
            }
        }

    }

}
