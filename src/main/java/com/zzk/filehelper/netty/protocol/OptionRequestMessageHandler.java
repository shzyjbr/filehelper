package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.netty.message.OptionRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 接收文件方在这里处理发送文件预检请求消息
 * @Author zhouzekun
 * @Date 2023/9/6 14:59
 */
@Slf4j
public class OptionRequestMessageHandler extends SimpleChannelInboundHandler<OptionRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, OptionRequestMessage msg) throws Exception {
        log.info("receive option request message:{}", msg);
    }
}
