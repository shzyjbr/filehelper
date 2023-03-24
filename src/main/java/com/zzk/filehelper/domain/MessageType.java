package com.zzk.filehelper.domain;

import java.io.Serializable;

/**
 * @author: kkrunning
 * @since: 2023/3/13 14:39
 * @description:
 */
public enum MessageType implements Serializable {
    Online, Response, FileTransfer, SendFileRequest,SendFileResponse


}
