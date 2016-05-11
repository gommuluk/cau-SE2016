package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

/**
 * Created by SH on 2016-05-12.
 */
public class ControlPaneSceneController {

    private void initialize(){

    }

    @FXML //
    private void onTBBtnFileOpenClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        //fileChooser.showOpenDialog(stage);
    }

}
