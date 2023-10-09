package com.zzk.filehelper.handler;

import com.zzk.filehelper.config.ClientConfig;
import com.zzk.filehelper.netty.message.*;
import com.zzk.filehelper.network.NetworkConfig;
import com.zzk.filehelper.serialize.Serializer;
import com.zzk.filehelper.state.DeviceManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import static com.zzk.filehelper.netty.message.MessageConfig.ONLINE_REPLY_MESSAGE;


/**
 * @author zzk
 * @date 2023年9月5日
 * description netty udp服务器入站处理器，在这里实现对状态消息的处理
 */
@Slf4j
public class ServerStatusHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static Serializer serializer = Serializer.getByCode(Serializer.JSON_SERIALIZER);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {

        ByteBuf msg = packet.content();
        byte messageType = msg.readByte();
        switch (messageType) {
            case MessageConfig.ONLINE_REQUEST_MESSAGE ->
                // 别的客户端的上线消息
                    processOnlineMessage(ctx, packet, msg);
            case ONLINE_REPLY_MESSAGE ->
                // 别的客户端回复的上线回复消息
                processReplyMessage(msg);
            case MessageConfig.OFFLINE_MESSAGE ->
                // 别的客户端的下线消息
                processOfflineMessage(msg);
        }
    }

    /**
     * 提取状态消息， 状态消息格式： 4字节表示消息类型， 剩余都是内容
     * @param msg
     * @param clazz
     * @return
     * @param <T>
     */
    private static <T> T decodeMsg(ByteBuf msg, Class<T> clazz) {
        int len = msg.readableBytes();
        byte[] bytes = new byte[len];
        msg.readBytes(bytes);
        return serializer.deserialize(bytes, clazz);
    }

    /**
     * 处理其他客户端回复自己的上线消息
     * @param msg
     */
    private void processReplyMessage(ByteBuf msg) {
        OnlineReplyMessage replyMessage = decodeMsg(msg, OnlineReplyMessage.class);
        log.info("收到上线回复消息：{}", replyMessage);
        String ip = replyMessage.getIp();
        int port = replyMessage.getPort();
        addDevices(ip, port);
    }

    /**
     * 处理其他客户端离线消息
     * @param msg
     */
    private void processOfflineMessage(ByteBuf msg) {
        OfflineMessage offlineMessage = decodeMsg(msg, OfflineMessage.class);
        String ip = offlineMessage.getIp();
        log.info("服务端接收到客户端下线消息: {}",offlineMessage);
        removeDevice(ip);
    }

    /**
     * 从设备列表从移除该客户端记录
     * @param ip
     */
    private void removeDevice(String ip) {
        DeviceManager.getInstance().removeDevice(ip);
    }


    /**
     * 处理其他客户端上线消息
     * @param ctx
     * @param packet
     * @param msg
     */
    private void processOnlineMessage(ChannelHandlerContext ctx, DatagramPacket packet, ByteBuf msg) {
        OnlineRequestMessage onlineRequestMessage = decodeMsg(msg, OnlineRequestMessage.class);
        log.info("服务端接收到客户端上线消息: {}",onlineRequestMessage);
        // 获取ip， 文件传输端口
        String ip = onlineRequestMessage.getIp();
        int port = onlineRequestMessage.getPort();
        // 放进设备列表
        addDevices(ip, port);
        // 回复该客户端自己的ip和文件传输端口
        OnlineReplyMessage onlineReplyMessage = new OnlineReplyMessage();
        onlineReplyMessage.setIp(DeviceManager.getInstance().getLocalIP());
        onlineReplyMessage.setPort(NetworkConfig.FILE_PORT);
        onlineReplyMessage.setSequenceId(onlineRequestMessage.getSequenceId());
        onlineReplyMessage.setMessageType(onlineReplyMessage.getMessageType());
        byte[] replyContent = serializer.serialize(onlineReplyMessage);
        byte[] replyMessage = new byte[]{ONLINE_REPLY_MESSAGE};
        ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer(replyMessage, replyContent), packet.sender()));
    }

    private void addDevices(String ip, int port) {
        DeviceManager.getInstance().addDevice(ip, port);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("消息处理过程中出现错误：");
        cause.printStackTrace();
        ctx.close();
    }

}
