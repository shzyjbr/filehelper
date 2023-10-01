package com.zzk.filehelper.network;

import com.zzk.filehelper.domain.ReceiveFileTask;
import io.netty.channel.ChannelId;

import java.util.HashMap;
import java.util.Map;

/**
 * 待确认文件任务管理器，可以在发送文件时将pendingTask加入manager
 * 在收到metaAck的时候将pendingTask移出manager，转而加入sendManager
 * @Author zhouzekun
 * @Date 2023/10/1 10:21
 */
public class FilePendingManager {

    private static final Map<ChannelId, PendingFileTask> PENDING_FILE_TASK_MAP = new HashMap<>();

    public static void addTask(ChannelId id, PendingFileTask task) {
        PENDING_FILE_TASK_MAP.put(id, task);
    }
    //删除对应的outputStream
    public static void removeTask(ChannelId id) {
        PENDING_FILE_TASK_MAP.remove(id);
    }

    public static PendingFileTask getTask(ChannelId id) {
        return PENDING_FILE_TASK_MAP.get(id);
    }
}
