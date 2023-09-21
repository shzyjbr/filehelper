package com.zzk.filehelper.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * 在这里保存和其他客户端的文件传输channel
 */
@Slf4j
public class ChannelProvider {
    private static EventLoopGroup workerGroup;
    private static final Bootstrap bootstrap = initializeBootstrap();
    /**
     * key是ip:port， value是channel
     */
    private static final Map<String, Channel> channels = new ConcurrentHashMap<>();

    public static void shutdown() {
        workerGroup.shutdownGracefully();
    }

    /**
     * 获取channel，可能来自缓存，也可能是新建的
     * @param ip
     * @param port
     * @return
     * @throws InterruptedException
     */
    public static Channel getChannel(String ip, int port) throws InterruptedException {
        String key = ip + ":" + port;
        if (channels.containsKey(key)) {
            Channel channel = channels.get(key);
            if (channel.isActive()) {
                return channel;
            } else {
                channels.remove(key);
            }
        }
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline()
                        // todo 替换成文件传输的handler
                        .addLast();
            }
        });
        Channel channel = null;
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
            channel = connect(bootstrap, inetSocketAddress);
        } catch (ExecutionException e) {
            log.error("连接客户端时发生错误，", e);
            return null;
        }
        channels.put(key, channel);
        return channel;

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

    private static Bootstrap initializeBootstrap() {
        workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                //连接超时时间，超过该时间则连接失败, 5s
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //开启TCP底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //TCP默认开启Nagle算法，关闭它。Nagle算法会尽可能地发送大数据块，从而导致小块数据在缓冲区停留
                .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }
}
