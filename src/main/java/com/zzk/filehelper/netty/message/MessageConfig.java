package com.zzk.filehelper.netty.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息类型集中管理，增加和删除消息需要在这里做统一的配置
 * @Author kelton
 * @Date 2023/9/4 22:20
 * @Version 1.0
 */
public class MessageConfig {
    /**
     * 根据消息类型字节，获得对应的消息 class
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    /**
     * 上线请求消息
     */
    public static final int ONLINE_REQUEST_MESSAGE = 0;
    /**
     * 上线回复消息
     */
    public static final int ONLINE_REPLY_MESSAGE = 1;

    /**
     * 离线通知消息，不需要回复
     */
    public static final int OFFLINE_MESSAGE = 2;

    /**
     * 文件发送预检请求消息
     */
    public static final int OPTION_REQUEST_MESSAGE = 3;
    /**
     * 文件发送预检回复消息
     */
    public static final int OPTION_REPLY_MESSAGE = 4;
    /**
     * 文件内容消息
     */
    public static final int FILE_CONTENT_MESSAGE = 5;

    /**
     * 文件元信息消息，存着文件名长度，文件名，文件大小，总共分成几个包等信息
     */
    public static final int FILE_META_MESSAGE = 6;
    /**
     * 文件包确认消息
     */
    public static final int FILE_CONTENT_ACK_MESSAGE = 7;
    /**
     * 文件元数据确认消息
     */
    public static final int FILE_META_ACK_MESSAGE = 8;


    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(ONLINE_REQUEST_MESSAGE, OnlineRequestMessage.class);
        messageClasses.put(ONLINE_REPLY_MESSAGE, OnlineReplyMessage.class);
        messageClasses.put(OPTION_REQUEST_MESSAGE, OptionRequestMessage.class);
        messageClasses.put(OPTION_REPLY_MESSAGE, OptionReplyMessage.class);
        messageClasses.put(OFFLINE_MESSAGE, OfflineMessage.class);
        messageClasses.put(FILE_META_MESSAGE, FileMetaMessage.class);
        messageClasses.put(FILE_META_ACK_MESSAGE, FileMetaAckMessage.class);
        messageClasses.put(FILE_CONTENT_ACK_MESSAGE, FileContentAckMessage.class);

    }
}
