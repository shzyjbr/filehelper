package com.zzk.filehelper.state;

import com.zzk.filehelper.domain.ClientConfig;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
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
            this.localIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static DeviceManager getInstance() {
        return instance;
    }

    public void addDevice(String deviceName, ClientConfig config) {
        if (!deviceName.equals(getLocalIP())) {
            this.devices.put(deviceName, config);
        }
        this.devices.forEach((String device, ClientConfig cfg) -> System.out.println(device + "::::" + cfg));
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
