package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author kelton
 * @Date 2023/9/30 23:13
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileContentAckMessage extends Message {
    /**
     * 唯一标识，用来标识一个文件传输任务
     */
    private Integer id;
    /**
     * 确认号，表示已经接收到的数据包的编号
     */
    private long ackNumber;

    /**
     * 该数据包的数据大小(字节)
     */
    private long packetSize;

    @Override
    public int getMessageType() {
        return MessageConfig.FILE_CONTENT_ACK_MESSAGE;
    }
}
