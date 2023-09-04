package com.zzk.filehelper.netty.message;

import java.util.HashMap;
import java.util.Map;

/**
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

    public static final int ONLINE_REQUEST_MESSAGE = 0;
    public static final int ONLINE_REPLY_MESSAGE = 1;

    /**
     * 请求类型 byte 值
     */
    public static final int REQUEST_MESSAGE = 101;
    /**
     * 响应类型 byte 值
     */
    public static final int REPLY_MESSAGE = 102;

    public static final int OPTION_MESSAGE = 103;
    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(ONLINE_REQUEST_MESSAGE, OnlineRequestMessage.class);
        messageClasses.put(ONLINE_REPLY_MESSAGE, OnlineReplyMessage.class);

        messageClasses.put(REQUEST_MESSAGE, RequestMessage.class);
        messageClasses.put(REPLY_MESSAGE, ReplyMessage.class);
    }
}
