package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

/**
 * Created by SH on 2016-05-18.
 */
public class ControlPaneSceneController {

    @FXML private Button btnCompare, btnMergeLeft, btnMergeRight;
    @FXML private GridPane controlPane;

    private TextArea textArea;

    @FXML
    public void initialize(){
        Platform.runLater(()->{
            //this.textArea = controlPane.getParent()
        });
    }


    @FXML // 비교 버튼이 클릭되었을 때의 동작
    private void onBtnCompareClicked(ActionEvent event) {
        ArrayList<String> leftList;
        ArrayList<String> rightList;
        try{
            //TODO 파일 할당 여부를 보고 있다면 파일을 읽어오는 코드. 버튼 컨트롤러가 해체되어야 함.
            /*
            if(leftEditor.isFileExist) {
                leftList  = FileManager.getFileManager().getFileModelL().getStringArrayList();
            }
            else throw new LeftEditorFileNotFoundException("Left files are not exist");

            if(rightEditor.isFileExist) {
                rightList = FileManager.getFileManager().getFileModelR().getStringArrayList();
            }
            else throw new RightEditorFileNotFoundException("Right files are not exist");
            */

            //TODO LCS 알고리즘을 사용하는 메서드
            //TODO 다른 부분에 대한 블럭에서 각 블럭의 LINE 범위를 받아서 공백을 맞춰준다.
            //TODO 블럭에 속하는 line들을 highlight한다.
            //TODO IsCompared를 true로 설정



        }
        /*
        catch(LeftEditorFileNotFoundException e){
            // 두 파일 중 하나라도 없으면 생길만한 오류

            //TODo 아무 일도 없게 하거나/경고창 띄워서 파일 로드 유도


        }
        catch(RightEditorFileNotFoundException e) {

        }
        */
        catch(Exception e) {

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
