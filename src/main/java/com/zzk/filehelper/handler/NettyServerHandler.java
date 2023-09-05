package com.zzk.filehelper.handler;

import com.zzk.filehelper.netty.message.OnlineReplyMessage;
import com.zzk.filehelper.netty.message.OnlineRequestMessage;
import com.zzk.filehelper.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;


/**
 * @author zzk
 * @date 2023年9月5日
 * description netty udp服务器入站处理器，在这里实现对其他客户端上线消息的回复
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {

        ByteBuf msg = packet.content();
        int len = msg.readableBytes();
        byte[] bytes = new byte[len];
        msg.readBytes(bytes);
        Serializer  serializer = Serializer.getByCode(Serializer.JSON_SERIALIZER);
        OnlineRequestMessage deserialize = serializer.deserialize(bytes, OnlineRequestMessage.class);
        log.info("服务端接收到客户端上线消息: {}",deserialize);
        OnlineReplyMessage onlineReplyMessage = new OnlineReplyMessage();
        onlineReplyMessage.setIp("192.168.0.2");
        onlineReplyMessage.setPort(8888);
        onlineReplyMessage.setSequenceId(deserialize.getSequenceId());
        onlineReplyMessage.setMessageType(onlineReplyMessage.getMessageType());
        byte[] reply = serializer.serialize(onlineReplyMessage);
        ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(reply), packet.sender()));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("消息处理过程中出现错误：");
        cause.printStackTrace();
        ctx.close();
    }

}
