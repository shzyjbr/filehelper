package com.zzk.filehelper.network;

import com.zzk.filehelper.domain.FileTask;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author: kkrunning
 * @since: 2023/3/13 18:59
 * @description: 文件传输工具类
 *                  传输包结构： 魔术字（4字节） 传输类型（4字节） 文件名长度（4字节） 文件名（文件名长度字节） 文件长度（8字节） 文件（文件长度字节）
 */
public class FileTransferUtil {

    /**
     *
     * @param fileTask 文件传输任务
     */
    public static void sendFile(FileTask fileTask) {
        //检查文件是否存在
        File file = new File(fileTask.getFullPath());
        if (!file.exists()) {
        //    文件不存在
            System.err.println("文件不存在");
            fileTask.setTransferStatus(FileTask.TransferStatus.unexpected_close);
            return;
        }
        Socket socket = fileTask.getSocket();
        try (OutputStream out = socket.getOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(out)){

            // 写入魔术字
            dataOutputStream.writeInt(FileConfig.MAGIC_NUMBER);
            // 写入传输类型
            dataOutputStream.writeInt(FileConfig.FILE_TYPE);

            // 获取文件名
            String filename = file.getName();
            byte[] filenameBytes = filename.getBytes(StandardCharsets.UTF_8);
            // 写入文件名长度
            dataOutputStream.writeInt(filenameBytes.length);
            // 写入文件名
            dataOutputStream.write(filenameBytes);
            long fileLength = file.length();
            // 写入文件长度
            dataOutputStream.writeLong(fileLength);
            byte[] buf = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(file);
            int len = 0;
            int readSize = 0;
            // 创建文件任务FileTask
            fileTask.setFilename(filename);
            fileTask.setFileType(FileTask.FileType.send);
            fileTask.setTotalSize(fileLength);
            // todo 目前暂时直接设置开始时间
            fileTask.setBeginTransferTime(System.currentTimeMillis());
            fileTask.setTransferStatus(FileTask.TransferStatus.transferring);
            while ((len = fileInputStream.read(buf)) != -1) {
                readSize += len;// 计算当前传输的总字节数
                dataOutputStream.write(buf, 0, len);
                dataOutputStream.flush();
                fileTask.setCurrentSize(readSize);
            }
            //    发送完成
            fileTask.setTransferStatus(FileTask.TransferStatus.history);
            fileTask.setFinish(true);
            System.out.println("发送完成");
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void receiveFile(FileTask fileTask) {
        Socket socket = fileTask.getSocket();
        try (InputStream in = socket.getInputStream();
             DataInputStream dataInputStream = new DataInputStream(in)){
            // 读取魔术字
            int magic = dataInputStream.readInt();
            if (magic != FileConfig.MAGIC_NUMBER) {
                System.err.println("不识别的协议包");
            }
            // 读取传输类型
            int type = dataInputStream.readInt();
            // 读取文件名长度
            int filenameLength = dataInputStream.readInt();
            byte[] filenameBuf = new byte[filenameLength];
            // 读取文件名
            dataInputStream.read(filenameBuf);
            String filename = new String(filenameBuf);

            long filesize = dataInputStream.readLong();
            String fullFileName = FileConfig.BASE_DIR + filename;
            System.out.println(fullFileName);
            File file = new File(fullFileName);// 通过选择的文件路径+传输的文件名，确定文件保存位置
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                long read_size = 0;
                int len = 0;// 每次接收的大小
                byte[] bytes = new byte[1024];

                fileTask.setFilename(filename);
                fileTask.setTotalSize(filesize);
                // todo 目前暂时直接设置开始时间
                fileTask.setBeginTransferTime(System.currentTimeMillis());
                fileTask.setTransferStatus(FileTask.TransferStatus.transferring);

                while ((len = in.read(bytes)) != -1) {

                    read_size += len;
                    fileOutputStream.write(bytes, 0, len);
                    fileOutputStream.flush();
                    fileTask.setCurrentSize(read_size);
                }
                //    传输完成
                fileTask.setTransferStatus(FileTask.TransferStatus.history);
            } catch (IOException e) {
                System.err.println("接收文件异常:" + e.getMessage());
                e.printStackTrace();
                fileTask.setTransferStatus(FileTask.TransferStatus.unexpected_close);

            }

        } catch (IOException e) {
            System.err.println("接收过程异常:" + e.getMessage());
            e.printStackTrace();
            fileTask.setTransferStatus(FileTask.TransferStatus.unexpected_close);

        }
    }
}
