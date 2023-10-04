package com.zzk.filehelper.component;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * @Author zhouzekun
 * @Date 2023/10/4 15:06
 */
public class FileAttachStackPane extends StackPane {

    private String filename;
    public FileAttachStackPane(String filename) {
        super();
        this.filename = filename;
        Region region = new Region();
        region.setPrefSize(35,35);
        region.setMaxSize(35,35);
        region.getStylesheets().add("css/send.css");
        region.getStyleClass().add("thumbnail-region");
        this.getChildren().add(region);
        this.setPrefSize(60,60);
        this.setMinSize(60,60);

        this.getStylesheets().add("css/send.css");
        this.getStyleClass().add("thumbnail-stackpane");
    }

    public String getFilename() {
        return filename;
    }
}
