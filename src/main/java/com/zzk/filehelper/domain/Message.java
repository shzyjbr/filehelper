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

