package com.zzk.filehelper.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zhouzekun
 * @Date 2023/9/6 19:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileSendEvent {

    private String filename;
}
