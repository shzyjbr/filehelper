package com.zzk.filehelper.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author zhouzekun
 * @Date 2023/10/1 17:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionReplyEvent {
    /**
     * 接收哪些文件
     */
    private Map<String, Boolean> reply;
}
