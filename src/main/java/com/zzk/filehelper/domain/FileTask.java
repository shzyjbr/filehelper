package com.zzk.filehelper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: kkrunning
 * @since: 2023/3/21 20:08
 * @description: 文件任务
 */
@Data
@AllArgsConstructor
public class FileTask {
    public enum TransferStatus {
        /**
         * 排队中
         */
        peeding,
        /**
         * 传输中
         */
        transferring,
        /**
         * 传输完成
         */
        completed,
        /**
         * 异常关闭
         */
        unexpected_close;
    }

    public enum FileType {
        /**
         * 接收文件
         */
        receive,
        /**
         * 发送文件
         */
        send;
    }

    /**
     * 观察者模式，当文件传输完成时，可以用来通知对此传输任务进行观察的对象
     */
    private List<FileObserver> observers;

    /**
     * UUID
     */
    private String taskId;


    /**
     * 文件名， 如 readme.txt
     */
    private String filename;

    /**
     * 文件全路径，如 D:\\docs\\readme.txt
     */

    private String fullPath;

    /**
     * 文件总大小
     */
    private long totalSize;

    /**
     * 文件当前已接收大小；如果是个发送任务，则代表文件当前已发送大小
     */
    private long currentSize;

    /**
     * 传输状态
     */
    private TransferStatus transferStatus;

    /**
     * 是否已完成，和上面的传输状态有点重叠了
     */
    private boolean finish;

    /**
     * 任务创建时间 ms
     */
    private long createTime;

    /**
     * 开始传输时间。 用来计算传输速度
     */
    private long beginTransferTime;

    /**
     * 完成时间
     */
    private long finishTime;

    /**
     * 接收文件还是发送文件
     */
    private FileType fileType;


    public FileTask() {
        observers = new ArrayList<>();
        taskId = UUID.randomUUID().toString().replaceAll("-","");
    }


    public void register(FileObserver observer) {
        this.observers.add(observer);
    }

}
