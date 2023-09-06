package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.netty.message.OptionReplyMessage;
import com.zzk.filehelper.netty.message.OptionRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 发送文件方在这里处理文件预检回复消息
 * @Author zhouzekun
 * @Date 2023/9/6 16:20
 */
@Slf4j
public class OptionReplyMessageHandler extends SimpleChannelInboundHandler<OptionReplyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, OptionReplyMessage msg) throws Exception {
        log.info("receive option reply message:{}", msg);
    }
}
