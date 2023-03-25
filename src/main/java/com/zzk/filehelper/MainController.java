package com.zzk.filehelper;

import java.net.URL;
import java.util.ResourceBundle;

import com.zzk.filehelper.state.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane reveiveMainPane;
    @FXML
    private AnchorPane sendMainPane;
    @FXML
    private AnchorPane settingMainPane;

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


    private String baseColor = "#f2f9f8";

    private Timeline showAnim;
    private Timeline hideAnim;

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

    private void initAnim() {
        // showAnim = new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(sliderPane.translateXProperty(), 0)));
        // hideAnim = new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(sliderPane.translateXProperty(), 300)));
        // hideAnim.setOnFinished(event -> drawerPane.setVisible(false));
    }

    @FXML
    void initialize() {
        System.out.println("MainController init...");

        //初始默认接收选项卡
        reveiveMainPane.setVisible(true);
        sendMainPane.setVisible(false);
        settingMainPane.setVisible(false);
        receiveStackPane.setStyle("-fx-background-color: #009688");
        sendStackPane.setStyle("-fx-background-color: #f2f9f8");
        settingStackPane.setStyle("-fx-background-color: #f2f9f8");
        wifiRegion.setStyle("-fx-background-color: white");
        sendRegion.setStyle("-fx-background-color: black");
        settingRegion.setStyle("-fx-background-color: black");

        initAnim();



    }

}
