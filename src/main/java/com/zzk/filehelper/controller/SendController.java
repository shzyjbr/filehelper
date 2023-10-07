package com.zzk.filehelper.controller;

import com.google.common.eventbus.Subscribe;
import com.zzk.filehelper.component.FileAttachStackPane;
import com.zzk.filehelper.component.MyHbox;
import com.zzk.filehelper.component.ThumbnailCell;
import com.zzk.filehelper.event.EventCenter;
import com.zzk.filehelper.event.ShowSelectedFilesEvent;
import com.zzk.filehelper.state.FileContainer;
import com.zzk.filehelper.state.SceneManager;
import com.zzk.filehelper.util.FileUtil;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
        FileUtil.showFileChooserDialog();
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
    public void openModalWindow() throws IOException {

        // 遮罩层
        Stage maskStage = SceneManager.instance.getMaskStage();
        if (maskStage == null) {
            // 创建遮罩层的 Stage
            maskStage = new Stage();
            maskStage.initOwner(SceneManager.instance.mainStage);
            maskStage.initModality(Modality.APPLICATION_MODAL);
            maskStage.initStyle(StageStyle.TRANSPARENT);

            // 创建遮罩层的 Scene
            StackPane maskLayout = new StackPane();
            maskLayout.setStyle("-fx-background-color: rgba(242, 249, 248, 0.5);");

            Scene maskScene = new Scene(maskLayout);
            maskScene.setFill(Color.TRANSPARENT);
            maskStage.setScene(maskScene);
            SceneManager.instance.setMaskStage(maskStage);
            // maskLayout.visibleProperty().bind(SceneManager.instance.getMainStage().getScene().getWindow().showingProperty());

            SceneManager.instance.getMainStage().widthProperty().addListener(e -> {
                maskLayout.setPrefSize(SceneManager.instance.getMainStage().getWidth(),
                        SceneManager.instance.getMainStage().getHeight());
                // 使模态窗口始终在主窗口中居中
                SceneManager.instance.getModalStage().setX(SceneManager.instance.getMainStage().getX()+SceneManager.instance.getMainStage().getWidth()/2-SceneManager.instance.getModalStage().getWidth()/2);
            });
            SceneManager.instance.getMainStage().heightProperty().addListener(e -> {
                maskLayout.setPrefSize(SceneManager.instance.getMainStage().getWidth(),
                        SceneManager.instance.getMainStage().getHeight());
                SceneManager.instance.getModalStage().setY(SceneManager.instance.getMainStage().getY()+SceneManager.instance.getMainStage().getHeight()/2-SceneManager.instance.getModalStage().getHeight()/2);
            });
        }


        // modalStage.initStyle(StageStyle.TRANSPARENT);
        Stage modalStage = SceneManager.instance.getModalStage();
        if (modalStage == null) {
            // 第一次加载初始化modalScene
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = fxmlLoader.getClassLoader().getResource("fxml/component/addMore.fxml");
            fxmlLoader.setLocation(url);
            StackPane stackPane = fxmlLoader.load();

            Scene modalScene = new Scene(stackPane,400,250);
            modalStage = new Stage();
            modalStage.setScene(modalScene);
            // 去除状态栏
            modalStage.initStyle(StageStyle.UNDECORATED);
            modalScene.setFill(Color.TRANSPARENT);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            SceneManager.instance.setModalStage(modalStage);
        }
        modalStage.setX(SceneManager.instance.getMainStage().getX()+SceneManager.instance.getMainStage().getWidth()/2-SceneManager.instance.getModalStage().getWidth()/2);
        modalStage.setY(SceneManager.instance.getMainStage().getY()+SceneManager.instance.getMainStage().getHeight()/2-SceneManager.instance.getModalStage().getHeight()/2);
        maskStage.show();
        modalStage.showAndWait();
    }

}
