package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * ChunkedInput了解一下
 * 先尝试一下小文件
 * @Author  zhouzekun
 * @Date 2023/9/6 16:40
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMessage extends Message{
    /**
     * 文件名
     */
    private File file;

    /**
     * 0 表示文件发送请求
     * 1 表示后续内容接受
     */
    private int ACK;

    @Override
    public int getMessageType() {
        return MessageConfig.FILE_MESSAGE;
    }
}
