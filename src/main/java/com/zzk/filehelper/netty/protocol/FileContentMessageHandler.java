package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.domain.ReceiveFileTask;
import com.zzk.filehelper.netty.message.FileContentAckMessage;
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
    protected void channelRead0(ChannelHandlerContext ctx, FileContentMessage msg) throws Exception {
        log.info("FileContentMessageHandler[packetNumber:{},length:{},id:{}]", msg.getPacketNumber(),
                msg.getContentLength(), msg.getId());
        // 取出对应的传输任务
        ReceiveFileTask task = FileReceiveManager.getTask(ctx.channel().id());
        // 写入文件
        FileOutputStream fileOutputStream = task.getFileOutputStream();
        fileOutputStream.write(msg.getData());
        // 更新ReceiveFileTask
        task.setReceivedFileSize(task.getReceivedFileSize() + msg.getContentLength());
        log.info("文件：{}, 当前写入文件大小：{}", task.getFileName(), task.getReceivedFileSize());
        if (task.getReceivedFileSize() == task.getFileSize()) {
            // 关闭接收任务
            task.close();
            FileReceiveManager.removeTask(ctx.channel().id());
        }
        // 发送确认消息
        FileContentAckMessage fileContentAckMessage = new FileContentAckMessage();
        fileContentAckMessage.setId(msg.getId());
        fileContentAckMessage.setAckNumber(msg.getPacketNumber());
        fileContentAckMessage.setPacketSize(msg.getContentLength());
        System.out.println(fileContentAckMessage);
        ctx.writeAndFlush(fileContentAckMessage);

    }
}
