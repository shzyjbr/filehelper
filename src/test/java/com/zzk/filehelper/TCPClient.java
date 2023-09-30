package com.zzk.filehelper;

import com.zzk.filehelper.netty.message.FileMetaMessage;
import com.zzk.filehelper.netty.protocol.*;
import com.zzk.filehelper.network.NetworkConfig;
import com.zzk.filehelper.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.util.Scanner;

/**
 * 入站事件：从ChannelPipeline的头部开始流向尾部
 * 出站事件：从ChannelPipeline的尾部开始流向头部
 * @Author zhouzekun
 * @Date 2023/9/6 15:10
 */
public class TCPClient {
    public static void main(String[] args) throws Exception {
        // 创建 EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        Serializer serializer = Serializer.getByCode(Serializer.JSON_SERIALIZER);
        try {
            // 创建并配置 Bootstrap
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // todo 设计客户端发送的相关handler， 服务端收到消息后也要进行回复对应的确认消息
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new OptionRequestMessageHandler());
                            pipeline.addLast(new OptionReplyMessageHandler());
                            pipeline.addLast(new FileMetaMessageHandler());
                            pipeline.addLast(new FileContentMessageHandler());

                        }
                    });

            // 连接到服务器
            ChannelFuture f = b.connect("localhost", NetworkConfig.FILE_PORT).sync();
            // 等待直到连接关闭
            f.channel().closeFuture().sync();
            // 起一个获取控制输入并发送文件的线程
            new Thread(() -> {
                if (f.channel().isActive()) {
                    for (;;) {
                        System.out.println("请输入文件路径：");
                        Scanner scanner = new Scanner(System.in);
                        String filePath = scanner.nextLine();
                        File file = new File(filePath);
                        if (!file.exists()) {
                            System.out.println("该文件不存在,请重新输入");
                            continue;
                        }

                        FileMetaMessage fileMetaMessage = new FileMetaMessage();
                        fileMetaMessage.setFileName(filePath);
                        fileMetaMessage.setFileSize(file.length());
                        // 假设发送的每一个数据包的大小为1024*1024字节
                        // 计算该文件可以被分割成多少包
                        long fileSize = file.length();
                        int packetSize = (int) (fileSize / (1024 * 1024));
                        if (fileSize % (1024 * 1024) != 0) {
                            packetSize++;
                        }
                        fileMetaMessage.setPacketSize(packetSize);
                        f.channel().writeAndFlush(fileMetaMessage);
                        // 随后发送文件内容
                        
                    }
                }
            }).start();
        } finally {
            // 关闭 EventLoopGroup，释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}
