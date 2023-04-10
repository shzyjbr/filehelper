package com.zzk.filehelper;

import java.net.URL;
import java.util.ResourceBundle;

import com.zzk.filehelper.state.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ReceiveController reveiveMainPaneController;

    @FXML
    private AnchorPane reveiveMainPane;
    @FXML
    private Parent sendMainPane;
    @FXML
    private Parent settingMainPane;

    @FXML
    private AnchorPane receivePane;

    @FXML
    private StackPane receiveStackPane;

    @FXML
    private AnchorPane sendPane;

    @FXML
    private Region sendRegion;

    @FXML
    private StackPane sendStackPane;

    @FXML
    private AnchorPane settingPane;

    @FXML
    private Region settingRegion;

    @FXML
    private StackPane settingStackPane;

    @FXML
    private Region wifiRegion;

    @FXML
    private AnchorPane histortMainPane;
    @FXML
    private StackPane backStackPane;

    @FXML
    private AnchorPane topBar;

    @FXML
    private AnchorPane sliderPane;

    @FXML
    private AnchorPane masterPane;



    private Timeline hideAnim;

    private Timeline showAnim;


    /**
     * 接收选项点击处理
     * @param event
     */
    @FXML
    void receiveActive(MouseEvent event) {
        System.out.println("receiveActive");

        reveiveMainPane.setVisible(true);
        sendMainPane.setVisible(false);
        settingMainPane.setVisible(false);


        receiveStackPane.setStyle("-fx-background-color: #009688");
        sendStackPane.setStyle("-fx-background-color: #f2f9f8");
        settingStackPane.setStyle("-fx-background-color: #f2f9f8");
        wifiRegion.setStyle("-fx-background-color: white");
        sendRegion.setStyle("-fx-background-color: black");
        settingRegion.setStyle("-fx-background-color: black");


    }


    /**
     * 发送选项卡点击处理
     * @param event
     */
    @FXML
    void sendActivte(MouseEvent event) {
        System.out.println("sendActivte");

        reveiveMainPane.setVisible(false);
        sendMainPane.setVisible(true);
        settingMainPane.setVisible(false);
        receiveStackPane.setStyle("-fx-background-color: #f2f9f8");
        sendStackPane.setStyle("-fx-background-color: #009688");
        settingStackPane.setStyle("-fx-background-color: #f2f9f8");
        wifiRegion.setStyle("-fx-background-color: black");
        sendRegion.setStyle("-fx-background-color: white");
        settingRegion.setStyle("-fx-background-color: black");

    }

    /**
     * 设置选项点击处理
     * @param event
     */
    @FXML
    void settingActive(MouseEvent event) {
        System.out.println("settingActive");

        reveiveMainPane.setVisible(false);
        sendMainPane.setVisible(false);
        settingMainPane.setVisible(true);
        receiveStackPane.setStyle("-fx-background-color: #f2f9f8");
        sendStackPane.setStyle("-fx-background-color: #f2f9f8");
        settingStackPane.setStyle("-fx-background-color: #009688");
        wifiRegion.setStyle("-fx-background-color: black");
        sendRegion.setStyle("-fx-background-color: black");
        settingRegion.setStyle("-fx-background-color: white");


    }

    /**
     * 收起历史记录面板，返回主界面
     */
    @FXML
    void backToMain() {
        showAnim.stop();
        hideAnim.play();
    }

    private void initAnim() {
        System.out.println("initAnim()...");

        showAnim = new Timeline(new KeyFrame(Duration.millis(300),
                new KeyValue(sliderPane.translateXProperty(), 0)));

        hideAnim = new Timeline(new KeyFrame(Duration.millis(300),
                new KeyValue(sliderPane.translateXProperty(), 880)));
        hideAnim.setOnFinished(event -> histortMainPane.setVisible(false));

        reveiveMainPaneController.setShowAnim(showAnim);
        reveiveMainPaneController.setHideAnim(hideAnim);
        reveiveMainPaneController.setHistoryMainPane(histortMainPane);

        SceneManager.instance.addAnimation("showAnim", showAnim);
        SceneManager.instance.addAnimation("hideAnim", hideAnim);

    }

    private Stage findStage() {
        return (Stage) masterPane.getScene().getWindow();
    }

    @FXML
    void initialize() {
        System.out.println("MainController init...");

        //初始默认接收选项卡
        reveiveMainPane.setVisible(true);
        sendMainPane.setVisible(false);
        settingMainPane.setVisible(false);
        //历史面板初始不可见
        histortMainPane.setVisible(false);
        //把historyMainPane管理起来
        SceneManager.instance.setHistortMainPane(histortMainPane);
        receiveStackPane.setStyle("-fx-background-color: #009688");
        sendStackPane.setStyle("-fx-background-color: #f2f9f8");
        settingStackPane.setStyle("-fx-background-color: #f2f9f8");
        wifiRegion.setStyle("-fx-background-color: white");
        sendRegion.setStyle("-fx-background-color: black");
        settingRegion.setStyle("-fx-background-color: black");

        initAnim();

    //    设置高度变化监听事件

        // reveiveMainPane.getScene().getWindow().heightProperty().addListener((observableValue, number, t1) -> {
        //     System.out.println(number);
        //     System.out.println(t1);
        // });
        Stage primaryStage = SceneManager.instance.mainStage;
        primaryStage.heightProperty().addListener(((observableValue, oldValue, newValue) -> {
            System.out.println(oldValue);
            System.out.println(newValue);
        }));
    }

}
