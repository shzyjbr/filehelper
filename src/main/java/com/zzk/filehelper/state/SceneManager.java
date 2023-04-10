package com.zzk.filehelper.state;

import javafx.animation.Animation;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * @author: kkrunning
 * @since: 2023/3/25 15:50
 * @description:
 */
public class SceneManager {

    public static final SceneManager instance = new SceneManager();

    private Parent mainPane;

    private Parent historyPane;

    private Parent histortMainPane;

    public Stage mainStage;

    private HashMap<String, Animation> animations;


    private SceneManager() {
        animations = new HashMap<>();
    }

    public void addAnimation(String name, Animation animation) {
        animations.put(name, animation);
    }

    public Animation getAnimation(String name) {
        return animations.get(name);
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

    public Parent getHistortMainPane() {
        return histortMainPane;
    }

    public void setHistortMainPane(Parent histortMainPane) {
        this.histortMainPane = histortMainPane;
    }
}
