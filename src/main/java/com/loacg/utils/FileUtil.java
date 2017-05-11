package com.loacg.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by sendya on 2017/5/12.
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 检查路径是否存在，不存在则创建该路径
     * @param filePath
     * @return
     */
    public static boolean pathExists(String filePath) {
        File p = new File(filePath);
        return !p.exists() && p.mkdirs();
    }

    /**
     *
     * 创建一个新的空文件
     *
     * RandomAccessFile  mode 值 含意:<br>
     *
     * "r" 以只读方式打开。调用结果对象的任何 write 方法都将导致抛出 IOException。 <br>
     * "rw" 打开以便读取和写入。如果该文件尚不存在，则尝试创建该文件。<br>
     * "rws" 打开以便读取和写入，对于 "rw"，还要求对文件的内容或元数据的每个更新都同步写入到底层存储设备。<br>
     * "rwd" 打开以便读取和写入，对于 "rw"，还要求对文件内容的每个更新都同步写入到底层存储设备。
     */
    public static boolean openSpace(File file, Long sourceSize) {
        RandomAccessFile randomAccessFile = null;

        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.setLength(sourceSize);
            return true;
        } catch (Exception e) {
            logger.error("Create file failed: {}", e.getMessage());
            return false;
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                    return true;
                }
            } catch (IOException e) {
                logger.error("Close file failed: {}", e.getMessage());
                return false;
            }
        }
    }
}
