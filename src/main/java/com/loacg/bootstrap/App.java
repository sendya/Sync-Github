package com.loacg.bootstrap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/22/2016 11:44 PM
 */
@Component
@ConfigurationProperties(prefix = "github.sync")
public class App {

    public static final String DEFAULT_PATH = "D:\\tmp\\";

    public static final int TRY_COUNT = 10;

    public static final boolean IS_PROXY = false;

    private String basePath;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
