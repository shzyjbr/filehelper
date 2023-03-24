package com.zzk.filehelper.network;

import com.zzk.filehelper.factory.ThreadPoolFactory;
import com.zzk.filehelper.handler.RequestHandlerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @author: kkrunning
 * @since: 2023/3/13 20:21
 * @description:
 */
public class FileReceiver implements  Runnable{

    private int filePort;
    private final ExecutorService threadPool;

    public FileReceiver(int filePort) {
        this.filePort = filePort;
        this.threadPool = ThreadPoolFactory.createDefaultThreadPool("file-receiver-server");
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.filePort)){
            System.out.println("文件接收服务器已启动...");
            System.out.println("服务器监听端口:" + this.filePort);
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                System.out.println("客户端连接，ip:"+socket.getInetAddress() + ", port:"+ socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
