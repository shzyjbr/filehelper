package com.zzk.filehelper;

import com.zzk.filehelper.netty.message.FileMetaMessage;
import com.zzk.filehelper.netty.message.OptionRequestMessage;
import com.zzk.filehelper.netty.protocol.*;
import com.zzk.filehelper.network.FileConfig;
import com.zzk.filehelper.network.FilePendingManager;
import com.zzk.filehelper.network.NetworkConfig;
import com.zzk.filehelper.network.PendingFileTask;
import com.zzk.filehelper.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.util.Arrays;
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
                            pipeline.addLast(new FileMetaAckMessageHandler());
                            pipeline.addLast(new FileContentMessageHandler());
                            pipeline.addLast(new FileContentAckMessageHandler());

                        }
                    });

            // 连接到服务器
            ChannelFuture f = b.connect("localhost", NetworkConfig.FILE_PORT).sync();

            // 起一个获取控制输入并发送文件的线程
//            new Thread(() -> {
//                if (f.channel().isActive()) {
//                    for (;;) {
//                        System.out.println("请输入文件路径：");
//                        Scanner scanner = new Scanner(System.in);
//                        String filePath = scanner.nextLine();
//                        File file = new File(filePath);
//                        if (!file.exists()) {
//                            System.out.println("该文件不存在,请重新输入");
//                            continue;
//                        }
//
//                        FileMetaMessage fileMetaMessage = new FileMetaMessage();
//                        fileMetaMessage.setFileName(filePath);
//                        fileMetaMessage.setFileSize(file.length());
//                        // 假设发送的每一个数据包的大小为1024*1024字节
//                        // 计算该文件可以被分割成多少包
//                        long fileSize = file.length();
//                        int total = (int) (fileSize / (1024 * 1024));
//                        if (fileSize % (1024 * 1024) != 0) {
//                            total++;
//                        }
//                        fileMetaMessage.setTotal(total);
//                        // 构造pendingtask
//                        PendingFileTask pendingFileTask = new PendingFileTask();
//                        pendingFileTask.setId(SequenceIdGenerator.nextId());
//                        pendingFileTask.setFileName(filePath);
//                        pendingFileTask.setFileSize(file.length());
//                        pendingFileTask.setTotal(total);
//                        FilePendingManager.addTask(f.channel().id(), pendingFileTask);
//                        try {
//                            f.channel().writeAndFlush(fileMetaMessage).sync();
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                    }
//                }
//            }).start();
//             sendFile(f);
            OptionRequestMessage optionRequestMessage = new OptionRequestMessage();
            optionRequestMessage.setFiles(Arrays.asList("C:/demo.txt"));
            optionRequestMessage.setSequenceId(1);
            f.channel().writeAndFlush(optionRequestMessage);
            // 等待直到连接关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭 EventLoopGroup，释放所有资源
            group.shutdownGracefully().sync();
        }
    }

    private static void sendFile(ChannelFuture f) {
        System.out.println("请输入文件路径：");
        Scanner scanner = new Scanner(System.in);
        String filePath = scanner.nextLine();
        filePath = filePath.trim();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("该文件不存在,请重新输入");
        }
        int id = SequenceIdGenerator.nextId();
        FileMetaMessage fileMetaMessage = new FileMetaMessage();
        // fileMetaMessage使用简单文件名
        fileMetaMessage.setId(id);
        fileMetaMessage.setFileName(file.getName());
        fileMetaMessage.setFileSize(file.length());
        // 假设发送的每一个数据包的大小为1024*1024字节
        // 计算该文件可以被分割成多少包
        long fileSize = file.length();
        int total = (int) (fileSize / FileConfig.PACKET_SIZE);
        if (fileSize % (1024 * 1024) != 0) {
            total++;
        }
        fileMetaMessage.setTotal(total);
        // 构造pendingtask
        PendingFileTask pendingFileTask = new PendingFileTask();
        pendingFileTask.setId(id);
        // pendingFileTask需要使用全路径
        pendingFileTask.setFileName(filePath);
        pendingFileTask.setFileSize(file.length());
        pendingFileTask.setTotal(total);
        FilePendingManager.addTask(f.channel().id(), pendingFileTask);
        try {
            f.channel().writeAndFlush(fileMetaMessage).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
