package com.zzk.filehelper.network;

import com.zzk.filehelper.domain.ReceiveFileTask;
import io.netty.channel.ChannelId;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author kelton
 * @Date 2023/9/30 23:20
 * @Version 1.0
 */
public class FileSendManager {

    private static final Map<ChannelId, ReceiveFileTask> fileInputStreamMap =  new HashMap<>();

    public static void addTask(ChannelId id, ReceiveFileTask task) {
        fileInputStreamMap.put(id, task);
    }

    public static void remoeTask(ChannelId id) {
        fileInputStreamMap.remove(id);
    }

    public static ReceiveFileTask getTask(ChannelId id) {
        return fileInputStreamMap.get(id);
    }
}
