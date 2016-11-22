package com.loacg.entity;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/22/2016 8:33 PM
 */
public class Owner {

    /**
     * Primary key
     */
    private Integer id;

    /**
     * Github user name
     */
    private String userName;

    /**
     * Github repo name
     */
    private String repoName;

    /**
     * Sync repo release last
     */
    private String syncLast;

    /**
     * last sync time
     */
    private Integer lastSyncTime;
    /**
     * Github repo release latest
     */
    private String latest;

    public static Owner build() {
        return new Owner();
    }

    public Integer getId() {
        return id;
    }

    public Owner setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Owner setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getRepoName() {
        return repoName;
    }

    public Owner setRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }

    public String getSyncLast() {
        return syncLast;
    }

    public Owner setSyncLast(String syncLast) {
        this.syncLast = syncLast;
        return this;
    }

    public Integer getLastSyncTime() {
        return lastSyncTime;
    }

    public Owner setLastSyncTime(Integer lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
        return this;
    }

    public String getLatest() {
        return latest;
    }

    public Owner setLatest(String latest) {
        this.latest = latest;
        return this;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", repoName='" + repoName + '\'' +
                ", syncLast='" + syncLast + '\'' +
                ", latest='" + latest + '\'' +
                '}';
    }
}
