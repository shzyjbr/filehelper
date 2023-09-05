package com.zzk.filehelper.netty.protocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class DatagramPacketHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        ctx.fireChannelRead(packet.content());
    }
}
