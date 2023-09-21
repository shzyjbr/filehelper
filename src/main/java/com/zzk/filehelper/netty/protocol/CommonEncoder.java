package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.netty.message.Message;
import com.zzk.filehelper.netty.message.MessageConfig;
import com.zzk.filehelper.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author zzk
 * @date 2021/12/8
 * description  通用编码器 （出站）
 * 自定义数据包还解决了TCP粘包拆包问题
 * 这里可以使用一个Message将RpcRequest和RpcResponse统一起来
 * 包格式:
 * magicCode :  4 bytes
 * packageType: 4 bytes
 * serializerCode:  4 bytes
 * length:      4 bytes
 * object:      bytes of the serialized object
 */

public class CommonEncoder extends MessageToByteEncoder<Message> {
    private final Serializer serializer;

    public CommonEncoder(Serializer serializer) {
        this.serializer = serializer;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        if (msg.getMessageType() == MessageConfig.FILE_MESSAGE) {
            // 处理文件内容发送的格式
        } else {
            // 普通消息格式
            // 1. 4 字节的魔数
            out.writeBytes(new byte[]{1, 2, 3, 4});
            // 4. 1 字节的指令类型
            out.writeByte(msg.getMessageType());
            // 2. 1 字节的版本
            out.writeByte(1);
            // 3. 1 字节的序列化方式 jdk 0 , json 1
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
}
