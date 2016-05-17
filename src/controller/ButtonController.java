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
import java.util.ArrayList;

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
    //TODO 나머지 버튼들 활성화/비활성화 조절
    private void  onTBBtnEditClicked(ActionEvent event) {
        if(!textArea.isEditable()) {    // edit 모드로 진입
            textArea.setEditable(true);
            btnFileOpen.setDisable(true);
            btnFileSave.setDisable(true);
            btnMergeLeft.setDisable(true);
            //TODO STATUS 갱신
        }
        else {                          // edit 모드 탈출
            textArea.setEditable(false);

            btnFileOpen.setDisable(false);
            btnFileSave.setDisable(false);

        }

    }

    @FXML // 비교 버튼이 클릭되었을 때의 동작
    private void onBtnCompareClicked(ActionEvent event) {
        ArrayList<String> leftList  = FileManager.getFileManager().getFileModelL().getStringArrayList();
        ArrayList<String> rightList = FileManager.getFileManager().getFileModelR().getStringArrayList();

        try{
            //TODO LCS 알고리즘을 사용하는 메서드
            //TODO 다른 부분에 대한 블럭에서 각 블럭의 LINE 범위를 받아서 공백을 맞춰준다.
            //TODO 블럭에 속하는 line들을 highlight한다.
            //TODO IsCompared를 true로 설정



        } catch(IndexOutOfBoundsException e){
            // 두 파일 중 하나라도 없으면 생길만한 오류
            //TODo 아무 일도 없게 하거나/경고창 띄워서 파일 로드 유도


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

/*

                경고창을 만드는 소스코드 예제
                final Stage newStage = new Stage();
                Group newRoot = new Group();
                Scene scene = new Scene(newRoot, 300, 200);
                newStage.setScene(scene);
                newStage.show();

                VBox vBox = new VBox();

                Label newLabel = new Label();
                newLabel.setText(newStage.toString());

                Button btnClose = new Button("Close");
                btnClose.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        newStage.close();
                    }
                });

                vBox.getChildren().addAll(newLabel, btnClose);
                newRoot.getChildren().add(vBox);


 */