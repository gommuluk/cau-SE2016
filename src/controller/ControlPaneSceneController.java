package controller;

import etc.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import model.TestModel;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by SH on 2016-05-12.
 */
public class ControlPaneSceneController implements Initializable {

    @FXML private Button btnFileOpen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setModel();
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

    public void setModel(){
        TestModel t = new TestModel();
        btnFileOpen.disableProperty().bind(t.isActive);

        t.run();

        System.out.println(t.isActive);
    }
}
