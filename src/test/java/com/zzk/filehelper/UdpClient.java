package com.zzk.filehelper;

import com.zzk.filehelper.handler.ServerStatusHandler;
import com.zzk.filehelper.netty.message.MessageConfig;
import com.zzk.filehelper.netty.message.OnlineRequestMessage;
import com.zzk.filehelper.netty.protocol.SequenceIdGenerator;
import com.zzk.filehelper.network.NetworkConfig;
import com.zzk.filehelper.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @Author kelton
 * @Date 2023/8/31 22:19
 * @Version 1.0
 */

@Slf4j
public class UdpClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerGroup= new NioEventLoopGroup();
        try {
            Serializer serializer = Serializer.getByCode(Serializer.JSON_SERIALIZER);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true);
            bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {
                @Override
                protected void initChannel(NioDatagramChannel socketChannel) {
                    socketChannel.pipeline()
                            .addLast(new ServerStatusHandler());
                }
            });
            // 这里替换为你的服务器地址
            Channel channel = bootstrap.bind(0).sync().channel();
            InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 9099);

            if (channel.isActive()) {
                OnlineRequestMessage onlineRequestMessage = new OnlineRequestMessage();
                onlineRequestMessage.setIp("192.168.0.104");
                onlineRequestMessage.setPort(NetworkConfig.FILE_PORT);
                onlineRequestMessage.setSequenceId(SequenceIdGenerator.nextId());
                onlineRequestMessage.setMessageType(onlineRequestMessage.getMessageType());

                byte[] messageContent = serializer.serialize(onlineRequestMessage);
                System.out.println(messageContent.length);
                byte[] messageType = new byte[]{MessageConfig.ONLINE_REQUEST_MESSAGE};
                DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(messageType, messageContent), addr);
                channel.writeAndFlush(packet).sync();
                channel.closeFuture().await();
            }
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


}
