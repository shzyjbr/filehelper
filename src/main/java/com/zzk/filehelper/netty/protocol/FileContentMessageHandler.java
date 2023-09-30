package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.domain.ReceiveFileTask;
import com.zzk.filehelper.netty.message.FileContentMessage;
import com.zzk.filehelper.network.FileReceiveManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;

/**
 * @Author kelton
 * @Date 2023/9/30 21:54
 * @Version 1.0
 */
@Slf4j
public class FileContentMessageHandler extends SimpleChannelInboundHandler<FileContentMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FileContentMessage fileContentMessage) throws Exception {
        // 取出对应的传输任务
        ReceiveFileTask task = FileReceiveManager.getTask(channelHandlerContext.channel().id());
        // 写入文件
        FileOutputStream fileOutputStream = task.getFileOutputStream();
        fileOutputStream.write(fileContentMessage.getData());
        task.setReceivedFileSize(task.getReceivedFileSize() + fileContentMessage.getContentLength());
        log.info("文件：{}, 当前写入文件大小：{}", task.getFileName(), task.getReceivedFileSize());
        if (task.getReceivedFileSize() == task.getFileSize()) {
            fileOutputStream.close();
            FileReceiveManager.remoeTask(channelHandlerContext.channel().id());
        }

    }
}
