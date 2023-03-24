package com.zzk.filehelper;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private ListView<String> devicesList;

    @FXML
    private BorderPane leftPane;


    @FXML
    void initialize() {
        System.out.println("初始化HelloController");
    }

    private Stage findStage() {
        return (Stage) leftPane.getScene().getWindow();
    }


    @FXML
    void onCloseAction(MouseEvent event) {
        //disposeMediaPlayer();
        Platform.exit();
        //System.exit(0);
    }
    @FXML
    void onFullAction(MouseEvent event) {
        Stage stage = findStage();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    void onMiniAction(MouseEvent event) {
        Stage stage = findStage();
        stage.setIconified(true);
    }

}