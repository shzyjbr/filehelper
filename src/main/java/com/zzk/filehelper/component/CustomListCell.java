package com.zzk.filehelper.component;

import com.zzk.filehelper.state.FileContainer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class CustomListCell extends ListCell<String> {
    private MyHbox hbox;

    public CustomListCell() {
        super();
        hbox = new MyHbox("");
        hbox.getBtn().setOnAction(event -> {
            String item = getItem();
            FileContainer.instance.remove(item);
        });
        hbox.getLabel().textProperty().bind(itemProperty());
        setStyle("-fx-background-color: null;");
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
        } else {
            setGraphic(hbox);
            if (isSelected()) {
                hbox.getLabel().setTextFill(Color.BLACK);
            } else {
                hbox.getLabel().setTextFill(Color.BLACK);
            }
        }
    }
}