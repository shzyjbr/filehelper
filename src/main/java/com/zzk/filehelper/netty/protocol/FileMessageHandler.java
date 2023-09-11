package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.netty.message.FileMessage;
import com.zzk.filehelper.state.FileManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;

/**
 * @Author zhouzekun
 * @Date 2023/9/11 17:16
 */
public class FileMessageHandler extends SimpleChannelInboundHandler<FileMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileMessage msg) throws Exception {
        File file = msg.getFile();
        System.out.println("receive file from peer: " + file.getName());
        FileManager.INSTANCE.setLength( file.length());
        FileManager.INSTANCE.setFilename(file.getName());
        // 一个数据包传递到pipeline的尾部，经过处理，重复利用了这个packet，再从尾部到头部写出
        msg.setACK(msg.getACK() + 1);
        ctx.writeAndFlush(msg);
    }
}
