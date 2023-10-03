package com.zzk.filehelper;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private Stage primaryStage; // 主舞台对象
    private StackPane mainLayout; // 主界面布局
    private StackPane otherLayout; // 另一个页面布局

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createMainLayout(); // 创建主界面布局
        createOtherLayout(); // 创建另一个页面布局
        showMainPage(); // 显示主界面
    }

    // 创建主界面布局
    private void createMainLayout() {
        Button goToOtherPageButton = new Button("Go to Other Page");
        goToOtherPageButton.setOnAction(e -> showOtherPage()); // 点击按钮跳转到另一个页面

        mainLayout = new StackPane(goToOtherPageButton);
    }

    // 创建另一个页面布局
    private void createOtherLayout() {
        Button goBackButton = new Button("Go Back");
        goBackButton.setOnAction(e -> showMainPage()); // 点击按钮回到主界面

        otherLayout = new StackPane(goBackButton);
        otherLayout.setOpacity(0.0); // 初始设置为透明
    }

    // 显示主界面
    private void showMainPage() {
        primaryStage.setTitle("Main Page");

        Scene scene = new Scene(mainLayout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 显示另一个页面
    private void showOtherPage() {
        primaryStage.setTitle("Other Page");

        Scene scene = primaryStage.getScene();
        scene.setRoot(otherLayout);

        // 创建一个淡入的过渡动画
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), otherLayout);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}