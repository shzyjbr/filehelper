package com.zzk.filehelper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

    /**
     * 标识发送文件任务的id
     */
    private Integer id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小(字节)
     */
    private long fileSize;

    /**
     * 已接收文件大小(字节)
     */
    private long receivedFileSize;

    /**
     * 上一个确认的序号
     */
    private int lastAck;

    /**
     * 每一次发送的包大小，用来计算偏移量的
     */
    private int packetSize;

    /**
     * 当前文件总共被分割成多少包
     */
    private long total;

    private FileInputStream fileInputStream;

    /**
     * 关闭该任务
     */
    public void close() throws IOException {
        fileInputStream.close();
    }
}
