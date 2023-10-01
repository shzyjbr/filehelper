package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.netty.message.FileContentMessage;
import com.zzk.filehelper.netty.message.Message;
import com.zzk.filehelper.netty.message.MessageConfig;
import com.zzk.filehelper.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static com.zzk.filehelper.network.NetworkConfig.MAGIC_NUMBER;

/**
 *
 * @Author  zhouzekun
 * @Date 2023/10/1
 */


public class CommonEncoder extends MessageToByteEncoder<Message> {
    private final Serializer serializer;

    public CommonEncoder(Serializer serializer) {
        this.serializer = serializer;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        if (msg.getMessageType() == MessageConfig.FILE_CONTENT_MESSAGE) {
            // 处理文件内容发送的格式
            encodeFileContentMessage(ctx, msg, out);
        } else {
            // 普通消息格式
            // 1. 4 字节的魔数
            out.writeInt(MAGIC_NUMBER);
            // 2. 1 字节的指令类型
            out.writeByte(msg.getMessageType());
            // 3. 1 字节的版本
            out.writeByte(1);
            // 4. 1 字节的序列化方式 jdk 0 , json 1
            out.writeByte(com.zzk.filehelper.serialize.Serializer.JSON_SERIALIZER);
            // 5. 4 个字节序列号
            out.writeInt(1);
            // 6. 无意义，对齐填充
            out.writeByte(0xff);
            //  获取内容的字节数组
            byte[] bytes = serializer.serialize(msg);
            // 7. 长度
            out.writeInt(bytes.length);
            // 8. 写入内容
            out.writeBytes(bytes);
        }
    }

    private void encodeFileContentMessage(ChannelHandlerContext ctx, Message msg, ByteBuf out) {
        // 强制转换
        FileContentMessage fileContentMessage = (FileContentMessage) msg;
        // 1. 4 字节的魔数
        out.writeInt(MAGIC_NUMBER);
        // 2. 1 字节的指令类型
        out.writeByte(fileContentMessage.getMessageType());
        // 任务id
        out.writeInt(fileContentMessage.getId());
        // 3. 写入8字节的包序号
        out.writeLong(fileContentMessage.getPacketNumber());
        // 4. 写入8字节的包总数
        out.writeLong(fileContentMessage.getTotalPackets());
        // 5. 写入4字节的包内容长度
        out.writeInt(fileContentMessage.getContentLength());
        // 6. 写入包内容
       out.writeBytes(fileContentMessage.getData());
    }
}
