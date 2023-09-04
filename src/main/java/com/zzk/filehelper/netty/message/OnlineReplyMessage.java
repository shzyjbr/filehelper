package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.zzk.filehelper.netty.message.MessageConfig.ONLINE_REPLY_MESSAGE;

/**
 * 客户端对于其他客户端上线消息的回复
 * @Author kelton
 * @Date 2023/7/3 21:27
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OnlineReplyMessage extends Message {

    /**
     * 回复自己的IP
     */
    private String ip;


    /**
     * 回复自己的文件传输端口
     */
    private int port;


    @Override
    public int getMessageType() {
        return ONLINE_REPLY_MESSAGE;
    }
}
