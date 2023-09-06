package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

import static com.zzk.filehelper.netty.message.MessageConfig.OPTION_REPLY_MESSAGE;
import static com.zzk.filehelper.netty.message.MessageConfig.OPTION_REQUEST_MESSAGE;

/**
 * 文件发送预检回复消息
 * @Author  zhouzekun
 * @Date 2023/9/6
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OptionReplyMessage extends Message{

    /**
     * 要发送的文件名
     */
    private Map<String, Boolean> reply;

    @Override
    public int getMessageType() {
        return OPTION_REPLY_MESSAGE;
    }
}
