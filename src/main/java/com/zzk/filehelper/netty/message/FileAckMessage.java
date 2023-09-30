package com.zzk.filehelper.netty.message;

/**
 * @Author kelton
 * @Date 2023/9/30 23:13
 * @Version 1.0
 */
public class FileAckMessage {
    /**
     * 唯一标识，用来标识一个文件传输任务
     */
    private String id;
    /**
     * 确认号，表示已经接收到的数据包的编号
     */
    private int ackNumber;
}
