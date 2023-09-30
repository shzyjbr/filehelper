package com.zzk.filehelper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 提供给发送文件的一端使用
 * @Author kelton
 * @Date 2023/9/30 23:37
 * @Version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendFileTask {

    private String fileName;

    private long fileSize;

    private long receivedFileSize;

    private FileInputStream fileInputStream;
}
