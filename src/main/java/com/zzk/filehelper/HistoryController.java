package com.zzk.filehelper;

import com.zzk.filehelper.state.SceneManager;
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
        backStackPane.getScene().setRoot(SceneManager.instance.getMainPane());
    }

    @FXML
    void initialize() {
        System.out.println("HistoryController init");
    }

}
