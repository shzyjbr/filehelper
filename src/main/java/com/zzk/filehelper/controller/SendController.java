package com.zzk.filehelper.controller;

import com.google.common.eventbus.Subscribe;
import com.zzk.filehelper.component.MyHbox;
import com.zzk.filehelper.component.ThumbnailCell;
import com.zzk.filehelper.event.EventCenter;
import com.zzk.filehelper.event.ShowSelectedFilesEvent;
import com.zzk.filehelper.state.FileContainer;
import com.zzk.filehelper.state.SceneManager;
import javafx.animation.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * @Author zhouzekun
 * @Date 2023/10/2 9:57
 */
public class SendController {

    @FXML
    private HBox selectArea;

    @FXML
    private VBox selectedResources;

    private Timeline inTimeLine;
    private Timeline outTimeLine;

    public SendController() {
        EventCenter.register(this);
    }

    @FXML
    void openFileChooserDialog(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(SceneManager.instance.mainStage);
        if (selectedFile != null && !selectedFile.isEmpty()) {
            for (File file : selectedFile) {
                FileContainer.instance.addFile(file.getCanonicalPath());
            }
            EventCenter.postMessage(new ShowSelectedFilesEvent());
        }
    }


    @Subscribe
    public void showSelectedFilesArea(ShowSelectedFilesEvent event) throws IOException {
        selectArea.setVisible(false);
        selectedResources.setVisible(true);
        Label fileCount = (Label) selectArea.getParent().lookup("#fileCount");
        fileCount.setText("已选文件：" + FileContainer.instance.count());
        Label fileSize = (Label) selectArea.getParent().lookup("#fileSize");
        fileSize.setText("文件大小：" + FileContainer.instance.totalSize() + "MB");
        FileContainer.instance.getFiles().addListener((InvalidationListener) observable -> {
            // 监听FileContainer状态变化
            fileCount.setText("已选文件：" + FileContainer.instance.count());
            fileSize.setText("文件大小：" + FileContainer.instance.totalSize() + "MB");
            if (FileContainer.instance.count() == 0) {
                selectArea.setVisible(true);
                selectedResources.setVisible(false);
            }
        });

        HBox hBox = new HBox();
        for (String file : FileContainer.instance.getFiles()) {
            ThumbnailCell thumbnailCell = new ThumbnailCell();
            hBox.getChildren().add(thumbnailCell);
        }
        selectedResources.getChildren().add(hBox);
        Button detail = new Button("detail");
        detail.setOnMouseClicked(event1-> {
            try {
                showOtherPage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        selectedResources.getChildren().add(detail);
        selectedResources.setSpacing(5);
        selectedResources.setFillWidth(true);
        ScrollPane scrollPane = new ScrollPane(selectedResources);
        scrollPane.setFitToWidth(true); // 将ScrollPane的宽度调整为可视区域的宽度
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // 始终显示垂直滚动条
//        selectArea.getParent()

    }

    // 显示另一个页面
    public void showOtherPage() throws IOException {
        // 获取fileDetailLayout
        Node fileDetailLayout = SceneManager.instance.getFileDetailLyout();
        if (fileDetailLayout == null) {
            // 第一次加载初始化fileDetailLayout
            fileDetailLayout = initFileDetailLayout();
        }
        fileDetailLayout.setVisible(true);
        outTimeLine.stop();
        inTimeLine.play();


    }

    private Node initFileDetailLayout() throws IOException {
        Node fileDetailLayout;
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = fxmlLoader.getClassLoader().getResource("fxml/component/fileDetail.fxml");
        fxmlLoader.setLocation(url);
        fileDetailLayout = fxmlLoader.load();
        fileDetailLayout.setTranslateX(SceneManager.instance.getMainPane().getScene().getWidth());
        fileDetailLayout.setVisible(false);
        initAnimation(fileDetailLayout);
        StackPane mainStackPane = (StackPane) SceneManager.instance.getMainPane().lookup("#mainStackPane");
        mainStackPane.getChildren().add(fileDetailLayout);
        return fileDetailLayout;
    }

    /**
     * 初始化文件详情布局的动画
     * @param fileDetailLayout
     */
    private void initAnimation(Node fileDetailLayout) {
        inTimeLine = new Timeline(new KeyFrame(Duration.millis(300),
                new KeyValue(fileDetailLayout.translateXProperty(), 0)));
        outTimeLine = new Timeline(new KeyFrame(Duration.millis(300),
                new KeyValue(fileDetailLayout.translateXProperty(), SceneManager.instance.getMainPane().getScene().getWidth())));
        SceneManager.instance.setFileDetailLyout(fileDetailLayout);
        SceneManager.instance.addAnimation("fileDetailIn", inTimeLine);
        SceneManager.instance.addAnimation("fileDetailOut", outTimeLine);
        inTimeLine.setOnFinished(e -> {
            SceneManager.instance.getMainPane()
                    .lookup("#masterPane").setVisible(false);
            fileDetailLayout.setVisible(true);
        });
        outTimeLine.setOnFinished(e-> {
            SceneManager.instance.getMainPane()
                    .lookup("#masterPane").setVisible(true);
            fileDetailLayout.setVisible(false);
        });
    }
}
