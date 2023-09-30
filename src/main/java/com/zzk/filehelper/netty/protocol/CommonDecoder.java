package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.netty.message.FileContentMessage;
import com.zzk.filehelper.netty.message.FileMetaMessage;
import com.zzk.filehelper.netty.message.Message;
import com.zzk.filehelper.netty.message.MessageConfig;
import com.zzk.filehelper.network.FileConfig;
import com.zzk.filehelper.network.NetworkConfig;
import com.zzk.filehelper.serialize.Serializer;
import com.zzk.filehelper.state.ServerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * 通用解码器（入站）
 * @Author  zhouzekun
 * @Date 2023/9/10
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder<Void> {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outList) throws Exception {
        // 1. 4字节魔术
        int magicNum = in.readInt();
        // 校验魔术字
        if(magicNum != NetworkConfig.MAGIC_NUMBER) {
            // 直接关闭该通道
            ChannelFuture close = ctx.channel().close();
            close.addListener((ChannelFutureListener) channelFuture ->
                    System.out.println("关闭不安全链接:"+ channelFuture.channel().remoteAddress()));
        }
        // 4. 1 字节的消息类型
        byte messageType = in.readByte();

        if (messageType == MessageConfig.FILE_MESSAGE) {
            // 如果是文件内容消息，根据文件内容格式来解析消息
            parseFileMessage(in,outList);
        } else {
            // 如果是普通消息，根据普通消息格式来解析消息
            Message message = parseNormalMessage(in, magicNum, messageType);
            outList.add(message);
        }
    }


    private static Message parseNormalMessage(ByteBuf in, int magicNum, byte messageType) {
        // 2. 1 字节的版本
        byte version = in.readByte();
        // 3. 1 字节的序列化方式 jdk 0 , json 1
        byte serializerCode = in.readByte(); // 0 或 1
        // 5. 4 个字节序列号
        int sequenceId = in.readInt();
        // 6. 无意义，对齐填充
        in.readByte();
        // 7. 长度
        int length = in.readInt();
        // 获取内容的字节数组
        byte[] bytes = new byte[length];
        // 8. 读取内容
        in.readBytes(bytes, 0, length);
        log.info("magicNum:{}, version:{}, serializerAlgorithm:{}, messageType:{}, sequenceId:{}, length:{}",
                magicNum, version, serializerCode, messageType, sequenceId, length);
        // 找到反序列化算法
        Serializer serializer = Serializer.getByCode(Serializer.JSON_SERIALIZER);
        // 确定具体消息类型
        Class<? extends Message> messageClass = MessageConfig.getMessageClass(messageType);
        Message message = serializer.deserialize(bytes, messageClass);

        log.info("{}", message);
        return message;
    }

    private static void parseFileMessage(ByteBuf in, List<Object> outList) throws IOException {
        FileContentMessage fileContentMessage = new FileContentMessage();
        long packetNumber = in.readLong();
        fileContentMessage.setPacketNumber(packetNumber);

        long totalPacket = in.readLong();
        fileContentMessage.setTotalPackets(totalPacket);

        int dataLength = in.readInt();
        fileContentMessage.setContentLength(dataLength);

        byte[] dataBytes = new byte[dataLength];
        in.readBytes(dataBytes);
        fileContentMessage.setData(dataBytes);
        outList.add(fileContentMessage);

    }

}
