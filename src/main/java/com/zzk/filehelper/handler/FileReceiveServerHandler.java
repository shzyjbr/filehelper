package com.zzk.filehelper.handler;

import com.zzk.filehelper.netty.message.Message;
import com.zzk.filehelper.netty.message.MessageConfig;
import com.zzk.filehelper.state.FileManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.zzk.filehelper.network.NetworkConfig.MAGIC_NUMBER;

public class FileReceiveServerHandler extends ChannelInboundHandlerAdapter {

	static FileOutputStream outputStream;

	static long fileLength;

	private static long readLength;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		int magicNumber = byteBuf.getInt(0);
		if (magicNumber != MAGIC_NUMBER) {
			// 属于文件内容数据包， 直接读取
			readLength += byteBuf.readableBytes();
			writeToFile(byteBuf);
			sendComplete(readLength);
		} else {
			// 接着往下传递
			super.channelRead(ctx, msg);
		}
	}

	private void writeToFile(ByteBuf byteBuf) throws IOException {
		if (outputStream == null) {
			outputStream = new FileOutputStream(FileManager.INSTANCE.getFilename());
		}
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		outputStream.write(bytes);
		byteBuf.release();
	}

	private void sendComplete(long readLength) throws IOException {
		if (readLength >= fileLength) {
			System.out.println("文件[" + FileManager.INSTANCE.getFilename() + "]接收完成...");
			outputStream.close();
			outputStream = null;
			this.readLength = 0;
		}
	}

}
