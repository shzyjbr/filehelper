package com.zzk.filehelper.handler;



import com.zzk.filehelper.domain.FileTask;
import com.zzk.filehelper.domain.Message;
import com.zzk.filehelper.network.FileTaskManager;
import com.zzk.filehelper.network.FileTransferUtil;
import com.zzk.filehelper.network.MessageSender;

import java.net.Socket;


/**
 * @author zzk
 * description Socket服务器处理线程，在这里对请求数据进行处理
 */

public class RequestHandlerThread implements Runnable {

    private final Socket socket;

    public RequestHandlerThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {

        try {
            // 读取文件传送预检请求消息
            MessageSender.readOptionMsg(socket);
            Message msg = new Message();
            // todo 使用GUI  如果autoSave为true那么直接保存，否则询问用户
            //调用GUI可以使用事件总线
            //GUI程序启动的时候，可以将mainScene或者app保存起来
            //调用GUI同意或者拒绝之后， 应该返回结果，而且能对应到是哪个任务  应该有一个fileTask映射

            msg.setContent("accept");
            MessageSender.replyOptionMsg(socket, msg);
            //客户端传送一个文件使用一个端口，如果同时传送多个文件，那么使用多个端口
            // 为每个socket创建一个FileTask
            FileTask fileTask = new FileTask();
            fileTask.setSocket(socket);
            fileTask.register(FileTaskManager.getInstance());
            fileTask.setTransferStatus(FileTask.TransferStatus.peeding);
            fileTask.setFileType(FileTask.FileType.receive);
            fileTask.setCreateTime(System.currentTimeMillis());

            // 添加进FileTaskManager进行文件传输状态管理
            FileTaskManager.getInstance().offer(fileTask);
            // 任务队列管理， 使用观察者模式
            if (fileTask.getFileType() == FileTask.FileType.receive) {
                FileTransferUtil.receiveFile(fileTask);
            } else if (fileTask.getFileType() == FileTask.FileType.send) {
                FileTransferUtil.sendFile(fileTask);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
