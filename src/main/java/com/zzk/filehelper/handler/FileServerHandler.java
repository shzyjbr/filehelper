package com.zzk.filehelper.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.FileOutputStream;

/**
 * 计算文件传输速度的样板代码，暂未使用
 */
public class FileServerHandler extends ChannelInboundHandlerAdapter {

    private FileOutputStream fos;
    private int fileLength;
    private int receivedLength;
    private long startTime;
    private long lastTime;
    private DoubleProperty speedProperty = new SimpleDoubleProperty();

    public DoubleProperty speedProperty() {
        return speedProperty;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        // 解析消息头
        if (fileLength == 0) {
            int magicNumber = buf.readInt();
            byte version = buf.readByte();
            byte serializer = buf.readByte();
            byte command = buf.readByte();
            int sequence = buf.readInt();
            fileLength = buf.readInt();

            // 根据消息头的信息，创建文件输出流
            fos = new FileOutputStream("received-file-" + sequence);

            // 记录开始接收文件的时间
            startTime = System.currentTimeMillis();
            lastTime = startTime;
        }

        // 写入文件内容
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        fos.write(bytes);
        receivedLength += bytes.length;

        // 计算并更新接收速度
        long now = System.currentTimeMillis();
        if (now - lastTime >= 1000) { // 每秒更新一次速度
            double speed = (double) receivedLength / (now - startTime) * 1000 / 1024; // KB/s
            speedProperty.set(speed);
            lastTime = now;
        }

        // 如果文件已经接收完毕，关闭文件输出流
        if (receivedLength >= fileLength) {
            fos.close();

            // 重置文件长度和已接收长度，以便于处理下一个文件
            fileLength = 0;
            receivedLength = 0;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
