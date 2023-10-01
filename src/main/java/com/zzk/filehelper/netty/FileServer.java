package com.zzk.filehelper.netty;

import com.zzk.filehelper.netty.protocol.*;
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
                        pipeline.addLast(new CommonDecoder());
                        pipeline.addLast(new CommonEncoder(serializer));
                        pipeline.addLast(new OptionRequestMessageHandler());
                        pipeline.addLast(new OptionReplyMessageHandler());
                        pipeline.addLast(new FileMetaMessageHandler());
                        pipeline.addLast(new FileMetaAckMessageHandler());
                        pipeline.addLast(new FileContentMessageHandler());
                        pipeline.addLast(new FileContentAckMessageHandler());
                    }
                });

        ChannelFuture f = b.bind(port).sync();
    }

    public void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
