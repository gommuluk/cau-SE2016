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

    @FXML // 비교 버튼이 클릭되었을 때의 동작
    private void onBtnCompareClicked(ActionEvent event) {

    }

    @FXML // copy-to-right 버튼이 클릭되었을 때의 동작
    private void onBtnCopyToRightClicked(ActionEvent event) {

    }

    @FXML // copy-to-left 버튼이 클릭되었을 때의 동작
    private void onBtnCopyToLeftClicked(ActionEvent event) {

    }

    @FXML
    private void onTBBtnFileOpenClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        //File selectedFile = fileChooser.showOpenDialog();
        //if (selectedFile != null) {
        //    mainStage.display(selectedFile);
        //}
    }

}
