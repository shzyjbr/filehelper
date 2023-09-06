package com.zzk.filehelper.netty;

import com.zzk.filehelper.netty.protocol.CommonDecoder;
import com.zzk.filehelper.netty.protocol.CommonEncoder;
import com.zzk.filehelper.netty.protocol.OptionReplyMessageHandler;
import com.zzk.filehelper.netty.protocol.OptionRequestMessageHandler;
import com.zzk.filehelper.serialize.Serializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author zhouzekun
 * @Date 2023/9/6 11:05
 */
public class FileServer {

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();

    public void run(int port) throws Exception {
        Serializer serializer = Serializer.getByCode(Serializer.JSON_SERIALIZER);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline
                                .addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(serializer));
//                                .addLast(new OptionRequestMessageHandler())
//                                .addLast(new OptionReplyMessageHandler());
                    }
                });

        ChannelFuture f = b.bind(port).sync();
//        f.channel().closeFuture().sync();
    }

    public void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
