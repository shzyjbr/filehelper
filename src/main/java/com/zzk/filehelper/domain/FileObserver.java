package com.zzk.filehelper.domain;

/**
 * @author: kkrunning
 * @since: 2023/3/22 10:44
 * @description:
 */
public interface FileObserver {
    void action(FileTask task);
}
