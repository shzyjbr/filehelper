package com.zzk.filehelper.controller.component;

import com.zzk.filehelper.state.SceneManager;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class HistoryController {

    @FXML
    private StackPane backStackPane;

    @FXML
    private AnchorPane topBar;

    @FXML
    void backToMain(MouseEvent event) {
        System.out.println("go back!");
        Timeline inTimeLine = (Timeline) SceneManager.instance.getAnimation("historyIn");
        Timeline outTimeLine = (Timeline) SceneManager.instance.getAnimation("historyOut");
        SceneManager.instance.getMainPane()
                .lookup("#masterPane").setVisible(true);
        inTimeLine.stop();
        outTimeLine.play();

    }

}
