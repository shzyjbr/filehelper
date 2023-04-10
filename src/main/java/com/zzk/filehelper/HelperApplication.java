package com.zzk.filehelper;

import com.zzk.filehelper.state.SceneManager;
import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class HelperApplication extends Application {

    private double offsetX,offsetY;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        SceneManager.instance.mainStage = primaryStage;

        Parent root =FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        SceneManager.instance.setMainPane(root);


        scene = new Scene(root, null);
        primaryStage.setScene(scene);
        primaryStage.setFullScreenExitHint("");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/img/logo.png")).toExternalForm()));
        primaryStage.setTitle("文件传输助手");
        primaryStage.show();



        // scene.setOnMousePressed(event -> {
        //     offsetX = event.getSceneX();
        //     offsetY = event.getSceneY();
        // });
        //
        // scene.setOnMouseDragged(event -> {
        //     primaryStage.setX(event.getScreenX()-offsetX);
        //     primaryStage.setY(event.getScreenY()-offsetY);
        // });

    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("init()...");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("stop()...");

    }

    public static void main(String[] args) {
        System.out.println("launch main win");
        launch();
    }
}