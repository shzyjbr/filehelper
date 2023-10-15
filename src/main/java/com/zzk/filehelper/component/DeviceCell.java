package com.zzk.filehelper.component;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * @Author kelton
 * @Date 2023/10/14 22:22
 * @Version 1.0
 */
public class DeviceCell extends HBox {

    private StackPane stackPane;
    private Label label;

    public DeviceCell(String labelContent) {
        super();
        this.getStylesheets().add("css/component/device-label.css");
        this.getStyleClass().add("device-hbox");

        // add a label
        Label label = new Label(labelContent);
        label.setFont(Font.font(18));
        label.setStyle("-fx-alignment: center-left;");


        // add a stackpane
        Region region = new Region();
        region.setPrefSize(21,25);
        region.setMaxSize(21,25);
        region.getStyleClass().add("loop-icon");
        StackPane stackPane = new StackPane(region);
        stackPane.setPrefSize(35,35);
        stackPane.getStyleClass().add("loop-bg");
        stackPane.setOnMouseEntered( e -> {
            stackPane.setStyle("-fx-background-color:#e5ecea;");
        });
        stackPane.setOnMouseExited( e -> {
            stackPane.setStyle("-fx-background-color:#f2f9f8;");
        });
        stackPane.setOnMouseClicked( e -> {
            System.out.println("test connection : " + labelContent);
            // 阻止点击事件传播到父组件
            e.consume();
        });


        this.getChildren().addAll(stackPane, label);
        this.setStyle("-fx-alignment: center-left;");

        this.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("device: " + labelContent);
        });

    }

    public StackPane getStackPane() {
        return stackPane;
    }

    public void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
