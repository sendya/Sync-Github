package com.loacg.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/22/2016 11:44 PM
 */
@Component
@ConfigurationProperties(prefix = "github")
public class AppEnv {

    private int retry;

    private String savePath;

    private int maxThread;

    private int singleFileDownloadThread;

    @Value("${github.proxy.enable}")
    private boolean proxyEnable;

    @Value("${github.proxy.host}")
    private String proxyHost;

    @Value("${github.proxy.port}")
    private int proxyPort;

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public int getMaxThread() {
        return maxThread;
    }

    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    public int getSingleFileDownloadThread() {
        return singleFileDownloadThread;
    }

    public void setSingleFileDownloadThread(int singleFileDownloadThread) {
        this.singleFileDownloadThread = singleFileDownloadThread;
    }

    public boolean isProxyEnable() {
        return proxyEnable;
    }

    public void setProxyEnable(boolean proxyEnable) {
        this.proxyEnable = proxyEnable;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    @Override
    public String toString() {
        return "AppEnv{" +
                "retry=" + retry +
                ", savePath='" + savePath + '\'' +
                ", maxThread=" + maxThread +
                ", singleFileDownloadThread=" + singleFileDownloadThread +
                ", proxyEnable=" + proxyEnable +
                ", proxyHost='" + proxyHost + '\'' +
                ", proxyPort=" + proxyPort +
                '}';
    }
}
