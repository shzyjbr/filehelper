package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.zzk.filehelper.netty.message.MessageConfig.OFFLINE_MESSAGE;

/**
 * 离线消息,离线消息没有对应的回复消息，因为离线不需要回复
 * @Author zhouzekun
 * @Date 2023/9/6 9:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineMessage extends Message{

    private String ip;

    private int port;
    @Override
    public int getMessageType() {
        return OFFLINE_MESSAGE;
    }
}
