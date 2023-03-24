package com.zzk.filehelper.domain;

import java.io.Serializable;

/**
 * @author: kkrunning
 * @since: 2023/3/13 14:13
 * @description:
 */
public class Message implements Serializable {

    /**
     * 消息格式：
     */
    private Object content;

    /**
     * 0: 上线消息
     * 1: 应答消息
     * 2: 文件发送回复消息：同意或者拒绝
     */
    private MessageType type;

    public Message(Object content, MessageType type) {
        this.content = content;
        this.type = type;
    }

    public Message() {
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content=" + content +
                ", type=" + type +
                '}';
    }
}

