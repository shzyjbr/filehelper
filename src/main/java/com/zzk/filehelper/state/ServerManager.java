package com.zzk.filehelper.state;

import com.zzk.filehelper.network.FileConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author zhouzekun
 * @Date 2023/9/6 20:00
 */
public class ServerManager {

    @Getter
    @Setter
    private String saveDir;

    private static final ServerManager instance = new ServerManager();

    private ServerManager() {
        saveDir = FileConfig.BASE_DIR;
    }

    public static ServerManager getInstance() {
        return instance;
    }


}
