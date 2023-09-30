package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件内容package
 * @Author kelton
 * @Date 2023/9/25 0:50
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileContentMessage extends Message{
    /**
     * 当前是第几个包
     */
    private long packetNumber;

    /**
     * 该文件总共被分成了几个包
     */
    private long totalPackets;

    /**
     * 该包的内容长度, 即data的长度
     */
    private int contentLength;

    /**
     * 该包的内容
     */
    private byte[] data;
    @Override
    public int getMessageType() {
        return MessageConfig.FILE_MESSAGE;
    }
}
