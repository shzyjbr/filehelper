package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author kelton
 * @Date 2023/9/26 23:55
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetaMessage extends Message{


    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 分成了多少数据包
     */
    private long packetSize;
    @Override
    public int getMessageType() {
        return MessageConfig.FILE_META_MESSAGE;
    }
}
