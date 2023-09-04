package com.zzk.filehelper.netty.register;

import com.zzk.filehelper.netty.protocol.MessageCodecSharable;
import com.zzk.filehelper.netty.protocol.ProcotolFrameDecoder;
import com.zzk.filehelper.netty.protocol.NettyServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @Author kelton
 * @Date 2023/7/3 21:29
 * @Version 1.0
 */
public class UdpServer {
    public static void main(String[] args) throws InterruptedException {
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    // 主线程处理
                    .channel(NioDatagramChannel.class)
                    // 广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    // 设置读缓冲区为2M
                    .option(ChannelOption.SO_RCVBUF, 2048 * 1024)
                    // 设置写缓冲区为1M
                    .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {

                        @Override
                        protected void initChannel(NioDatagramChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ProcotolFrameDecoder());
                            pipeline.addLast(MESSAGE_CODEC);
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            ChannelFuture f = bootstrap.bind(8088).sync();
            System.out.println("服务器正在监听......");
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }

}
