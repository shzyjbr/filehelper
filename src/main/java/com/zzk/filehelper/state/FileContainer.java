package com.zzk.filehelper.state;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.io.File;
import java.util.*;

/**
 * 保存打开文件框后选中的文件
 * @Author zhouzekun
 * @Date 2023/10/2 9:39
 */
public class FileContainer {

    /**
     * 保存选中的文件
     */
    private static final ObservableSet<String> files = FXCollections.observableSet();

    public int fileCount = 0;

    public static final FileContainer instance = new FileContainer();

    private FileContainer() {

    }

    public ObservableSet<String> getFiles () {
        return files;
    }

    /**
     * 加入文件时使用全文件名
     * @param file
     */
    public void addFile(String file) {
        files.add(file);
    }

    public void remove(String file) {
        files.remove(file);
    }

    public void print() {
        files.forEach(System.out::println);
    }

    public int count() {
        return files.size();
    }

    /**
     * 计算总的文件大小 单位：MB
     * @return
     */
    public long totalSize() {
        if (files.isEmpty())
            return 0;

        Optional<Long> sum = files.stream().filter(filename -> {
            File file = new File(filename);
            return file.exists();
        }).map(filename -> {
            File file = new File(filename);
            return file.length();
        }).reduce(Long::sum);
        return sum.orElse(0L)/(1024*1024);

    }
}
