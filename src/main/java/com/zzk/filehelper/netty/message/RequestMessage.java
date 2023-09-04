package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.zzk.filehelper.netty.message.MessageConfig.REQUEST_MESSAGE;

/**
 * 文件传输消息
 * @Author kelton
 * @Date 2023/8/31 20:42
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RequestMessage extends Message {
    @Override
    public int getMessageType() {
        return REQUEST_MESSAGE;
    }
}
