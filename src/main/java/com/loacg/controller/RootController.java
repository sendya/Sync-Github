package com.loacg.controller;

import com.loacg.bootstrap.AppEnv;
import com.loacg.github.SyncDaemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 2017/05/12 AM 02:14
 */
@RestController
public class RootController {

    @Autowired
    private AppEnv appEnv;

    @Autowired
    private SyncDaemon daemon;

    @RequestMapping("/")
    public String home() {
        return String.format("save path: %s, thread: %s, max thread: %s", appEnv.getSavePath(), appEnv.getSingleFileDownloadThread(), appEnv.getMaxThread());
    }

    @RequestMapping("/start")
    public String start() {
        daemon.sync();
        return "success";
    }
}
