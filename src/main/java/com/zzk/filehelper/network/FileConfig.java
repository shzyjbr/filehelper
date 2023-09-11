package com.zzk.filehelper.network;

import java.io.File;

/**
 * @author: kkrunning
 * @since: 2023/3/13 19:00
 * @description: 传输包结构： 魔术字（4字节） 传输类型（4字节） 文件名长度（4字节） 文件名（文件名长度字节） 文件长度（8字节） 文件（文件长度字节）
 */
public interface FileConfig {



    /**
     * 0:文件类型
     */
    int FILE_TYPE = 0;


    String BASE_DIR = System.getProperty("user.home") + File.separator;
}
