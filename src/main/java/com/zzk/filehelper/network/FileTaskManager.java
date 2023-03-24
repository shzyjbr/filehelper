package com.zzk.filehelper.network;

import com.zzk.filehelper.domain.FileObserver;
import com.zzk.filehelper.domain.FileTask;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: kkrunning
 * @since: 2023/3/21 20:20
 * @description: FileTask管理者， 在这里持有FileTask状态， 单例模式（静态字段+私有构造器）
 */
public class FileTaskManager implements FileObserver {
    /**
     * FileTask队列
     */
    private Deque<FileTask> fileTasks;


    private static final  FileTaskManager instance = new FileTaskManager();

    private FileTaskManager() {
        fileTasks = new LinkedList<>();
    }

    public static FileTaskManager getInstance() {
        return instance;
    }


    public void offer(FileTask task) {
        fileTasks.offer(task);
    }

    public Deque<FileTask> getFileTasks() {
        return fileTasks;
    }

    /**
     * waitingQue队列为空时返回null
     * @return
     */
    public FileTask poll() {
        return fileTasks.poll();
    }

    public List<FileTask> getWaitFileTasks() {
        return fileTasks
                .stream()
                .filter(fileTasks -> fileTasks.getTransferStatus()== FileTask.TransferStatus.peeding)
                .collect(Collectors.toList());
    }

    public List<FileTask> getTransferFileTasks() {
        return fileTasks
                .stream()
                .filter(fileTasks -> fileTasks.getTransferStatus()== FileTask.TransferStatus.transferring)
                .collect(Collectors.toList());
    }

    public List<FileTask> getHistoryFileTasks() {
        return fileTasks
                .stream()
                .filter(fileTasks -> fileTasks.getTransferStatus()== FileTask.TransferStatus.history)
                .collect(Collectors.toList());
    }


    /**
     * task是事件源
     * @param task
     */
    @Override
    public void action(FileTask task) {
    //    模拟做一件事情
        System.out.println(task);
        System.out.println("收到filetask状态改变:"+ task.getTransferStatus());
        System.out.println("发送进度："+ String.format("%.2f%%",task.getProgressRate() * 100.0 ));
    }
}
