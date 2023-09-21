package com.zzk.filehelper.bigdata;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.RandomAccessFile;

public class FileServer {
   public void run(final int port) throws Exception {
      EventLoopGroup bossGroup = new NioEventLoopGroup();
      EventLoopGroup workerGroup = new NioEventLoopGroup();
      try {
         ServerBootstrap b = new ServerBootstrap();
         b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .handler(new LoggingHandler())
          .childHandler(new ChannelInitializer<Channel>() {
             @Override
             protected void initChannel(final Channel ch) throws Exception {
                ch.pipeline().addLast(
                   new ChunkedWriteHandler(),
                   new FileServerHandler()
                );
             }
          });

         ChannelFuture future = b.bind(port).sync();
         future.channel().closeFuture().sync();
      } finally {
         bossGroup.shutdownGracefully();
         workerGroup.shutdownGracefully();
      }
   }

   public static void main(final String[] args) throws Exception {
      new FileServer().run(8080);
   }

   private class FileServerHandler extends SimpleChannelInboundHandler<String> {
      @Override
      public void channelActive(ChannelHandlerContext ctx) throws Exception {
         super.channelActive(ctx);
         RandomAccessFile raf = new RandomAccessFile("/path/to/your/file", "r");
         long fileLength = raf.length();
         ctx.writeAndFlush(new DefaultFileRegion(raf.getChannel(), 0, fileLength));
      }

      @Override
      protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
         // handle client message if any
      }

      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
         cause.printStackTrace();
         ctx.close();
      }
   }
}
