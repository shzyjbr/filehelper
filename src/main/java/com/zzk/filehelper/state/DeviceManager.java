package com.zzk.filehelper.state;

import com.zzk.filehelper.config.ClientConfig;
import com.zzk.filehelper.network.IpUtil;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * 关于客户端上下线状态的管理，首先是收到其他客户端上线消息之后就会被添加进device set
 * 对于正常退出的客户端，其在关闭软件时会广播一条下线消息，因此其他客户端能从device set中删除这条设备记录
 * 那么客户端非正常下线，即没有发出下线消息，我们应该如何更新这个device set呢
 * 第一种方式：采用类似心跳包的方式，定期发送广播包，维持上线消息，比如一分钟发送一次，再设置一个扫描线程，把超时的下线客户端清理掉
 * 那么每次ClientConfig类中还需要添加一个更新时间，这种方式消耗的资源比较多
 * 第二种方式：设置一个后台线程，定时扫描device set中的设备是否连通，清除不连通的设备记录；并且在想某个客户端传送文件时，检测是否连通，
 * 不连通则利用事件总线发出一条消息，由对应的时间处理器清理该客户端记录
 *
 * @author: kkrunning
 * @since: 2023/3/13 14:43
 * @description:
 */
public class DeviceManager {

    private HashMap<String, ClientConfig> devices;

    private String localIP;

    private static DeviceManager instance = new DeviceManager();

    private DeviceManager() {
        this.devices = new HashMap<>();
        try {
            this.localIP = IpUtil.getLocalIp4Address();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public static DeviceManager getInstance() {
        return instance;
    }

    /**
     * deviceName目前直接用ip
     * @param ip
     * @param port
     */
    public void addDevice(String ip, int port) {
        if (!ip.equals(getLocalIP())) {
            // todo 目前还没实现自动保存相关功能
            this.devices.put(ip, new ClientConfig(ip, port, false));
        }
        // 打印当前的客户端记录信息
        this.devices.forEach((String device, ClientConfig cfg) -> System.out.println(device + "::::" + cfg));
    }

    public void removeDevice(String ip) {
        devices.remove(ip);
    }

    public void clearDevice() {
        this.devices.clear();
    }

    public boolean updateDevice(String deviceName, ClientConfig config) {
        if (!this.devices.containsKey(deviceName)) {
            return false;
        }
        this.devices.put(deviceName, config);
        return true;
    }

    public HashMap<String, ClientConfig> getDevices() {
        return this.devices;
    }

    public String getLocalIP() {
        return localIP;
    }

    public boolean hasDevice(String device) {
        return this.devices.containsKey(device);
    }
}
