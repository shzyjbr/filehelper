package com.zzk.filehelper.network;

import com.zzk.filehelper.domain.Message;
import com.zzk.filehelper.domain.MessageType;
import com.zzk.filehelper.serialize.HessianSerializer;


import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author: kkrunning
 * @since: 2023/3/13 10:52
 * @description: 消息发送工具类
 */
public class MessageSender {

    /**
     * 向接收端发送消息
     *
     * @param receiverHost 消息接收端IP
     * @param receiverPort 消息接收端port
     * @param msg          待发送消息
     */
    public static void sendMsg(String receiverHost, int receiverPort, Message msg) {

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            HessianSerializer hessianSerializer = new HessianSerializer();
            byte[] bytes = hessianSerializer.serialize(msg);
            InetAddress inetAddress = InetAddress.getByName(receiverHost);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, inetAddress, receiverPort);
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean sendOptionMsg(Socket socket, Message optionMessage) throws Exception {
        HessianSerializer hessianSerializer = new HessianSerializer();
        byte[] messageBytes = hessianSerializer.serialize(optionMessage);
        OutputStream out = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        InputStream in = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(in);
        try {

            // 写入魔术字
            dataOutputStream.writeInt(FileConfig.MAGIC_NUMBER);
            // 写入消息内容长度
            int messageContentLen = messageBytes.length;
            dataOutputStream.writeInt(messageContentLen);
            // 写入消息内容
            dataOutputStream.write(messageBytes);
            dataOutputStream.flush();

            // 获取魔术字
            int receiveMagic = dataInputStream.readInt();
            // 校验魔术字
            if (receiveMagic != FileConfig.MAGIC_NUMBER)
                throw new Exception("读取预检回复消息：unmatch magic number");

            // 获取回复消息长度
            int receiveMsgLen = dataInputStream.readInt();

            // 获取回复消息
            byte[] resByte = dataInputStream.readNBytes(receiveMsgLen);

            Message msg = (Message) hessianSerializer.deserialize(resByte, Message.class);

            return "accept".equals(msg.getContent());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Message readOptionMsg(Socket socket) throws Exception {
        HessianSerializer hessianSerializer = new HessianSerializer();
        InputStream in = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(in);

        // 获取魔术字
        int receiveMagic = dataInputStream.readInt();
        // 校验魔术字
        if (receiveMagic != FileConfig.MAGIC_NUMBER)
            throw new Exception("预检请求消息，unmatch magic number");

        // 获取预检请求消息内容长度
        int receiveMsgLen = dataInputStream.readInt();

        // 获取预检请求消息内容
        byte[] resByte = dataInputStream.readNBytes(receiveMsgLen);

        Message msg = hessianSerializer.deserialize(resByte, Message.class);
        if (msg.getType() != MessageType.SendFileRequest)
            throw new Exception("读取预检请求消息: unmatch MessageType.SendFileRequest");

        System.out.println("log: " + msg);
        return msg;

    }
    public static void replyOptionMsg(Socket socket, Message msg) throws IOException {
        HessianSerializer hessianSerializer = new HessianSerializer();
        OutputStream out = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(out);

        msg.setType(MessageType.SendFileResponse);
        byte[] msgByte = hessianSerializer.serialize(msg);
        // 写入魔术字
        dataOutputStream.writeInt(FileConfig.MAGIC_NUMBER);
        // 写入预检恢复消息内容长度
        int messageContentLen = msgByte.length;
        dataOutputStream.writeInt(messageContentLen);
        // 写入消息内容
        dataOutputStream.write(msgByte);
        dataOutputStream.flush();
    }

    public static void handleOptionMsg(Socket socket) throws Exception {
        HessianSerializer hessianSerializer = new HessianSerializer();
        InputStream in = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(in);
        OutputStream out = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        try {
            // 获取魔术字
            int receiveMagic = dataInputStream.readInt();
            // 校验魔术字
            if (receiveMagic != FileConfig.MAGIC_NUMBER)
                throw new Exception("预检请求消息，unmatch magic number");

            // 获取预检请求消息内容长度
            int receiveMsgLen = dataInputStream.readInt();

            // 获取预检请求消息内容
            byte[] resByte = dataInputStream.readNBytes(receiveMsgLen);

            Message msg = (Message) hessianSerializer.deserialize(resByte, Message.class);
            if (msg.getType() != MessageType.SendFileRequest)
                throw new Exception("读取预检请求消息: unmatch MessageType.SendFileRequest");

            System.out.println("log: " + msg);
            // todo 显示在界面， 是否接受， 暂时假设接受
            // 复用msg
            msg.setContent("accept");
            msg.setType(MessageType.SendFileResponse);
            byte[] msgByte = hessianSerializer.serialize(msg);
            // 写入魔术字
            dataOutputStream.writeInt(FileConfig.MAGIC_NUMBER);
            // 写入预检恢复消息内容长度
            int messageContentLen = msgByte.length;
            dataOutputStream.writeInt(messageContentLen);
            // 写入消息内容
            dataOutputStream.write(msgByte);
            dataOutputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void broadcastMsg(int port, Message msg) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            HessianSerializer hessianSerializer = new HessianSerializer();
            byte[] bytes = hessianSerializer.serialize(msg);
            // 向广播地址发送
            InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, inetAddress, port);
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
