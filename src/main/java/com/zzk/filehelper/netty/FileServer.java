package com.zzk.filehelper.netty;

import com.zzk.filehelper.handler.FileReceiveServerHandler;
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
                        // 加入文件接收功能
                        pipeline.addLast(new FileReceiveServerHandler());
                        pipeline.addLast(new CommonDecoder());
                        pipeline.addLast(new CommonEncoder(serializer));
                        pipeline.addLast(new FileMessageHandler());
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
