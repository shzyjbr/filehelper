package com.zzk.filehelper.bigdata;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.RandomAccessFile;

public class Server {
    private static final int MAGIC_WORD = 1234;  // Change this to your magic word

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChunkedWriteHandler());
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            super.channelActive(ctx);

                            File file = new File("D:\\temp\\BigFileTransfer\\client-receive-jdk-17.0.8_windows-x64_bin.zip");
                            RandomAccessFile raf = new RandomAccessFile(file, "r");
                            long fileLength = raf.length();

                            // Write magic word
                            ctx.write(ctx.alloc().buffer(4).writeInt(MAGIC_WORD));

                            // Write file name
                            ByteBuf fileNameByteBuf = ctx.alloc().buffer().writeBytes(file.getName().getBytes());
                            ctx.write(ctx.alloc().buffer(4).writeInt(fileNameByteBuf.readableBytes()));  // File name length
                            ctx.write(fileNameByteBuf);  // File name

                            // Write file length
                            ctx.write(ctx.alloc().buffer(8).writeLong(fileLength));

                            // Write file
                            ctx.writeAndFlush(new MagicWordChunkedFile(raf, MAGIC_WORD)).addListener(ChannelFutureListener.CLOSE);
                        }
                    });
                }
            });

            ChannelFuture f = b.bind(9999).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
