package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.zzk.filehelper.netty.message.MessageConfig.REPLY_MESSAGE;

/**
 * 文件发送完成确认消息
 * @Author kelton
 * @Date 2023/8/31 20:43
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ReplyMessage extends Message {

    @Override
    public int getMessageType() {
        return REPLY_MESSAGE;
    }
}
