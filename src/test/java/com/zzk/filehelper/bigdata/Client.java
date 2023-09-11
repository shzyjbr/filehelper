package com.zzk.filehelper.bigdata;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Client {
    private static final int MAGIC_WORD = 1234;  // Change this to your magic word

    public static void main(String[] args) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                        private String fileName;
                        private long fileSize;
                        private long received;
                        private File file;
                        private BufferedOutputStream outputFile;

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                            while (msg.isReadable()) {
                                // Read file properties only once
                                if (fileName == null) {
                                    if (msg.readableBytes() < 4) return;
                                    int magicWord = msg.readInt();
                                    assert magicWord == MAGIC_WORD;

                                    if (msg.readableBytes() < 4) return;
                                    int fileNameLength = msg.readInt();

                                    if (msg.readableBytes() < fileNameLength) return;
                                    byte[] fileNameBytes = new byte[fileNameLength];
                                    msg.readBytes(fileNameBytes);
                                    fileName = new String(fileNameBytes);
                                    System.out.println(fileName);

                                    if (msg.readableBytes() < 8) return;
                                    fileSize = msg.readLong();

                                    received = 0;
                                    file = new File("sds.zip");
                                    outputFile = new BufferedOutputStream(new FileOutputStream(file));
                                }

                                while (msg.isReadable() && received < fileSize) {
                                    byte[] bytes = new byte[msg.readableBytes()];
                                    msg.readBytes(bytes);
                                    outputFile.write(bytes);
                                    received += bytes.length;
                                }

                                if (received >= fileSize) {
                                    outputFile.close();
                                    System.out.println("File received (" + fileSize + " bytes)");
                                    ctx.close();
                                }
                            }
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            // Handle connection close, print warning if file transfer is incomplete
                            if (received < fileSize) {
                                System.out.println("File transfer not complete: " + received + " / " + fileSize + " bytes");
                            }
                            super.channelInactive(ctx);
                        }
                    });
                }
            });

            ChannelFuture f = b.connect("localhost", 9999).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
