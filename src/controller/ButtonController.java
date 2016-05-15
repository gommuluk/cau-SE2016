package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileManager;

import java.io.File;

/**
 * Created by SH on 2016-05-15.
 */
public class ButtonController {

    @FXML private AnchorPane editorScene;

    @FXML private AnchorPane editorToolbarScene = null;
    @FXML private ToggleButton btnEdit;
    @FXML private Button btnFileOpen;
    @FXML private Button btnFileSave;

    @FXML private Button btnMergeLeft;
    @FXML private Button btnMergeRight;
    @FXML private Button btnCompare;

    private TextArea textArea;

    @FXML
    public void initialize(){
        Platform.runLater(()->{
            if( this.editorToolbarScene != null ) {
                this.editorScene = (AnchorPane)((BorderPane)editorToolbarScene.getParent().getParent()).getCenter();
                this.textArea = (TextArea)editorScene.getChildren().get(1);
            }
        });
    }


    @FXML // 불러오기 버튼을 클릭했을 때의 동작
    private void onTBBtnLoadClicked(ActionEvent event) {
        //File chooser code
        try {
            System.out.println(editorToolbarScene.getParent().getParent().getId());
            Stage s = (Stage) btnFileOpen.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();                                            //File Chooser을 유저에게 보여준다.
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            File selectedFile = fileChooser.showOpenDialog(s);

                                                                                                    //선택된 파일의 Text를 해당되는 Edit Pane에 띄워준다.
            if(textArea.getParent().getParent().getId().equals("leftEditor")) {
                FileManager.getFileManager().getFileModelL().readFile(selectedFile.getPath());
                textArea.appendText(FileManager.getFileManager().getFileModelL().toString());
            }
            else {
                FileManager.getFileManager().getFileModelR().readFile(selectedFile.getPath());
                textArea.appendText(FileManager.getFileManager().getFileModelR().toString());
            }
        }
        catch(Exception e) {                                                                        // TODO Exception 별 처리 필요.

        }
    }

    @FXML // 저장 버튼을 클릭했을 때의 동작
    private void onTBBtnSaveClicked(ActionEvent event) {
        try {
                                                                                                    /* 저장 버튼이 속한 패널을 판단하고,
                                                                                                       해당 패널의 Text를 읽어서
                                                                                                       파일 객체를 갱신하고, 파일에 덮어씌운다. */
            if(editorToolbarScene.getParent().getParent().getId().equals("leftEditor")) {
                String s = textArea.getText();
                FileManager.getFileManager().getFileModelL().updateArrayList(s);
                FileManager.getFileManager().getFileModelL().writeFile();

            }
            else {
                String s = textArea.getText();
                FileManager.getFileManager().getFileModelR().updateArrayList(s);
                FileManager.getFileManager().getFileModelR().writeFile();

            }


        }

        catch(Exception e) { // FileNotFound 등 Exception에 대한 처리.
            // TODO 새 파일을 만들겠냐는 선택지 부여
            // TODO 만들겠다고 하면 파일 생성
            // TODO 만들지 않겠다고 하면 EDIT PANE을 비우고, 파일과의 연결을 끊는다.
        }

    }

    @FXML // 수정 버튼을 클릭했을 때의 동작
    private void  onTBBtnEditClicked(ActionEvent event) {
        if(!textArea.isEditable()) {    // edit 모드로 진입
            textArea.setEditable(true);

            btnFileOpen.setDisable(true);
            btnFileSave.setDisable(true);
            //TODO STATUS 갱신
        }
        else {                          // edit 모드 탈출
            textArea.setEditable(false);

            btnFileOpen.setDisable(false);
            btnFileSave.setDisable(false);
            //TODO STATUS 갱신
        }

    }

    @FXML // 비교 버튼이 클릭되었을 때의 동작
    private void onBtnCompareClicked(ActionEvent event) {

    }

    @FXML // merge-to-right 버튼이 클릭되었을 때의 동작
    private void onBtnMergeToRightClicked(ActionEvent event) {

    }

    @FXML // merge-to-left 버튼이 클릭되었을 때의 동작
    private void onBtnMergeToLeftClicked(ActionEvent event) {

    }

}
