package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.TestModel;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by SH on 2016-05-12.
 */
public class ControlPaneSceneController implements Initializable {

    @FXML private Button btnFileOpen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        this.setModel();
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


    }


    public void setModel(){
        TestModel t = new TestModel();
        btnFileOpen.disableProperty().bind(t.isActive);


        t.run();
        System.out.println(t.isActive);
    }

}
