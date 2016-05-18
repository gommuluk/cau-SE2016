package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileManager;
import model.LeftEditorFileNotFoundException;
import model.RightEditorFileNotFoundException;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by SH on 2016-05-18.
 */
public class ControlPaneSceneController {

    @FXML private Button btnCompare, btnMergeLeft, btnMergeRight;
    @FXML private GridPane controlPane;
    private TextArea leftEditor, rightEditor;


    @FXML
    public void initialize(){
        Platform.runLater(()->
            _getEditorReference()
        );
    }

    private void _getEditorReference(){
        Node root = controlPane.getScene().getRoot();
        this.leftEditor = (TextArea)root.lookup("#leftEditor").lookup("#textArea");
        this.rightEditor = (TextArea)root.lookup("#rightEditor").lookup("#textArea");
    }


    @FXML // 비교 버튼이 클릭되었을 때의 동작
    private void onBtnCompareClicked(ActionEvent event) {
        ArrayList<String> leftList;
        ArrayList<String> rightList;
        try{
            //TODO 파일 할당 여부를 보고 있다면 파일을 읽어오는 코드. 버튼 컨트롤러가 해체되어야 함.

            if(FileManager.getFileManager().getFileModelL().getFileExistFlag()) {
                leftList  = FileManager.getFileManager().getFileModelL().getStringArrayList();
            }
            else throw new LeftEditorFileNotFoundException("Left files are not exist");

            if(FileManager.getFileManager().getFileModelR().getFileExistFlag()) {
                rightList = FileManager.getFileManager().getFileModelR().getStringArrayList();
            }
            else throw new RightEditorFileNotFoundException("Right files are not exist");


            //TODO LCS 알고리즘을 사용하는 메서드
            //TODO 다른 부분에 대한 블럭에서 각 블럭의 LINE 범위를 받아서 공백을 맞춰준다.
            //TODO 블럭에 속하는 line들을 highlight한다.
            //TODO IsCompared를 true로 설정



        }

        catch(LeftEditorFileNotFoundException e){
            //TODo 아무 일도 없게 하거나/경고창 띄워서 파일 로드 유도
            Stage s = (Stage) btnCompare.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();                                            //File Chooser을 유저에게 보여준다.
            fileChooser.setTitle("Open Left Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            File selectedFile = fileChooser.showOpenDialog(s);

            //선택된 파일의 Text를 해당되는 Edit Pane에 띄워준다.
            FileManager.getFileManager().getFileModelL().readFile(selectedFile.getPath());
            leftEditor.appendText(FileManager.getFileManager().getFileModelL().toString());
        }
        catch(RightEditorFileNotFoundException e) {
            Stage s = (Stage) btnCompare.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();                                            //File Chooser을 유저에게 보여준다.
            fileChooser.setTitle("Open Right Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            File selectedFile = fileChooser.showOpenDialog(s);

            FileManager.getFileManager().getFileModelR().readFile(selectedFile.getPath());
            rightEditor.appendText(FileManager.getFileManager().getFileModelR().toString());
        }

        catch(Exception e) {
            e.printStackTrace();
            //TODO 알수없는 예외에 대한 처리.
        }

    }

    //TODO Merge
    //어떤 블록이 선택되었는지 알아낼 수 있어야 한다.

    @FXML // merge-to-right 버튼이 클릭되었을 때의 동작
    private void onBtnMergeToRightClicked(ActionEvent event) {
        //TODO 위 컴페어 버튼처럼 왼쪽, 오른쪽 파일을 저장중인 리스트를 받아야 함.
        //TODO 선택된 블럭 존재 여부에 따라 분기
        //TODO 있다면 복사. 없다면 메서드 종료
    }

    @FXML // merge-to-left 버튼이 클릭되었을 때의 동작
    private void onBtnMergeToLeftClicked(ActionEvent event) {
        //TODO 위 컴페어 버튼처럼 왼쪽, 오른쪽 파일을 저장중인 리스트를 받아야 함.
    }


}
