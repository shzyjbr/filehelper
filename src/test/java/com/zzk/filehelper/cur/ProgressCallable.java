package com.zzk.filehelper.cur;

class ProgressCallable implements java.util.concurrent.Callable<Integer> {
    private MyTask task;

    public ProgressCallable(MyTask task) {
        this.task = task;
    }

    @Override
    public Integer call() throws Exception {
        task.run();

        return task.getProgress();
    }

    public int getProgress() {
        return task.getProgress();
    }

    public int getTotal() {
        return task.getTotal();
    }
}