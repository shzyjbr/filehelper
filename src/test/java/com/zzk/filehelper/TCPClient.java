package com.zzk.filehelper;

import com.zzk.filehelper.netty.protocol.CommonDecoder;
import com.zzk.filehelper.netty.protocol.CommonEncoder;
import com.zzk.filehelper.netty.protocol.OptionRequestMessageHandler;
import com.zzk.filehelper.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
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
                            ch.pipeline().addLast(
                                    new CommonEncoder(serializer),
                                    new CommonDecoder(), // 添加你的解码器
                                    new OptionRequestMessageHandler(), // 添加处理 OptionMessage 的 handler
                                    // 如果还有其他类型的消息，你可以继续添加对应的 handler
                                    // new FileMessageHandler(),
                                    // new ReplyMessageHandler(),
                                    new EchoClientHandler()
                                    );
                        }
                    });

            // 连接到服务器
            ChannelFuture f = b.connect("localhost", 9999).sync();
            // 等待直到连接关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭 EventLoopGroup，释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}
