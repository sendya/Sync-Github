package com.loacg.tasks;

import com.loacg.github.SyncDaemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    private SyncDaemon daemon;

    @Scheduled(cron = "0 0 2 * * *")
    public void delayPullReleases() {
        daemon.sync(); // 运行同步
    }

}
