package com.zzk.filehelper;

import com.zzk.filehelper.handler.NettyServerHandler;
import com.zzk.filehelper.netty.protocol.*;
import com.zzk.filehelper.serialize.Serializer;
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
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    // 主线程处理
                    .channel(NioDatagramChannel.class)
                    // 广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {

                        @Override
                        protected void initChannel(NioDatagramChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline
                                    .addLast(new NettyServerHandler());
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
