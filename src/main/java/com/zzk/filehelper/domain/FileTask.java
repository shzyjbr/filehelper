package com.zzk.filehelper.domain;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: kkrunning
 * @since: 2023/3/21 20:08
 * @description: 文件任务
 */
public class FileTask {
    public enum TransferStatus {
        peeding, transferring, history, unexpected_close;
    }

    public enum FileType {
        receive, send;
    }

    private List<FileObserver> observers;

    /**
     * UUID
     */
    private String taskId;


    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件全路径
     */

    private String fullPath;

    /**
     * 文件总大小
     */
    private long totalSize;

    /**
     * 文件当前大小
     */
    private long currentSize;

    /**
     * 传输状态
     */
    private TransferStatus transferStatus;

    /**
     * 是否已完成
     */
    private boolean finish;

    /**
     * 任务创建时间 ms
     */
    private long createTime;

    /**
     * 开始传输时间
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

    /**
     * socket
     */
    private Socket socket;

    public FileTask() {
        observers = new ArrayList<>(16);
        taskId = UUID.randomUUID().toString().replaceAll("-","");
    }

    public FileTask(String filename, long totalSize, long currentSize, TransferStatus transferStatus,
                    boolean finish, long createTime, long beginTransferTime, long finishTime, FileType fileType) {
        this();
        this.filename = filename;
        this.totalSize = totalSize;
        this.currentSize = currentSize;
        this.transferStatus = transferStatus;
        this.finish = finish;
        this.createTime = createTime;
        this.beginTransferTime = beginTransferTime;
        this.finishTime = finishTime;
        this.fileType = fileType;
    }

    public void register(FileObserver observer) {
        this.observers.add(observer);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
        notifyObservers();
    }


    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
        notifyObservers();
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getBeginTransferTime() {
        return beginTransferTime;
    }

    public void setBeginTransferTime(long beginTransferTime) {
        this.beginTransferTime = beginTransferTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
        notifyObservers();
    }

    private void notifyObservers() {
        this.observers.forEach(observer -> observer.action(this));
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * 计算当前传输进度;
     * @return
     */
    public double getProgressRate() {
        return (double) currentSize / totalSize;
    }

    @Override
    public String toString() {
        return "FileTask{" +
                "observers=" + observers +
                ", taskId='" + taskId + '\'' +
                ", filename='" + filename + '\'' +
                ", fullPath='" + fullPath + '\'' +
                ", totalSize=" + totalSize +
                ", currentSize=" + currentSize +
                ", transferStatus=" + transferStatus +
                ", finish=" + finish +
                ", createTime=" + createTime +
                ", beginTransferTime=" + beginTransferTime +
                ", finishTime=" + finishTime +
                ", fileType=" + fileType +
                ", socket=" + socket +
                '}';
    }
}
