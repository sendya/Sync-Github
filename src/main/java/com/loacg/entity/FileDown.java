package com.loacg.entity;

import com.loacg.utils.MD5Util;

import java.io.File;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/22/2016 11:18 PM
 */
public class FileDown {

    /**
     * 唯一键
     */
    private String key;

    /**
     * 要下载的文件地址
     */
    private String url;

    /**
     * 下载文件存放位置
     */
    private String path;

    /**
     * 下载的文件另存名称
     * 若此参数为空，则使用 url 参数自动格式化文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 优先级
     */
    private int priority;

    /**
     * 下载状态
     *      0 - 无状态
     *      1 - 下载成功
     *      2 - 下载中
     *      3 - 超时
     *      4 - 被置于等待状态
     */
    private int status;

    /**
     * 完成时间
     */
    private Integer completeTime;

    public FileDown() {
        this.key = MD5Util.md5(String.valueOf(System.currentTimeMillis()));
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Integer completeTime) {
        this.completeTime = completeTime;
    }

    @Override
    public String toString() {
        return "FileDown{" +
                "url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", priority=" + priority +
                ", status=" + status +
                ", completeTime=" + completeTime +
                '}';
    }
}
