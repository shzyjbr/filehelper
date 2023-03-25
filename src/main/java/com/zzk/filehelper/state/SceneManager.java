package com.zzk.filehelper.state;

import javafx.scene.Parent;

/**
 * @author: kkrunning
 * @since: 2023/3/25 15:50
 * @description:
 */
public class SceneManager {

    public static final SceneManager instance = new SceneManager();

    private Parent mainPane;

    private Parent historyPane;


    private SceneManager() {
    }

    public Parent getMainPane() {
        return mainPane;
    }

    public void setMainPane(Parent mainPane) {
        this.mainPane = mainPane;
    }

    public Parent getHistoryPane() {
        return historyPane;
    }

    public void setHistoryPane(Parent historyPane) {
        this.historyPane = historyPane;
    }
}
