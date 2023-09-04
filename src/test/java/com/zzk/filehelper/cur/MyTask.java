package com.zzk.filehelper.cur;

import lombok.Data;

@Data
class MyTask implements Runnable {

    private static int ID_GENERATOR = 1;

    private int id;
    private int total;
    private int progress;

    public MyTask(int total) {
        this.total = total;
        this.progress = 0;
        this.id = ID_GENERATOR++;
    }

    @Override
    public void run() {
        // 任务执行的逻辑
        for (int i = 1; i <= total; i++) {
            // 模拟任务执行
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 更新进度
            progress = i;
        }
    }

}