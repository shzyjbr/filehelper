package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.domain.ReceiveFileTask;
import com.zzk.filehelper.netty.message.FileMetaAckMessage;
import com.zzk.filehelper.netty.message.FileMetaMessage;
import com.zzk.filehelper.network.FileReceiveManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;

import static com.zzk.filehelper.network.FileConfig.BASE_DIR;

/**
 * @Author kelton
 * @Date 2023/9/27 0:48
 * @Version 1.0
 */
@Slf4j
public class  FileMetaMessageHandler extends SimpleChannelInboundHandler<FileMetaMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileMetaMessage msg) throws Exception {
        log.info("FileMetaMessage：{}", msg.toString());
        ChannelId id = ctx.channel().id();
        // 以当前channel为标识，取出或者复制对应的传输任务
        String fileName = msg.getFileName();
        // 暂时没用到，后面统计进度用
        long fileSize = msg.getFileSize();
        long receivedFileSize = 0;
        File file = new File(BASE_DIR + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ReceiveFileTask receiveFileTask = new ReceiveFileTask();
        receiveFileTask.setFileName(fileName);
        receiveFileTask.setFileSize(fileSize);
        receiveFileTask.setReceivedFileSize(receivedFileSize);
        receiveFileTask.setFileOutputStream(fileOutputStream);
        FileReceiveManager.addTask(id, receiveFileTask);
        // 发送文件元数据确认消息
        FileMetaAckMessage fileMetaAckMessage = new FileMetaAckMessage();
        fileMetaAckMessage.setId(msg.getId());
        fileMetaAckMessage.setFilename(fileName);
        ctx.writeAndFlush(fileMetaAckMessage);


    }
}
