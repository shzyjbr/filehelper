package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.domain.SendFileTask;
import com.zzk.filehelper.netty.message.FileContentMessage;
import com.zzk.filehelper.netty.message.FileMetaAckMessage;
import com.zzk.filehelper.network.FileConfig;
import com.zzk.filehelper.network.FilePendingManager;
import com.zzk.filehelper.network.FileSendManager;
import com.zzk.filehelper.network.PendingFileTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;

/**
 * @Author zhouzekun
 * @Date 2023/10/1 10:03
 */
@Slf4j
public class FileMetaAckMessageHandler extends SimpleChannelInboundHandler<FileMetaAckMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileMetaAckMessage msg) throws Exception {
        log.info("FileMetaAckMessage：{}", msg.toString());
        PendingFileTask task = FilePendingManager.getTask(ctx.channel().id());
        log.info("PendingFileTask:{}", task);
        if (task == null) {
            // 不存在该task，出现异常，异常处理
            throw new RuntimeException("处理PendingFileTask异常，该任务不存在");
        }
        // 将该任务转化成sendTask并移入SendManager
        SendFileTask sendFileTask = new SendFileTask();
        sendFileTask.setId(task.getId());
        sendFileTask.setFileName(task.getFileName());
        File file = new File(task.getFileName());
        if (!file.exists()) {
            throw new RuntimeException("构造SendFileTask异常，文件不存在:" + task.getFileName());
        }
        sendFileTask.setFileSize(file.length());
        sendFileTask.setReceivedFileSize(0);
        // 初始为0，待收到第一个数据包的确认消息会+1
        sendFileTask.setLastAck(0);
        sendFileTask.setPacketSize(FileConfig.PACKET_SIZE);
        // 计算多少个包
        long total = file.length() / FileConfig.PACKET_SIZE;
        if (file.length() % FileConfig.PACKET_SIZE != 0)
            total+=1;
        sendFileTask.setTotal(total);
        FileInputStream fileInputStream = new FileInputStream(file);
        sendFileTask.setFileInputStream(fileInputStream);
        FileSendManager.addTask(ctx.channel().id(), sendFileTask);

        // 发送第一个内容包
        FileContentMessage fileContentMessage = new FileContentMessage();
        byte[] data;
        int c = 0;
        // 文件本身比较小，小于设定的包大小
        if (file.length() < FileConfig.PACKET_SIZE) {
            data = new byte[(int)file.length()];
            while (c < file.length()) {
                c += fileInputStream.read(data);
            }
        } else {
            data = new byte[sendFileTask.getPacketSize()];
            while (c < sendFileTask.getPacketSize()) {
                c += fileInputStream.read(data);
            }
        }
        fileContentMessage.setId(msg.getId());
        fileContentMessage.setContentLength(data.length);
        fileContentMessage.setData(data);
        // 第一个内容包
        fileContentMessage.setPacketNumber(1);
        fileContentMessage.setTotalPackets(sendFileTask.getTotal());
        System.out.println("fileContentMessage id: " + fileContentMessage.getId());
        // 发送
        ctx.writeAndFlush(fileContentMessage);

    }
}
