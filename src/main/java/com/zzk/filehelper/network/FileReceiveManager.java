package com.zzk.filehelper.network;

import com.zzk.filehelper.domain.ReceiveFileTask;
import io.netty.channel.ChannelId;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author kelton
 * @Date 2023/9/27 0:53
 * @Version 1.0
 */
public class FileReceiveManager {

    private static final Map<ChannelId, ReceiveFileTask> fileOutputStreamMap = new HashMap<>();

    public static void addTask(ChannelId id, ReceiveFileTask task) {
        fileOutputStreamMap.put(id, task);
    }
    //删除对应的outputStream
    public static void remoeTask(ChannelId id) {
        fileOutputStreamMap.remove(id);
    }

    public static ReceiveFileTask getTask(ChannelId id) {
        return fileOutputStreamMap.get(id);
    }
}
