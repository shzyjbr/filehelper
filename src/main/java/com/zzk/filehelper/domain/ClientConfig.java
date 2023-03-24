package com.zzk.filehelper.domain;

import java.io.Serializable;

/**
 * @author: kkrunning
 * @since: 2023/3/13 14:34
 * @description:
 */
public class ClientConfig implements Serializable {

    private int filePort;

    private int registerPort;
    private boolean autoSave;

    public ClientConfig(int filePort, int registerPort, boolean autoSave) {
        this.filePort = filePort;
        this.autoSave = autoSave;
        this.registerPort = registerPort;
    }

    public ClientConfig() {
    }

    public int getFilePort() {
        return filePort;
    }

    public void setFilePort(int filePort) {
        this.filePort = filePort;
    }

    public boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }


    public int getRegisterPort() {
        return registerPort;
    }

    public void setRegisterPort(int registerPort) {
        this.registerPort = registerPort;
    }

    @Override
    public String toString() {
        return "ClientConfig{" +
                "filePort=" + filePort +
                ", registerPort=" + registerPort +
                ", autoSave=" + autoSave +
                '}';
    }
}
