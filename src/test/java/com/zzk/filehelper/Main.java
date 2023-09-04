package com.zzk.filehelper;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author kelton
 * @Date 2023/7/2 10:50
 * @Version 1.0
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        // 创建线程池
        // 创建一个固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(5);

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Future<?> future = executor.submit((Callable) new ComputableTask());
            futures.add(future);

        }
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println(futures.get(i).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new Thread(() -> {
            for (int i = 0; i < futures.size(); i++) {
                log.debug("当前任务[{}]进度：{}",i, ((ComputableTask)futures.get(i)).progress());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "GetResultThread").start();


    }
}
