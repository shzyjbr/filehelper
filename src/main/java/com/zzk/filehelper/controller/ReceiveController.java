package com.zzk.filehelper.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.zzk.filehelper.state.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ReceiveController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private StackPane historyStackPane;

    @FXML
    private StackPane infoStackPane;

    private ContextMenu infoPopup;

    @FXML
    private Region logoRegion;
    private Label hostLabel;
    private Label ipLabel;
    private Label portLabel;

    private Timeline inTimeLine;
    private Timeline outTimeLine;




    @FXML
    void showHistoryWin(MouseEvent event) throws IOException {
        //    展示历史记录面板
        Node historyMainPane = SceneManager.instance.getHistortMainPane();
        if (historyMainPane == null) {
            // 第一次加载初始化fileDetailLayout
            historyMainPane = initHistoryPane();
        }
        // historyStackPane.setTranslateX(300);
        historyMainPane.setVisible(true);
        outTimeLine.stop();
        inTimeLine.play();

    }

    private Node initHistoryPane() throws IOException {
        Node historyPane;
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = fxmlLoader.getClassLoader().getResource("fxml/component/history.fxml");
        fxmlLoader.setLocation(url);
        historyPane = fxmlLoader.load();
        historyPane.setTranslateX(SceneManager.instance.getMainPane().getScene().getWidth());
        historyPane.setVisible(false);
        initAnimation(historyPane);
        StackPane mainStackPane = (StackPane) SceneManager.instance.getMainPane().lookup("#mainStackPane");
        mainStackPane.getChildren().add(historyPane);
        return historyPane;
    }

    private void initAnimation(Node historyPane) {
        inTimeLine = new Timeline(new KeyFrame(Duration.millis(300),
                new KeyValue(historyPane.translateXProperty(), 0)));
        outTimeLine = new Timeline(new KeyFrame(Duration.millis(300),
                new KeyValue(historyPane.translateXProperty(), SceneManager.instance.getMainPane().getScene().getWidth())));
        SceneManager.instance.setHistoryPane(historyPane);
        SceneManager.instance.addAnimation("historyIn", inTimeLine);
        SceneManager.instance.addAnimation("historyOut", outTimeLine);
        inTimeLine.setOnFinished(e -> {
            SceneManager.instance.getMainPane()
                    .lookup("#masterPane").setVisible(false);
            historyPane.setVisible(true);
        });
        outTimeLine.setOnFinished(e-> {
            SceneManager.instance.getMainPane()
                    .lookup("#masterPane").setVisible(true);
            historyPane.setVisible(false);
        });
    }

    @FXML
    void showInfo(MouseEvent event) {
        StackPane source = (StackPane) event.getSource();
        System.out.println(event.getSource());
        Bounds bounds = source.localToScreen(source.getBoundsInLocal());
        double minX = bounds.getMinX();
        double minY = bounds.getMinY();
        System.out.println(minX + " " + minY);
        infoPopup.show(findStage(), bounds.getMinX()-153, bounds.getMinY()+36);
    }

    private void initInfoPopup() {
        infoPopup = new ContextMenu(new SeparatorMenuItem());
        Parent infoRoot = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/component/info.fxml"));
            infoRoot = fxmlLoader.load();
            ObservableMap<String, Object> namespace = fxmlLoader.getNamespace();

            hostLabel = (Label) namespace.get("hostLabel");
            ipLabel = (Label) namespace.get("ipLabel");
            portLabel = (Label) namespace.get("portLabel");

            hostLabel.textProperty().set("host:kkrunning-pc");
            ipLabel.textProperty().set("ip:192.168.1.102");
            portLabel.textProperty().set("host:9000");

        } catch (IOException e) {
            e.printStackTrace();
        }
        infoPopup.getScene().setRoot(infoRoot);
    }


    @FXML
    void initialize() {

        // logo旋转动画
        RotateTransition rt =  new RotateTransition();
        rt.setNode(logoRegion);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setRate(0.01);
        rt.play();
        rt.setOnFinished(actionEvent -> {
            rt.play();
        });
        // 初始化用户设备信息
        initInfoPopup();
    }

    private Stage findStage() {
        return (Stage) historyStackPane.getScene().getWindow();
    }

}
