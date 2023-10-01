package com.zzk.filehelper.network;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送文件排队任务，也就是待对端确认的文件
 * @Author zhouzekun
 * @Date 2023/10/1 10:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingFileTask {

    /**
     * 标识任务的唯一id
     */
    private Integer id;


    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 分成了多少数据包
     */
    private long total;
}
