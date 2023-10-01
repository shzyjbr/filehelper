package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.domain.SendFileTask;
import com.zzk.filehelper.netty.message.FileContentAckMessage;
import com.zzk.filehelper.netty.message.FileContentMessage;
import com.zzk.filehelper.network.FileSendManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.util.Objects;

/**
 * 提供给传送文件一方使用，当对端收到数据包后会回复一个确认消息，确认消息中包含了确认号，表示已经接收到的数据包的编号
 *
 * @Author kelton
 * @Date 2023/9/30 23:16
 * @Version 1.0
 */
@Slf4j
public class FileContentAckMessageHandler extends SimpleChannelInboundHandler<FileContentAckMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileContentAckMessage ackMessage) throws Exception {
        log.info("FileContentAckMessage：{}", ackMessage);
        // 接着进行传送
        SendFileTask task = FileSendManager.getTask(ctx.channel().id());
        if (task.getId() == ackMessage.getId()) {
            // 更新SendFileTask
            task.setLastAck(task.getLastAck() + 1);
            task.setReceivedFileSize(task.getReceivedFileSize() + ackMessage.getPacketSize());
            if (task.getReceivedFileSize() == task.getFileSize()) {
                // 传输完成，关闭本端文件， 另一种思路是接着给对端发送一个文件传输完毕的消息
                task.close();
                // 移除该任务
                FileSendManager.removeTask(ctx.channel().id());
                return;
            }

            FileContentMessage fileContentMessage = new FileContentMessage();
            FileInputStream fileInputStream = task.getFileInputStream();
            long currentPacketNumber = ackMessage.getAckNumber() + 1;
            byte[] data;
            int c = 0;
            if (currentPacketNumber < task.getTotal()) {
                // 非最后一个包
                data = new byte[task.getPacketSize()];
                while (c < task.getPacketSize()) {
                    c += fileInputStream.read(data);
                }
            } else {
                // 最后一个包
                int remain = (int) (task.getFileSize() % task.getPacketSize());
                data = new byte[remain];
                while (c < remain) {
                    c += fileInputStream.read(data);
                }
            }
            fileContentMessage.setContentLength(data.length);
            fileContentMessage.setData(data);
            fileContentMessage.setPacketNumber(ackMessage.getAckNumber() + 1);
            fileContentMessage.setTotalPackets(task.getTotal());
            fileContentMessage.setId(ackMessage.getId());
            // 发送下一个数据包
            ctx.writeAndFlush(fileContentMessage);

        }
    }
}
