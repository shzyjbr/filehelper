package com.zzk.filehelper;

import com.google.common.eventbus.Subscribe;
import com.zzk.filehelper.event.EventCenter;
import com.zzk.filehelper.event.OptionRequestEvent;
import com.zzk.filehelper.event.manager.UIManager;
import com.zzk.filehelper.handler.ServerStatusHandler;
import com.zzk.filehelper.netty.FileServer;
import com.zzk.filehelper.netty.StatusServer;
import com.zzk.filehelper.netty.message.MessageConfig;
import com.zzk.filehelper.netty.message.OfflineMessage;
import com.zzk.filehelper.netty.message.OnlineRequestMessage;
import com.zzk.filehelper.netty.message.OptionRequestMessage;
import com.zzk.filehelper.netty.protocol.SequenceIdGenerator;
import com.zzk.filehelper.network.IpUtil;
import com.zzk.filehelper.network.NetworkConfig;
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
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Objects;

public class HelperApplication extends Application {

    private static final double MIN_WIDTH = 400; // 最小宽度
    private static final double MIN_HEIGHT = 300; // 最小高度

    private double offsetX, offsetY;
    private Scene scene;


    private FileServer fileServer;
    private StatusServer statusServer;

    @Override
    public void start(Stage primaryStage) throws IOException {


        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        
        scene = new Scene(root, null);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setFullScreenExitHint("");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/logo.png")).toExternalForm()));
        primaryStage.setTitle("文件传输助手");
        primaryStage.show();

        SceneManager.instance.setMainPane(root);
        SceneManager.instance.setMaskStage(primaryStage);

    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("启动状态服务器中...");
        statusServer = new StatusServer();
        statusServer.run(NetworkConfig.REGISTER_PORT);
        System.out.println("启动文件监听服务器中...");
        fileServer = new FileServer();
        fileServer.run(NetworkConfig.FILE_PORT);
        EventCenter.register(this);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("关闭资源中...");
        // 广播一条下线消息,关闭状态服务器
        statusServer.shutdown();
        // 关闭文件监听服务器
        fileServer.shutdown();
        System.out.println("关闭资源成功");
        Platform.exit();


    }

    public static void main(String[] args) {
        System.out.println("launch main win");

        launch();
    }


    @Subscribe
    private void handleEvent(OptionRequestMessage optionRequestEvent) {
        System.out.println("UIManager收到事件：" + optionRequestEvent);
        System.out.println(Thread.currentThread().getName());
    }
}