package com.zzk.filehelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.zzk.filehelper.state.SceneManager;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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

    private Timeline showAnim;
    private Timeline hideAnim;

    private Parent historyMainPane;

    public Timeline getShowAnim() {
        return showAnim;
    }

    public void setShowAnim(Timeline showAnim) {
        this.showAnim = showAnim;
    }

    public Timeline getHideAnim() {
        return hideAnim;
    }

    public void setHideAnim(Timeline hideAnim) {
        this.hideAnim = hideAnim;
    }

    public Parent getHistoryMainPane() {
        return historyMainPane;
    }

    public void setHistoryMainPane(Parent historyMainPane) {
        this.historyMainPane = historyMainPane;
    }

    @FXML
    void showHistoryWin(MouseEvent event) {

        System.out.println("historyStackPane," +( historyStackPane == null));
        System.out.println("showAnim," +( showAnim == null));
        System.out.println("hideAnim," +( hideAnim == null));
        // showAnim已经注入
        //    展示历史记录面板
        // historyStackPane.setTranslateX(300);
        historyMainPane.setVisible(true);
        hideAnim.stop();
        showAnim.play();


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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/info.fxml"));
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
        System.out.println("ReceiveController init...");

        RotateTransition rt =  new RotateTransition();
        rt.setNode(logoRegion);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setRate(0.01);
        rt.play();
        rt.setOnFinished(actionEvent -> {
            rt.play();
        });

        initInfoPopup();
        AnchorPane histortMainPane = (AnchorPane) SceneManager.instance.getHistortMainPane();
        System.out.println("hideAnim:" +  (SceneManager.instance.getAnimation("hideAnim") == null));
    }

    private Stage findStage() {
        return (Stage) historyStackPane.getScene().getWindow();
    }

}
