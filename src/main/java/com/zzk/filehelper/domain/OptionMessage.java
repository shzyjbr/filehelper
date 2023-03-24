package com.zzk.filehelper.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author: kkrunning
 * @since: 2023/3/24 12:11
 * @description:
 */
public class OptionMessage implements Serializable {

    private List<String> files;

    public OptionMessage(List<String> files) {
        this.files = files;
    }

    public OptionMessage() {
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "OptionMessage{" +
                "files=" + files +
                '}';
    }
}
