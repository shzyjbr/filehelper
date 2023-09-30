package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.netty.message.FileAckMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 提供给传送文件一方使用，当对端收到数据包后会回复一个确认消息，确认消息中包含了确认号，表示已经接收到的数据包的编号
 * @Author kelton
 * @Date 2023/9/30 23:16
 * @Version 1.0
 */
public class FileAckMessageHandler extends SimpleChannelInboundHandler<FileAckMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FileAckMessage fileAckMessage) throws Exception {
        // 接着进行传送

    }
}
