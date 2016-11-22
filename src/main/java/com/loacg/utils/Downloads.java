package com.loacg.utils;

import com.loacg.entity.FileDown;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/22/2016 11:17 PM
 */
@Component
public class Downloads {

    private static final List<FileDown> fileDownList = new ArrayList<>();

    /**
     * 添加到下载列表
     * @return boolean
     */
    public boolean add(FileDown fileDown) {
        if (fileDownList.contains(fileDown))
            return false;

        fileDownList.add(fileDown);
        return true;
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
}
