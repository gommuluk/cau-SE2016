package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileManager;
import model.FileModel;

import javax.naming.NoPermissionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by SH on 2016-05-11.
 */
public class EditorSceneController {

    @FXML private TextArea textArea;
    @FXML private Button btnFileOpen;

    @FXML
    private void initialize(){

        // ListView와 TextArea의 스크롤을 동기화시킨다.
        DoubleProperty textAreaScrollTo = textArea.scrollTopProperty();

    }

    @FXML // 불러오기 버튼을 클릭했을 때의 동작
    private void onTBBtnLoadClicked(ActionEvent event) {
        //File chooser code
        try {
            Stage s = (Stage) btnFileOpen.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            File selectedFile = fileChooser.showOpenDialog(s);

            //Show text in Edit panel
            //TODO 불러오기 버튼이 속한 에딧패널이 좌측인지 우측인지 인지가능한 코드가 필요.
            FileManager.getFileManager().GetFileModelL().readFile(selectedFile.getPath());

            ArrayList<String> strings = FileManager.getFileManager().GetFileModelL().getStringArrayList();

            for (String temp : strings) textArea.appendText(temp);
        }
        catch(Exception e) {

        }
    }

    @FXML // 저장 버튼을 클릭했을 때의 동작
    private void onTBBtnSaveClicked(ActionEvent event) {
        try {
            FileManager.getFileManager().GetFileModelL().writeFile(textArea.getText());

        }
        catch(Exception e) { // FileNotFound 등 다양한 Exception에 대한 처리.
            // TODO 새 파일을 만들겠냐는 선택지 부여
            // TODO 만들겠다고 하면 파일 생성
            // TODO 만들지 않겠다고 하면 EDIT PANE을 비우고, 파일과의 연결을 끊는다.
        }
    }

    @FXML // 수정 버튼을 클릭했을 때의 동작
    private void onTBBtnEditClicked(ActionEvent event) {
        if(!textArea.isEditable()) {    // edit 모드로 진입
            textArea.setEditable(true);
            //TODO STATUS 갱신
            //TODO 버튼 비활성화
        }
        else {                          // edit 모드 탈출
            textArea.setEditable(false);
            //TODO STATUS 갱신
            //TODO 버튼 활성화
        }
    }

}
