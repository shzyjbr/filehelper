package com.zzk.filehelper.domain;

/**
 * 原意是做一个文件传输任务的callback，可以实时获取传输速度
 * @author: kkrunning
 * @since: 2023/3/22 10:44
 * @description:
 */
public interface FileObserver {
    void action(FileTask task);
}
