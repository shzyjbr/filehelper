package com.zzk.filehelper.netty.protocol;

//import com.zzk.filehelper.event.EventCenter;
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
        // 采用事件总线机制，将事件发出， 由靠近UI的地方进行处理
        // 设想：采用一个UI管理器，这个管理器持有UI引用，在该管理器中注册这种事件处理器
        log.info("服务器接收到文件预检请求消息:{}", msg);

//        EventCenter.postMessage(msg);
    }
}
