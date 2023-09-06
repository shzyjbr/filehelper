package com.zzk.filehelper;

import com.zzk.filehelper.handler.NettyServerHandler;
import com.zzk.filehelper.netty.FileServer;
import com.zzk.filehelper.netty.message.MessageConfig;
import com.zzk.filehelper.netty.message.OfflineMessage;
import com.zzk.filehelper.serialize.Serializer;
import com.zzk.filehelper.state.SceneManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;

public class HelperApplication extends Application {

    private double offsetX, offsetY;
    private Scene scene;

    private EventLoopGroup statusGroup;
    private Bootstrap statusBootstrap;

    private Channel statusChannel;

    private FileServer fileServer;

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneManager.instance.mainStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        SceneManager.instance.setMainPane(root);


        scene = new Scene(root, null);
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitHint("");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/logo.png")).toExternalForm()));
        primaryStage.setTitle("文件传输助手");
        primaryStage.show();


        // scene.setOnMousePressed(event -> {
        //     offsetX = event.getSceneX();
        //     offsetY = event.getSceneY();
        // });
        //
        // scene.setOnMouseDragged(event -> {
        //     primaryStage.setX(event.getScreenX()-offsetX);
        //     primaryStage.setY(event.getScreenY()-offsetY);
        // });

    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("启动状态服务器中...");
        startStatusServer();
        System.out.println("启动文件监听服务器中...");
        fileServer = new FileServer();
        fileServer.run(9999);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("关闭资源中...");
        offline();
//        清理资源
        if (statusChannel != null) {
            statusChannel.close().sync();
        }
        if (statusGroup != null) {
            statusGroup.shutdownGracefully().sync();
        }
        fileServer.shutdown();

    }

    public static void main(String[] args) {
        System.out.println("launch main win");
        launch();
    }


    private void startStatusServer() throws InterruptedException {
        statusGroup = new NioEventLoopGroup();
        try {
            statusBootstrap = new Bootstrap();
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
                                    .addLast(new NettyServerHandler());
                        }
                    });
            int listenPort = 9099;
            statusChannel = statusBootstrap.bind(listenPort).sync().channel();
            System.out.println("状态服务器已启动，正在监听端口：" + listenPort);
            // 这里不能出现阻塞当前线程的操作，否则无法调出JavaFX主窗口
        } catch (InterruptedException e) {
            System.out.println("状态服务器启动异常，开始关闭客户端...");
            System.exit(-1);
        }
    }

    private void offline() {
        OfflineMessage offlineMessage = new OfflineMessage();
        offlineMessage.setIp("192.168.0.1");
        offlineMessage.setPort(8088);
        Serializer serializer = Serializer.getByCode(Serializer.JSON_SERIALIZER);
        byte[] serialize = serializer.serialize(offlineMessage);

        try {
            ByteBuf buf1 = Unpooled.buffer(1);
            buf1.writeByte(MessageConfig.OFFLINE_MESSAGE);
            statusChannel.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer(buf1, Unpooled.copiedBuffer(serialize)),
                    new InetSocketAddress("127.0.0.1", 8088))).sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}