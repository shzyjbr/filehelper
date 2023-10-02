package com.zzk.filehelper;

import com.google.common.eventbus.Subscribe;
import com.zzk.filehelper.component.MyHbox;
import com.zzk.filehelper.event.EventCenter;
import com.zzk.filehelper.event.ShowSelectedFilesEvent;
import com.zzk.filehelper.state.FileContainer;
import com.zzk.filehelper.state.SceneManager;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * @Author zhouzekun
 * @Date 2023/10/2 9:57
 */
public class SendController {

    @FXML
    private HBox selectArea;

    @FXML
    private VBox selectedResources;

    public SendController() {
        EventCenter.register(this);
    }

    @FXML
    void openFileChooserDialog(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(SceneManager.instance.mainStage);
        if (selectedFile != null && !selectedFile.isEmpty()) {
            for (File file : selectedFile) {
                FileContainer.instance.addFile(file.getCanonicalPath());
            }
            EventCenter.postMessage(new ShowSelectedFilesEvent());
        }
    }


    @Subscribe
    public void showSelectedFilesArea(ShowSelectedFilesEvent event) throws IOException {
        selectArea.setVisible(false);
        selectedResources.setVisible(true);
        Label fileCount = (Label)selectArea.getParent().lookup("#fileCount");
        fileCount.setText("已选文件：" + FileContainer.instance.count());
        Label fileSize = (Label)selectArea.getParent().lookup("#fileSize");
        fileSize.setText("文件大小：" + FileContainer.instance.totalSize() + "MB");
        FileContainer.instance.getFiles().addListener((InvalidationListener) observable -> {
            // 监听FileContainer状态变化
            fileCount.setText("已选文件：" + FileContainer.instance.count());
            fileSize.setText("文件大小：" + FileContainer.instance.totalSize() + "MB");
            if (FileContainer.instance.count() == 0) {
                selectArea.setVisible(true);
                selectedResources.setVisible(false);
            }
        });
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        URL url = fxmlLoader.getClassLoader().getResource("fxml/component/displayFile.fxml");
//        fxmlLoader.setLocation(url);
//        HBox fileBox = (HBox)fxmlLoader.load();

        for (String file : FileContainer.instance.getFiles()) {
            MyHbox myHbox = new MyHbox(file);
            selectedResources.getChildren().add(myHbox);
        }
        selectedResources.setSpacing(5);
        selectedResources.setFillWidth(true);
    }
}
