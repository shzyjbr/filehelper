package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.zzk.filehelper.netty.message.MessageConfig.ONLINE_REQUEST_MESSAGE;

/**
 * 客户端上线消息
 * @Author kelton
 * @Date 2023/7/3 21:22
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OnlineRequestMessage extends Message{

    /**
     * 告知别人自己的ip
     */
    private String ip;

    /**
     * 告知别人自己的端口
     */
    private int port;


    @Override
    public int getMessageType() {
        return ONLINE_REQUEST_MESSAGE;
    }
}
