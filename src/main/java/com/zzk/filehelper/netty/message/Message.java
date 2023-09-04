package com.zzk.filehelper.netty.message;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

}
