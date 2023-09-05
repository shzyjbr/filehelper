package com.zzk.filehelper.handler;

import com.zzk.filehelper.netty.message.Message;
import com.zzk.filehelper.netty.message.OnlineRequestMessage;
import com.zzk.filehelper.netty.message.OptionMessage;
import com.zzk.filehelper.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import java.net.InetSocketAddress;


/**
 * @author zzk
 * @date 2021/12/9
 * description netty客户端入站处理器，在这里可以实现对入站数据的操作
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接到服务器({})", ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {

        ByteBuf msg = packet.content();
        int len = msg.readableBytes();
        byte[] bytes = new byte[len];
        msg.readBytes(bytes);
        Serializer  serializer = Serializer.getByCode(Serializer.JSON_SERIALIZER);
        log.info("客户端接收到消息: {}", serializer.deserialize(bytes, OnlineRequestMessage.class));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端消息处理过程中出现错误：");
        cause.printStackTrace();
        ctx.close();
    }

}
