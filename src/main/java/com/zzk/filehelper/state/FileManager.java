package com.zzk.filehelper.state;

import lombok.Data;

import java.util.*;

/**
 * @Author zhouzekun
 * @Date 2023/9/11 17:27
 */

@Data
public class FileManager {

    private String filename;

    private long length;

    private List<Object> pendingFiles;

    public static FileManager INSTANCE;

    private FileManager() {
        pendingFiles = new ArrayList<>();
    }


}
