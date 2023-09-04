package com.zzk.filehelper.netty.protocol;


import com.zzk.filehelper.netty.message.Message;
import com.zzk.filehelper.netty.message.MessageConfig;
import com.zzk.filehelper.serialize.JsonSerializer;
import com.zzk.filehelper.serialize.Serializer;
import com.zzk.filehelper.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接到的 ByteBuf 消息是完整的
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 1. 4 字节的魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1 字节的版本
        out.writeByte(1);
        // 3. 1 字节的序列化方式 jdk 0 , json 1
        out.writeByte(com.zzk.filehelper.serialize.Serializer.JSON_SERIALIZER);
        // 4. 1 字节的指令类型
        out.writeByte(msg.getMessageType());
        // 5. 4 个字节序列号
        out.writeInt(msg.getSequenceId());
        // 6. 无意义，对齐填充
        out.writeByte(0xff);
        //  获取内容的字节数组
        byte[] bytes = new JsonSerializer().serialize(msg);
        // 7. 长度
        out.writeInt(bytes.length);
        // 8. 写入内容
        out.writeBytes(bytes);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1. 4字节魔术
        int magicNum = in.readInt();
        // 2. 1 字节的版本
        byte version = in.readByte();
        // 3. 1 字节的序列化方式 jdk 0 , json 1
        byte serializerAlgorithm = in.readByte(); // 0 或 1
        // 4. 1 字节的指令类型
        byte messageType = in.readByte(); // 0,1,2...
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

        // 找到反序列化算法
        Serializer serializer = SerializerFactory.getSerializer(serializerAlgorithm);
        // 确定具体消息类型
        Class<? extends Message> messageClass = MessageConfig.getMessageClass(messageType);
        Message message = serializer.deserialize(bytes, messageClass);
       log.info("magicNum:{}, version:{}, serializerAlgorithm:{}, messageType:{}, sequenceId:{}, length:{}",
               magicNum, version, serializerAlgorithm, messageType, sequenceId, length);
       log.info("{}", message);
        out.add(message);
    }

}
