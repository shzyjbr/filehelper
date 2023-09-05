package com.zzk.filehelper.netty.protocol;

import com.zzk.filehelper.netty.message.Message;
import com.zzk.filehelper.netty.message.MessageConfig;
import com.zzk.filehelper.serialize.JsonSerializer;
import com.zzk.filehelper.serialize.Serializer;
import com.zzk.filehelper.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @author zzk
 * @date 2023年9月5日
 * description  通用解码器（入站）
 *              自定义数据包还解决了TCP粘包拆包问题
 *              这里可以使用一个Message将RpcRequest和RpcResponse统一起来
 *                包格式:
 *                    magicCode :  4 bytes
 *                    packageType: 4 bytes
 *                    serializerCode:  4 bytes
 *                    length:      4 bytes
 *                    object:      bytes of the serialized object
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outList) throws Exception {
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
        outList.add(message);
        
    }
}
