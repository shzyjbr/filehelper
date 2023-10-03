package com.zzk.filehelper.controller;

import com.google.common.eventbus.Subscribe;
import com.zzk.filehelper.component.CustomListCell;
import com.zzk.filehelper.component.MyHbox;
import com.zzk.filehelper.event.UpdateFileListViewEvent;
import com.zzk.filehelper.state.FileContainer;
import com.zzk.filehelper.state.SceneManager;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class FileDetailController {

    @FXML
    private VBox fileDetailVBox;

    @FXML
    private ListView<String> fileListView;

    @FXML
    void initialize() {
        fileListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new CustomListCell();
            }
        });

        fileListView.setItems(FileContainer.instance.getFiles());
    }

    @FXML
    void goBackToMain(MouseEvent event) {
        Timeline inTimeLine = (Timeline) SceneManager.instance.getAnimation("fileDetailIn");
        Timeline outTimeLine = (Timeline) SceneManager.instance.getAnimation("fileDetailOut");
        SceneManager.instance.getMainPane()
                .lookup("#masterPane").setVisible(true);
        inTimeLine.stop();
        outTimeLine.play();
    }


}
