package com.zzk.filehelper.component;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * @Author zhouzekun
 * @Date 2023/10/3 16:54
 */
public class ThumbnailCell extends Button {

    public ThumbnailCell() {
        super();
        int size = 60;
        setPrefSize(size, size); // 设置大小
        setMaxSize(size, size);
        setMinSize(size, size);
        SVGPath svgPath = new SVGPath();
        String svgContent = "M530.354 83.891c25.407 0 46.004 20.583 46.004 45.974v204.803c0 19.043 15.447 34.481 34.503 34.481h218.694c25.407 0 46.003 20.583 46.003 45.974v420.448c0 69.825-56.64 126.429-126.51 126.429H254.51C184.64 962 128 905.396 128 835.57V210.32c0-69.825 56.64-126.429 126.51-126.429h275.844z m119.257 389.191c-34.458-34.595-90.237-34.668-124.785-0.162L345.591 651.938l-1.413 1.435c-46.187 47.651-45.984 123.939 0.81 171.337l1.696 1.685c47.595 46.387 123.566 45.949 170.625-1.341l173.435-174.668 0.338-0.35c6.867-7.28 6.747-18.783-0.355-25.917-7.216-7.25-18.908-7.242-26.115 0.016L491.271 798.71l-1.065 1.054c-33.176 32.275-86.085 31.922-118.83-1.043l-0.181-0.185-0.977-1.005c-31.91-33.389-31.399-86.477 1.396-119.233L550.85 499.28l0.607-0.597c20.161-19.495 52.197-19.254 72.064 0.692 20.037 20.117 20.101 52.752 0.143 72.948L460.421 737.508l-0.367 0.361c-6.892 6.586-17.787 6.484-24.556-0.311-7.064-7.093-7.17-18.573-0.237-25.796l138.514-144.318 0.332-0.354c6.743-7.398 6.43-18.897-0.792-25.909-7.337-7.124-19.027-6.917-26.11 0.463L408.69 685.962l-0.622 0.658c-20.302 21.843-19.778 56.028 1.34 77.23 21.317 21.401 55.848 21.37 77.126-0.072L649.856 598.51l1.02-1.048c33.291-34.797 32.843-90.136-1.265-124.38zM624.837 80.995a18.4 18.4 0 0 1 12.725 5.109l234.743 224.735c7.34 7.028 7.595 18.675 0.567 26.016a18.4 18.4 0 0 1-13.291 5.675H638.637c-17.783 0-32.2-14.416-32.2-32.2V99.395c0-10.162 8.238-18.4 18.4-18.4z";
        svgPath.setContent(svgContent);
        svgPath.setFill(Color.BLACK);
        Bounds bounds = svgPath.getBoundsInLocal();
        double scale = Math.min(size/bounds.getWidth(), size / bounds.getHeight());
        System.out.println(scale);
        svgPath.setScaleX(scale);
        svgPath.setScaleY(scale);

        this.setGraphic(svgPath);

        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setStyle(
                "-fx-background-color:#f2f9f8;"+         //设置背景颜色
                        "-fx-background-radius:10;"+     //设置背景圆角
                        "-fx-border-width:0;");          //设置边框宽度
        setDisable(true);
    }

    private SVGPath createPath(String d, String fill, String hoverFill) {
        SVGPath path = new SVGPath();
        path.getStyleClass().add("svg");
        path.setContent(d);
        path.setStyle("-fill:" + fill + ";-hover-fill:"+hoverFill+';');
        return path;
    }
}
