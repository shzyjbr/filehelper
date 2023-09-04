package com.zzk.filehelper.netty;

import com.zzk.filehelper.handler.NettyClientHandler;
import com.zzk.filehelper.netty.message.Message;
import com.zzk.filehelper.netty.message.OptionMessage;
import com.zzk.filehelper.netty.protocol.MessageCodecSharable;
import com.zzk.filehelper.netty.protocol.NettyServerHandler;
import com.zzk.filehelper.netty.protocol.ProcotolFrameDecoder;
import com.zzk.filehelper.serialize.Serializer;
import com.zzk.filehelper.serialize.SerializerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author kelton
 * @Date 2023/8/31 22:19
 * @Version 1.0
 */

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UdpClient {
    public static void main(String[] args) throws InterruptedException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8088);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        EventLoopGroup workerGroup= new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    //连接超时时间，超过该时间则连接失败, 5s
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    //开启TCP底层心跳机制
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //TCP默认开启Nagle算法，关闭它。Nagle算法会尽可能地发送大数据块，从而导致小块数据在缓冲区停留
                    .option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline()
                            .addLast(new ProcotolFrameDecoder())
                            .addLast(MESSAGE_CODEC)
                            .addLast(new NettyClientHandler());
                }
            });
            Channel channel = null;
            try {
                channel = connect(bootstrap, inetSocketAddress);
                OptionMessage optionMessage = new OptionMessage();
                optionMessage.setFiles(Arrays.asList("readme.txt", "sout.txt"));
                sendRequest(channel, optionMessage);
            } catch (ExecutionException e) {
                log.error("连接客户端时发生错误，", e);
            }
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static Channel connect(Bootstrap bootstrap, InetSocketAddress socketAddress) throws InterruptedException, ExecutionException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(socketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("客户端连接成功");
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    public static void sendRequest(Channel channel, Message rpcRequest) {
        channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
                log.info("客户端成功发送消息：{}", rpcRequest.toString());
            } else {
                future1.channel().close();
                log.error("发送消息时发生错误:", future1.cause());
            }
        });
    }
}
