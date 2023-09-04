package com.zzk.filehelper.cur;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author kelton
 * @Date 2023/7/2 11:08
 * @Version 1.0
 */
@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Future<Integer>> futures = new ArrayList<>();
        List<MyTask> taskList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            MyTask myTask = new MyTask(10);
            ProgressCallable task = new ProgressCallable(myTask);
            Future<Integer> future = executorService.submit(task);
            futures.add(future);
            taskList.add(myTask);

        }

        CountDownLatch countDownLatch = new CountDownLatch(taskList.size());
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            for (MyTask task : taskList) {
                // 每隔1秒钟获取一次任务进度
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.debug("任务【{}】进度：{}/{}", task.getId(), task.getProgress(), task.getTotal());
            }
            for(Future<Integer> future: futures) {
                if (future.isDone()) {
                    countDownLatch.countDown();
                }
            }


        }, 0, 1, TimeUnit.SECONDS);

        countDownLatch.await();
        executorService.shutdown();
        scheduledExecutorService.shutdownNow();
        for (Future<Integer> future : futures) {
            log.debug("任务【{}】结果：{}", future,future.get()); //阻塞获取
        }


    }
}
