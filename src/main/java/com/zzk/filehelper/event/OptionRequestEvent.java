package com.zzk.filehelper.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author zhouzekun
 * @Date 2023/10/1 17:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionRequestEvent {

    private List<String> files;
}
