package com.zzk.filehelper.event.manager;

import com.google.common.eventbus.Subscribe;
import com.zzk.filehelper.netty.message.OptionRequestMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 连接文件传输事件与UI的管理器
 * @Author zhouzekun
 * @Date 2023/10/1 17:27
 */
@Data
public class UIManager {

//    @Subscribe
//    private void handleEvent(OptionRequestMessage optionRequestEvent) {
//        System.out.println("UIManager收到事件：" + optionRequestEvent);
//        System.out.println(Thread.currentThread().getName());
//    }
}
