package com.zzk.filehelper.netty.message;

import com.zzk.filehelper.netty.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.stream.ChunkedInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.RandomAccessFile;

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
    private String name;

    /**
     * 文件内容
     */
    private byte[] content;

    @Override
    public int getMessageType() {
        return MessageConfig.FILE_MESSAGE;
    }
}
