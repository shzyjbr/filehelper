package com.zzk.filehelper.bigdata;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.RandomAccessFile;

public class FileClient {
   public void run(final String host, final int port) throws Exception {
      EventLoopGroup group = new NioEventLoopGroup();
      try {
         Bootstrap b = new Bootstrap();
         b.group(group)
                 .channel(NioSocketChannel.class)
                 .handler(new LoggingHandler())
                 .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(final Channel ch) throws Exception {
                       ch.pipeline().addLast(
                               new LengthFieldBasedFrameDecoder(8192, 0, 8, 0, 8),
                               new ChunkedWriteHandler(),
                               new FileClientHandler()
                       );
                    }
                 });

         ChannelFuture future = b.connect(host, port).sync();
         future.channel().closeFuture().sync();
      } finally {
         group.shutdownGracefully();
      }
   }

   public static void main(final String[] args) throws Exception {
      new FileClient().run("localhost", 8080);
   }

   private class FileClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
      private RandomAccessFile file;
      private long position = 0;

      @Override
      public void channelActive(ChannelHandlerContext ctx) throws Exception {
         super.channelActive(ctx);
         file = new RandomAccessFile("/path/to/save/file", "rw");
      }

      @Override
      protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
         while (msg.readableBytes() > 0) {
            file.writeByte(msg.readByte());
            position++;
         }
      }

      @Override
      public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
         file.close();
         ctx.close();
      }

      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
         cause.printStackTrace();
         ctx.close();
      }
   }
}
