package com.zzk.filehelper.network;


import com.zzk.filehelper.domain.ClientConfig;
import com.zzk.filehelper.domain.Message;
import com.zzk.filehelper.domain.MessageType;
import com.zzk.filehelper.serialize.HessianSerializer;
import com.zzk.filehelper.state.DeviceManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author: kkrunning
 * @since: 2023/3/13 10:17
 * @description: 监听线程，监听其他客户端上线消息
 */
public class RegisterRunnable implements Runnable {
    private int registerPort;


    public RegisterRunnable(int registerPort) {
        this.registerPort = registerPort;
    }

    @Override
    public void run() {
        System.out.println("启动监听线程...");
        DatagramSocket listenSocket = null;
        try {
            listenSocket = new DatagramSocket(this.registerPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        byte[] buffer = new byte[2048];
        HessianSerializer hessianSerializer = new HessianSerializer();
        // noinspection InfiniteLoopStatement
        while (true) {
            try {

                DatagramPacket recePack = new DatagramPacket(buffer, buffer.length);
                listenSocket.receive(recePack);
                // 4.取出数据
                int len = recePack.getLength();

                Message msg = (Message) hessianSerializer.deserialize(recePack.getData(), len, Message.class);
                ClientConfig config = (ClientConfig) msg.getContent();
                String hostAddress = recePack.getAddress().getHostAddress();
                System.out.println("收到来自: " + hostAddress + ",对方端口号为: " + recePack.getPort() + "的消息: " + msg);
                if (hostAddress.equals(DeviceManager.getInstance().getLocalIP()))
                    continue;
                if (msg.getType() == MessageType.Online) {
                    //    收到上线消息，需要进行回复
                    int receiverPort = config.getRegisterPort();
                    response(hostAddress, receiverPort);
                    if (DeviceManager.getInstance().hasDevice(hostAddress)) {
                        //    更新
                        DeviceManager.getInstance().updateDevice(hostAddress, config);
                    } else {
                        //    加入
                        DeviceManager.getInstance().addDevice(hostAddress, config);
                    }
                } else if (msg.getType() == MessageType.Response) {
                    //    收到响应消息，进行状态管理
                    DeviceManager.getInstance().addDevice(hostAddress, config);
                }
            } catch (IOException e) {
                // todo 异常处理
                throw new RuntimeException(e);
            }
        }

    }

    private void response(String hostAddress, int port) {
        ClientConfig config = new ClientConfig();
        config.setRegisterPort(NetworkConfig.REGISTER_PORT);
        config.setFilePort(NetworkConfig.FILE_PORT);
        config.setAutoSave(NetworkConfig.AUTO_SAVE);
        Message message = new Message();
        message.setContent(config);
        message.setType(MessageType.Response);
        MessageSender.sendMsg(hostAddress, port, message);
    }
}
