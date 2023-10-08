package com.zzk.filehelper.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.leewyatt.rxcontrols.animation.carousel.AnimFlip;
import com.leewyatt.rxcontrols.controls.RXCarousel;
import com.leewyatt.rxcontrols.pane.RXCarouselPane;
import com.zzk.filehelper.state.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {

    @FXML
    private RXCarousel mainCarousel;

    @FXML
    private ToggleGroup navGroup;



    @FXML
    void initialize() throws IOException {
        Pane p1 = FXMLLoader.load(getClass().getResource("/fxml/receive.fxml"));
        RXCarouselPane receivePane = new RXCarouselPane(p1);
        Pane p2 = FXMLLoader.load(getClass().getResource("/fxml/send.fxml"));
        RXCarouselPane sendPane = new RXCarouselPane(p2);
        Pane p3 = FXMLLoader.load(getClass().getResource("/fxml/setting.fxml"));
        RXCarouselPane settingPane = new RXCarouselPane(p3);

        mainCarousel.setPaneList(receivePane, sendPane, settingPane);
        mainCarousel.setCarouselAnimation(new AnimFlip());
        navGroup.selectedToggleProperty().addListener((ob, ov, nv) -> {
            int index = navGroup.getToggles().indexOf(nv);
            mainCarousel.setSelectedIndex(index);
        });

    }

}
