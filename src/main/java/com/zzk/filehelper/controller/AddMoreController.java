package com.zzk.filehelper.controller;

import com.zzk.filehelper.state.SceneManager;
import com.zzk.filehelper.util.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class AddMoreController {

    @FXML
    void handlerClose(MouseEvent event) {
        SceneManager.instance.getMaskStage().close();
        SceneManager.instance.getModalStage().close();
    }

    @FXML
    void openFileChooser(MouseEvent event) throws IOException {
        handlerClose(null);
        FileUtil.showFileChooserDialog();
    }

}
