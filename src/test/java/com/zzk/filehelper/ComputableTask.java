package com.zzk.filehelper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @Author kelton
 * @Date 2023/7/1 23:38
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@Slf4j
public class ComputableTask implements Callable, Result{

    private Integer progress;

    public ComputableTask() {
        this.progress = 0;
    }

    @Override
    public int progress() {
        return progress;
    }

    @Override
    public Object call() throws Exception {
        for(int i = 0;i < 10; i++) {
            try {
                // 模拟文件传输速度
                TimeUnit.SECONDS.sleep(1);
                log.info("process:{}%", progress);
                progress+=10;
            } catch (InterruptedException e) {
                throw new RuntimeException("",e);
            }
        }
        return progress;
    }
}
