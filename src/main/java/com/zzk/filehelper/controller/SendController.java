package com.zzk.filehelper.controller;

import com.google.common.eventbus.Subscribe;
import com.zzk.filehelper.component.FileAttachStackPane;
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
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    private Label fileCount;

    @FXML
    private Label fileSize;

    @FXML
    private HBox selectArea;

    @FXML
    private VBox selectedResources;

    @FXML
    private HBox thumbnailArea;

    private Timeline inTimeLine;
    private Timeline outTimeLine;

    public SendController() {
        EventCenter.register(this);
    }

    /**
     * 处理注册监听器之类的工作
     */
    @FXML
    void initialize() {
        fileCount.setText("已选文件：" + FileContainer.instance.count());
        fileSize.setText("文件大小：" + FileContainer.instance.totalSize() + "MB");
        // 计算文件数量和大小
        FileContainer.instance.getFiles().addListener((InvalidationListener) observable -> {
            // 监听FileContainer状态变化
            fileCount.setText("已选文件：" + FileContainer.instance.count());
            fileSize.setText("文件大小：" + FileContainer.instance.totalSize() + "MB");
            if (FileContainer.instance.count() == 0) {
                selectArea.setVisible(true);
                selectedResources.setVisible(false);
            } else {
                selectArea.setVisible(false);
                selectedResources.setVisible(true);
            }
        });
        // 更新缩略图UI
        // 监听ObservableList的变化
        ListChangeListener<String> listChangeListener = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    // 处理新增元素
                    for (String addedItem : change.getAddedSubList()) {
                        FileAttachStackPane fileAttachStackPane = new FileAttachStackPane(addedItem);
                        thumbnailArea.getChildren().add(fileAttachStackPane);
                    }
                }
                if (change.wasRemoved()) {
                    // 处理删除元素
                    for (String removedItem : change.getRemoved()) {
                        for (Node pane : thumbnailArea.getChildren()) {
                            FileAttachStackPane fileAttachStackPane =(FileAttachStackPane)pane;
                            if (fileAttachStackPane.getFilename().equals(removedItem)) {
                                thumbnailArea.getChildren().remove(pane);
                                break;
                            }
                        }
                    }
                }
            }
        };
        FileContainer.instance.getFiles().addListener(listChangeListener);
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
        }
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


    @FXML
    /**
     * 打开模态窗口
     */
    public void openModalWindow() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.UTILITY);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> modalStage.close());

        VBox modalRoot = new VBox(closeButton);
        Scene modalScene = new Scene(modalRoot, 200, 150);

        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

}
