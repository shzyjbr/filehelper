package com.zzk.filehelper.state;

import com.zzk.filehelper.netty.message.FileMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhouzekun
 * @Date 2023/9/11 17:27
 */

@Data
public class FileManager {

    private String filename;

    private long length;

    private List<FileMessage> pendingFiles;

    public static FileManager INSTANCE;

    private FileManager() {
        pendingFiles = new ArrayList<>();
    }


}
