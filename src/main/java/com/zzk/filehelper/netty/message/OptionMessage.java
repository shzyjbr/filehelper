package com.zzk.filehelper.netty.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 预请求消息，比如A给B传输文件，A会发送一个预请求消息，B确认之后才可以传输文件
 * @author: kkrunning
 * @since: 2023/3/24 12:11
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class OptionMessage implements Serializable {

    /**
     * 要发送的文件名
     */
    private List<String> files;
}
