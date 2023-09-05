package com.zzk.filehelper.netty.message;

import lombok.Data;

import java.io.Serializable;


@Data
public abstract class Message implements Serializable {

    protected int sequenceId;

    protected int messageType;

    public abstract int getMessageType();

}
