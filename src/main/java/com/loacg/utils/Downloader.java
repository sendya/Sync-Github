package com.loacg.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Project: Sync-Github
 * Author: liangliang.Yin <yinliangliang@rd.keytop.com.cn>
 * Date: 11/23/2016 10:23 AM
 */
class Downloader extends Thread {

    private static Logger logger = LoggerFactory.getLogger(Downloader.class);

    private String filePath; // 档案路径
    private String downUrl;
    private String fileName; // 文档名
    private long startPosition; // 起始位置
    private long endPosition; // 结束位置
    private Proxy proxy = null;

    Downloader(String filePath, String downUrl, String fileName, long startPosition, long endPosition, Proxy proxy) {
        super();
        this.filePath = filePath;
        this.downUrl = downUrl;
        this.fileName = fileName;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.proxy = proxy;
    }

    @Override
    public void run() {

        try {
            String threadName = Thread.currentThread().getName();

            URL url = new URL(downUrl);
            HttpURLConnection conn = null;
            if (proxy != null) {
                conn = (HttpURLConnection) url.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }

            logger.info("Thread {} now downloading.. start: {}, end: {}", threadName, startPosition, endPosition);
            conn.setRequestProperty("Range", "bytes=" + startPosition + "-"
                    + endPosition);
            conn.setConnectTimeout(5000);
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0");

            // logger.info("Range : bytes={}-{}", startPosition, endPosition);
            InputStream is = conn.getInputStream();
            FileUtil.pathExists(new File(filePath).getParent());
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date date = new Date();

            logger.info("[{}] 开始时间：{}", threadName, sdf.format(date));
            // 设置数据从那个位置开始写
            file.seek(startPosition);
            byte[] buffer = new byte[1024];
            // 文件长度
            // 当 length = -1代表文件读完了
            int length = 0;

            // 当前读到服务器数据的位置，同时这个值已经存储的文件的位置
            long currentPosition = startPosition;

            while ((length = is.read(buffer)) != -1) {
                file.write(buffer, 0, length);
                currentPosition += length;
                this.startPosition = currentPosition;
            }

            file.close();
            Date lastDate = new Date();
            logger.info("[{}] 结束时间： {}, 相差(毫秒)：{}", threadName, sdf.format(lastDate), lastDate.getTime() - date.getTime());
            logger.info("Thread {} download success", threadName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] getBytes(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        bos.flush();
        return bos.toByteArray();
    }


}
