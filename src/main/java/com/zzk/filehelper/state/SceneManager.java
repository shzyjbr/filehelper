package com.zzk.filehelper.state;

import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Data;

import java.util.HashMap;

/**
 * @author: kkrunning
 * @since: 2023/3/25 15:50
 * @description:
 */
@Data
public class SceneManager {

    public static final SceneManager instance = new SceneManager();

    private Parent mainPane;

    private Node historyPane;

    private Parent histortMainPane;

    private Node fileDetailLyout;

    /**
     * addmore 模态窗
     */
    private Stage modalStage;

    /**
     * 遮罩层
     */
    private Stage maskStage;

    /**
     * 主舞台
     */
    private Stage mainStage;

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
}
