package com.zzk.filehelper.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: kkrunning
 * @since: 2023/3/13 14:34
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientConfig implements Serializable {
    /**
     * 该客户端的文件接收端口
     */
    private int filePort;

    /**
     * 该客户端的状态端口, 上线下线
     */
    private int statusPort;
    /**
     * 自动保存表示向该客户端传输文件无需发送预检请求
     */
    private boolean autoSave;

}
