package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.netty.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author kelton
 * @Date 2023/7/3 38:29
 * @Version 1.0
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {
 
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
        log.info("服务端接收到上线消息：{}",msg);

    }
}