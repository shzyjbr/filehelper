package com.zzk.filehelper.netty;

import com.zzk.filehelper.handler.NettyClientHandler;
import com.zzk.filehelper.netty.message.Message;
import com.zzk.filehelper.netty.message.OnlineRequestMessage;
import com.zzk.filehelper.netty.message.OptionMessage;
import com.zzk.filehelper.netty.protocol.*;
import com.zzk.filehelper.netty.protocol.SequenceIdGenerator;
import com.zzk.filehelper.serialize.Serializer;
import com.zzk.filehelper.serialize.SerializerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
                            .addLast(new NettyClientHandler());
                }
            });
            // 这里替换为你的服务器地址
            Channel channel = bootstrap.bind(0).sync().channel();
            InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 8088);

            if (channel.isActive()) {
                OnlineRequestMessage onlineRequestMessage = new OnlineRequestMessage();
                onlineRequestMessage.setIp("192.168.0.1");
                onlineRequestMessage.setPort(9999);
                onlineRequestMessage.setSequenceId(SequenceIdGenerator.nextId());
                onlineRequestMessage.setMessageType(onlineRequestMessage.getMessageType());

                byte[] bytes = serializer.serialize(onlineRequestMessage);
                System.out.println(bytes.length);
                DatagramPacket packet = new DatagramPacket(Unpooled.copiedBuffer(bytes), addr);
                channel.writeAndFlush(packet).sync();
            }
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


}
