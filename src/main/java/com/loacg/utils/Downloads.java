package com.loacg.utils;

import com.loacg.entity.FileDown;
import javassist.bytecode.analysis.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private static Logger logger = LoggerFactory.getLogger(Downloads.class);

    private static final List<FileDown> fileDownList = new ArrayList<>();

    private static long last = 0;

    /**
     * 添加到下载列表
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
                build(file.getPath(), file.getFileName(), file.getUrl(), file.getFileSize(), exe);
            }

            exe.shutdown();
            while (true) {
                if(exe.isTerminated()) {
                    logger.info("all downloads success");
                    break;
                }
                Thread.sleep(30);
            }
        } catch (Exception e) {
            logger.error("Exception: {}", e);
        }
    }


    private void build(String path, String name, String downUrl, Long fileSize, ExecutorService exe) throws Exception {

        try {

            URL url = new URL(downUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0");
            int code = conn.getResponseCode();
            if (code == 200) {
                int length = conn.getContentLength();
                logger.info("文件名称：{} , 下载文件大小： {} , 文件实际大小: {}", name, length, fileSize);

                if (length == -1) {
                    logger.error("Download queue task : {} length -1", name);
                    return;
                }


                /*
                 * mode 值 含意:<br>
                 *
                 * "r" 以只读方式打开。调用结果对象的任何 write 方法都将导致抛出 IOException。 <br>
                 * "rw" 打开以便读取和写入。如果该文件尚不存在，则尝试创建该文件。<br>
                 * "rws" 打开以便读取和写入，对于 "rw"，还要求对文件的内容或元数据的每个更新都同步写入到底层存储设备。<br>
                 * "rwd" 打开以便读取和写入，对于 "rw"，还要求对文件内容的每个更新都同步写入到底层存储设备。
                 */
                // 产生一个跟服务器文件大小一致的空文件
                File p = new File(new File(path).getParent());
                if (!p.exists()) p.mkdirs();

                RandomAccessFile file = new RandomAccessFile(path, "rwd");
                // 在本地创建一个文件 文件大小要跟服务器文件的大小一致
                file.setLength(length);
                Map<String, List<Integer>> ranges = new HashMap<>();
                // 开启4个线程
                int threadNumber = 3;
                int blockSize = length / threadNumber;
                for (int i = 0; i < threadNumber; i++) {
                    List<Integer> range = new ArrayList<>();
                    int startPosition = i * blockSize;
                    int endPosition = (i + 1) * blockSize;
                    if (i == (threadNumber - 1)) {
                        // 最后一个线程
                        endPosition = length;
                    }
                    range.add(startPosition);
                    range.add(endPosition);
                    exe.execute(new Downloader(i, path, downUrl, name, range));
                }
            }
        } catch (Exception e) {
            logger.error("Exception: {}", e);
        }
    }
}
