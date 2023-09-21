package com.zzk.filehelper;

import com.zzk.filehelper.network.IpUtil;

import java.net.SocketException;

/**
 * @Author kelton
 * @Date 2023/9/9 18:12
 * @Version 1.0
 */
public class IPTest {
    public static void main(String[] args) throws SocketException {
        String localIp4Address = IpUtil.getLocalIp4Address();
        System.out.println(localIp4Address);

    }
}
