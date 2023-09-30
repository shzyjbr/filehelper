package com.zzk.filehelper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileOutputStream;

/**
 * 提供给接收文件的一端使用
 * @Author kelton
 * @Date 2023/9/27 0:57
 * @Version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveFileTask {

    private String fileName;

    private long fileSize;

    private long receivedFileSize;

    private FileOutputStream fileOutputStream;
}
