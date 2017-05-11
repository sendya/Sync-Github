package com.loacg.utils;

import com.loacg.bootstrap.AppEnv;
import com.loacg.entity.FileDown;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/22/2016 11:17 PM
 */
@Component
public class Downloads {

    @Autowired
    private AppEnv appEnv;

    private static Logger logger = LoggerFactory.getLogger(Downloads.class);

    private static final List<FileDown> fileDownList = new ArrayList<>();

    private static long last = 0;

    /**
     * 添加到下载列表
     *
     * @return boolean
     */
    public boolean add(FileDown fileDown) {
        if (fileDownList.contains(fileDown))
            return false;
        if (this.exists(fileDown.getPath(), fileDown.getFileSize()))
            return false;

        logger.info("Add -> {}", fileDown);
        fileDownList.add(fileDown);
        return true;
    }

    /**
     * download file exists
     *
     * @param path
     * @param size
     * @return boolean
     */
    private boolean exists(String path, Long size) {
        File f = new File(path);
        return f.exists() && (f.length() == size || size == -1L);
    }

    /**
     * 批量添加到下载列表
     *
     * @param fileDowns
     * @return boolean
     */
    public Map<String, Boolean> add(List<FileDown> fileDowns) {
        Map<String, Boolean> flags = new HashMap<>();
        Iterator<FileDown> it = fileDowns.iterator();
        for (FileDown fileDown : fileDowns) {
            flags.put(fileDown.getKey(), this.add(fileDown));
        }
        return flags;
    }

    public void start() {
        try {
            ExecutorService exe = Executors.newFixedThreadPool(15);
            //
            for (FileDown file : fileDownList) {
                build(file.getPath(), file.getFileName(), file.getUrl(), file.getFileSize(), exe, 0x0);
            }

            exe.shutdown();
            while (true) {
                if (exe.isTerminated()) {
                    logger.info("all downloads success");
                    break;
                }
                Thread.sleep(30);
            }
        } catch (Exception e) {
            logger.error("Exception: {}", e);
        }
    }


    private void build(String path, String name, String downUrl, Long fileSize, ExecutorService exe, int retry) throws Exception {

        // 如果重试次数大于等于 3 则取消下载这一文件
        if (retry >= appEnv.getRetry()) return;

        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(appEnv.getProxyHost(), appEnv.getProxyPort()));
        URL ipURL = new URL("http://api.ipaddress.com/myip?parameters");
        HttpURLConnection connt = (HttpURLConnection)ipURL.openConnection(proxy);
        logger.info("URL 对象请求所用 IP ：{}", connt.getContent());

        URL url = new URL(downUrl);

        long length;
        try {

            URLConnection conn;

            if (appEnv.isProxyEnable()) {
                conn = url.openConnection(proxy);
            } else {
                conn = url.openConnection();
            }

            conn.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0");
            length = conn.getContentLengthLong(); // 获得网络文件流长度

        } catch (IOException e) {
            logger.warn("Open address failed: {}, message: {}, retry: {}", downUrl, e.getMessage(), retry);
            build(path, name, downUrl, fileSize, exe, ++retry);
            return;
        }

        logger.info("文件名称：{} , 下载文件大小： {} , 文件实际大小: {}", name, length, fileSize);

        if (length <= 0) {
            logger.error("Download queue task : {}, length 0", name);
            return;
        }

        File file = new File(path);
        // 检查要存的文件目录(包含子目录)是否有建立，没建立则创建
        FileUtil.pathExists(file.getParent());

        // 在本地创建一个文件 文件大小要跟服务器文件的大小一致
        FileUtil.openSpace(file, length);

        // 每个文件的下载线程数
        int threadNum = appEnv.getSingleFileDownloadThread();
        long blockSize = length / threadNum;

        for (int i = 0; i < threadNum; i++) {
            long startPosition = blockSize * i; // 开始下载的字节数
            long endPosition = blockSize * (i + 1); // 终止下载的字节数
            if (i == (threadNum - 1)) { // 如果最后一个线程，则将所有剩余的字节都分配给最后一个线程
                endPosition = length;
            }
            exe.execute(new Downloader(path, downUrl, name, startPosition, endPosition, proxy));
        }


    }


}
