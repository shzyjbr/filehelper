package com.zzk.filehelper.netty;

import com.zzk.filehelper.handler.ServerStatusHandler;
import com.zzk.filehelper.netty.message.MessageConfig;
import com.zzk.filehelper.netty.message.OfflineMessage;
import com.zzk.filehelper.netty.message.OnlineRequestMessage;
import com.zzk.filehelper.netty.protocol.SequenceIdGenerator;
import com.zzk.filehelper.network.IpUtil;
import com.zzk.filehelper.network.NetworkConfig;
import com.zzk.filehelper.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @Author kelton
 * @Date 2023/10/9 21:24
 * @Version 1.0
 */
public class StatusServer {

    private final EventLoopGroup statusGroup = new NioEventLoopGroup();
    private final Bootstrap statusBootstrap = new Bootstrap();

    private Channel statusChannel;

    public void run(int registerPort) {
        try {
            statusBootstrap.group(statusGroup)
                    // 主线程处理
                    .channel(NioDatagramChannel.class)
                    // 广播
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {

                        @Override
                        protected void initChannel(NioDatagramChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline
                                    .addLast(new ServerStatusHandler());
                        }
                    });
            statusChannel = statusBootstrap.bind(registerPort).sync().channel();
            OnlineRequestMessage onlineRequestMessage = new OnlineRequestMessage();
            onlineRequestMessage.setIp(IpUtil.getLocalIp4Address());
            onlineRequestMessage.setPort(NetworkConfig.FILE_PORT);
            onlineRequestMessage.setSequenceId(SequenceIdGenerator.nextId());
            Serializer serializer = Serializer.getByCode(Serializer.JSON_SERIALIZER);
            byte[] messageContent = serializer.serialize(onlineRequestMessage);
            byte[] messageType = new byte[]{MessageConfig.ONLINE_REQUEST_MESSAGE};
            InetSocketAddress addr = new InetSocketAddress("255.255.255.255", NetworkConfig.REGISTER_PORT);
            DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(messageType, messageContent), addr);
            statusChannel.writeAndFlush(packet);
            System.out.println("状态服务器已启动，正在监听端口：" + registerPort);
            // 这里不能出现阻塞当前线程的操作，否则无法调出JavaFX主窗口
        } catch (InterruptedException e) {
            System.out.println("状态服务器启动异常，开始关闭客户端...");
            System.exit(-1);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() throws SocketException, InterruptedException {
        // 下线前广播一条下线消息
        OfflineMessage offlineMessage = new OfflineMessage();
        offlineMessage.setIp(IpUtil.getLocalIp4Address());
        offlineMessage.setPort(NetworkConfig.FILE_PORT);
        offlineMessage.setSequenceId(SequenceIdGenerator.nextId());
        Serializer serializer = Serializer.getByCode(Serializer.JSON_SERIALIZER);
        byte[] messageContent = serializer.serialize(offlineMessage);
        byte[] messageType = new byte[]{MessageConfig.OFFLINE_MESSAGE};
        InetSocketAddress addr = new InetSocketAddress("255.255.255.255", NetworkConfig.REGISTER_PORT);
        DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(messageType, messageContent), addr);
        statusChannel.writeAndFlush(packet).sync();
        // 关闭资源
        statusChannel.close();
        statusGroup.shutdownGracefully();
    }
}
