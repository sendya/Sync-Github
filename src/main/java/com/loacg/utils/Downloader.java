package com.loacg.utils;

import com.loacg.bootstrap.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Project: Sync-Github
 * Author: liangliang.Yin <yinliangliang@rd.keytop.com.cn>
 * Date: 11/23/2016 10:23 AM
 */
class Downloader extends Thread {

    private static Logger logger = LoggerFactory.getLogger(Downloader.class);

    private int threadId;
    private String filePath; // 档案路径
    private String downUrl;
    private String fileName; // 文档名
    private int startPosition; // 起始位置
    private int endPosition; // 结束位置
    private List<Integer> range;

    Downloader(int threadId, String filePath, String downUrl, String fileName, List<Integer> range) {
        super();
        this.threadId = threadId;
        this.filePath = filePath;
        this.downUrl = downUrl;
        this.fileName = fileName;
        this.range = range;
        this.startPosition = range.get(0);
        this.endPosition = range.get(1);
    }

    @Override
    public void run() {

        try {
            String threadName = Thread.currentThread().getName();

            if (this.range.get(0) > startPosition) {
                startPosition = this.range.get(0);
            }

            URL url = new URL(downUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            logger.info("Thread {} now downloading.. start: {}, end: {}", threadName + threadId, startPosition, endPosition);
            conn.setRequestProperty("Range", "bytes=" + startPosition + "-"
                    + endPosition);
            conn.setConnectTimeout(5000);
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0");

            // logger.info("Range : bytes={}-{}", startPosition, endPosition);
            InputStream is = conn.getInputStream();
            pathExists(new File(filePath).getParent());
            RandomAccessFile file = new RandomAccessFile(filePath, "rwd");

            // 设置数据从那个位置开始写
            file.seek(startPosition);
            byte[] buffer = new byte[1024];
            // 文件长度
            // 当 length = -1代表文件读完了
            int length = 0;

            // 当前读到服务器数据的位置，同时这个值已经存储的文件的位置
            int currentPosition = startPosition;

            while ((length = is.read(buffer)) != -1) {
                file.write(buffer, 0, length);

                // 需要把 currentPosition 信息持久化到存储设备
                currentPosition += length;
                this.range.set(0, currentPosition);
            }

            file.close();
            this.range = null;
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

    private static boolean pathExists(String filePath) {
        File p = new File(filePath);
        return !p.exists() && p.mkdirs();
    }
}
