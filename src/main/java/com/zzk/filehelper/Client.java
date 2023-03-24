package com.zzk.filehelper;

import com.zzk.filehelper.domain.ClientConfig;
import com.zzk.filehelper.domain.Message;
import com.zzk.filehelper.domain.MessageType;
import com.zzk.filehelper.network.FileReceiver;
import com.zzk.filehelper.network.FileTaskManager;
import com.zzk.filehelper.network.NetworkConfig;
import com.zzk.filehelper.network.RegisterRunnable;
import com.zzk.filehelper.serialize.HessianSerializer;
import com.zzk.filehelper.state.DeviceManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: kkrunning
 * @since: 2023/3/13 10:34
 * @description:
 */
public class Client {


    public static void main(String[] args) throws IOException, InterruptedException {
        //模拟
        //创建registerThread
        RegisterRunnable registerRunnable = new RegisterRunnable(NetworkConfig.REGISTER_PORT);
        Thread registerThread = new Thread(registerRunnable);
        registerThread.start();

        //创建文件接收线程
        FileReceiver fileReceiver = new FileReceiver(NetworkConfig.FILE_PORT);
        Thread fileThread = new Thread(fileReceiver);
        fileThread.start();


        // 创建任务队列
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(2);
        // 定时打印Devices
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            DeviceManager.getInstance().getDevices().forEach((String device, ClientConfig cfg)-> {
                System.out.println("[定时任务一]:" + device + ":" + cfg);
            });
        }, 1, 3, TimeUnit.SECONDS); // 1s 后开始执行，每 3s 执行一次

        // 定时打印FileTask
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            FileTaskManager.getInstance().getFileTasks().forEach(fileTask-> {
                System.out.println("[定时任务二]:" + fileTask);
            });
        }, 1, 3, TimeUnit.SECONDS); // 1s 后开始执行，每 3s 执行一次

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            ClientConfig config = new ClientConfig();
            config.setRegisterPort(NetworkConfig.REGISTER_PORT);
            config.setAutoSave(false);
            config.setFilePort(NetworkConfig.FILE_PORT);
            Message message = new Message();
            message.setContent(config);
            message.setType(MessageType.Online);

            HessianSerializer hessianSerializer = new HessianSerializer();
            byte[] bytes = hessianSerializer.serialize(message);
            InetAddress inetAddress  = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, inetAddress, NetworkConfig.REGISTER_PORT);
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            //测试
            throw new RuntimeException(e);
        }

    }





}
