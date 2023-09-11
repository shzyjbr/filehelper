package com.zzk.filehelper;

import com.zzk.filehelper.netty.message.FileMessage;
import com.zzk.filehelper.netty.protocol.SequenceIdGenerator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("连接成功");

        FileMessage fileMessage = new FileMessage();
        fileMessage.setFile("a.txt");
        fileMessage.setSequenceId(SequenceIdGenerator.nextId());
        fileMessage.setMessageType(fileMessage.getMessageType());
        ctx.writeAndFlush(fileMessage);



    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Client channelRead");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
