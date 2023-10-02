package com.zzk.filehelper.component;

import com.zzk.filehelper.state.FileContainer;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;

/**
 * @Author zhouzekun
 * @Date 2023/10/2 15:35
 */
public class MyHbox extends HBox implements Cloneable {

    private Label label;

    private StackPane stackPane;

    private Region region;

    public MyHbox(String filename) {
        super();
        label = new Label(filename);
        String svgContent = "M865.578667 223.701333c16.64 0 30.421333 13.781333 30.421333 31.317334v16.213333a31.146667 31.146667 0 0 1-30.421333 31.317333H158.464A31.146667 31.146667 0 0 1 128 271.232v-16.213333c0-17.536 13.824-31.317333 30.464-31.317334H282.88c25.258667 0 47.232-17.962667 52.906667-43.306666l6.528-29.098667C352.469333 111.658667 385.749333 85.333333 423.893333 85.333333h176.213334c37.717333 0 71.424 26.325333 81.152 63.872l6.954666 31.146667a54.613333 54.613333 0 0 0 52.949334 43.349333h124.416z m-63.189334 592.682667c12.970667-121.045333 35.712-408.618667 35.712-411.52a31.829333 31.829333 0 0 0-7.68-23.808 30.976 30.976 0 0 0-22.357333-9.984H216.32c-8.533333 0-16.682667 3.712-22.357333 9.984a33.706667 33.706667 0 0 0-8.106667 23.808l2.261333 27.605333c6.058667 75.221333 22.912 284.757333 33.834667 383.914667 7.68 73.045333 55.637333 118.954667 125.056 120.618667 53.589333 1.237333 108.8 1.664 165.205333 1.664 53.162667 0 107.093333-0.426667 162.346667-1.664 71.850667-1.237333 119.722667-46.336 127.872-120.618667z";
        Group svg = new Group(
                createPath(svgContent, "blue", "darkblue")
        );
        Bounds bounds = svg.getBoundsInParent();
        double scale = Math.min(20/bounds.getWidth(), 20 / bounds.getHeight());
        svg.setScaleX(scale);
        svg.setScaleY(scale);

        Button btn = new Button();
        btn.setGraphic(svg);
        btn.setMaxSize(35, 35);
        btn.setMinSize(35, 35);
        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        btn.setStyle(
                "-fx-background-color:#f2f9f8;"+         //设置背景颜色
                        "-fx-background-radius:10;"+     //设置背景圆角
                        "-fx-border-width:0;");          //设置边框宽度
        // 设置鼠标进入按钮时的样式
        btn.setOnMouseEntered(event -> {
            btn.setStyle(
                    "-fx-background-color:#e5ecea;"+
                            "-fx-background-radius:10;"+
                            "-fx-border-width:0;");
        });

        // 设置鼠标离开按钮时的样式
        btn.setOnMouseExited(event -> {
            btn.setStyle(
                    "-fx-background-color:#f2f9f8;"+
                            "-fx-background-radius:10;"+
                            "-fx-border-width:0;");
        });
        HBox.setHgrow(label, Priority.ALWAYS);
        this.getChildren().addAll(label, btn);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setMinWidth(500);
        this.setMaxWidth(500);
        this.setStyle(
                "-fx-background-color:#f2f9f8;"+
                        "-fx-padding:5px;"+
                        "-fx-background-radius:10;"+
                        "-fx-border-width:0;");
        btn.setOnMouseClicked(event -> {
            Parent parent = btn.getParent();
            VBox vBox = (VBox) parent.getParent();
            vBox.getChildren().remove(parent);
            FileContainer.instance.remove(filename);
            FileContainer.instance.print();
        });


    }

    public void setText(String filename) {
        label.setText(filename);
    }



    @Override
    public MyHbox clone() {
        try {
            return (MyHbox) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private static SVGPath createPath(String d, String fill, String hoverFill) {
        SVGPath path = new SVGPath();
        path.getStyleClass().add("svg");
        path.setContent(d);
        path.setStyle("-fill:" + fill + ";-hover-fill:"+hoverFill+';');
        return path;
    }
}
