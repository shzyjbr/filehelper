package com.zzk.filehelper.network;

/**
 * @author: kkrunning
 * @since: 2023/3/13 17:15
 * @description:
 */
public interface NetworkConfig {

    /**
     * 注册端口，客户端上下线
     */
    int REGISTER_PORT = 8000;

    /**
     * 文件监听端口，别的客户端向本客户端发送文件时使用
     */
    int FILE_PORT = 9000;

    /**
     * 是否自动保存接受的文件，false则需要用户点击同意
     */
    boolean AUTO_SAVE = false;
}
