package com.zzk.filehelper.controller.component;

import com.google.common.eventbus.Subscribe;
import com.zzk.filehelper.component.CustomListCell;
import com.zzk.filehelper.component.MyHbox;
import com.zzk.filehelper.event.UpdateFileListViewEvent;
import com.zzk.filehelper.state.FileContainer;
import com.zzk.filehelper.state.SceneManager;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
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
    private Label statisticsLabel;

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


        statisticsLabel.setText("已选文件：" + FileContainer.instance.count() +
                "   " + "文件大小：" + FileContainer.instance.totalSize() + "MB");
        // 统计文件数量和大小
        FileContainer.instance.getFiles().addListener((InvalidationListener) observable -> {
            // 监听FileContainer状态变化
            statisticsLabel.setText("已选文件：" + FileContainer.instance.count() +
                    "   " + "文件大小：" + FileContainer.instance.totalSize() + "MB");
        });
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
