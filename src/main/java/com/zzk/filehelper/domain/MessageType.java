package com.zzk.filehelper.domain;

import java.io.Serializable;

/**
 * 消息类型
 * 0: 上线消息
 * 1: 应答消息
 *      * 2: 文件发送回复消息：同意或者拒绝
 * @author: kkrunning
 * @since: 2023/3/13 14:39
 * @description:
 */
public enum MessageType implements Serializable {
    Online, Response, FileTransfer, SendFileRequest,SendFileResponse

}
