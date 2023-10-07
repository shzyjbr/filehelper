package com.zzk.filehelper.util;

import com.zzk.filehelper.state.FileContainer;
import com.zzk.filehelper.state.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author kelton
 * @Date 2023/10/5 23:16
 * @Version 1.0
 */
public class FileUtil {
    public static void showFileChooserDialog() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(SceneManager.instance.mainStage);
        if (selectedFile != null && !selectedFile.isEmpty()) {
            for (File file : selectedFile) {
                FileContainer.instance.addFile(file.getCanonicalPath());
            }
        }
    }
}
