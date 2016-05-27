package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.FileManager;
import model.FileManagerInterface;
import model.LeftEditorFileCanNotCompareException;
import model.RightEditorFileCanNotCompareException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by SH on 2016-05-18.
 */
public class ControlPaneSceneController {

    @FXML private HighlightEditorInterface editor;

    @FXML private Button btnCompare, btnMergeLeft, btnMergeRight;
    @FXML private GridPane controlPane;
    private HighlightEditorInterface leftEditor, rightEditor;



    @FXML
    public void initialize(){
        Platform.runLater(this::_getEditorReference);
    }

    private void _getEditorReference(){
        Node root = controlPane.getScene().getRoot();
        this.leftEditor  = (HighlightEditorInterface)((BorderPane)root.lookup("#leftEditor")).getCenter().lookup("#editor");
        this.rightEditor = (HighlightEditorInterface)((BorderPane)root.lookup("#rightEditor")).getCenter().lookup("#editor");
    }


    @FXML // 비교 버튼이 클릭되었을 때의 동작
    private void onBtnCompareClicked(ActionEvent event) {
        ArrayList<String> leftList;
        ArrayList<String> rightList;
        Caution caution = new Caution();
        try {
                FileManager.getFileManagerInterface().setCompare(); // 양쪽 파일 없는거 감지 가능
                btnMergeLeft.setDisable(false);
                btnMergeRight.setDisable(false);
        }
        catch (LeftEditorFileCanNotCompareException e) {
            if(caution.getSaveWindow(FileManagerInterface.SideOfEditor.Left).get() == caution.getSavebtn()) {
                try {
                    FileManager.getFileManagerInterface().saveFile(leftEditor.getText(), FileManagerInterface.SideOfEditor.Left);
                } catch (FileNotFoundException e1) {
                    FileExplorer fileSaveExplorer = new FileSaveExplorer();
                    File file = fileSaveExplorer.getDialog(btnCompare);
                    if (file == null) return;
                    try {
                        FileManager.getFileManagerInterface().saveFile(leftEditor.getText(), file.getAbsolutePath(), FileManagerInterface.SideOfEditor.Left);
                    } catch (FileNotFoundException e2) {
                        caution.noticeSaveWindow(FileManagerInterface.SideOfEditor.Left);
                    }
                }
            }
        }
        catch (RightEditorFileCanNotCompareException e) {
            if(caution.getSaveWindow(FileManagerInterface.SideOfEditor.Right).get() == caution.getSavebtn()) {
                try {
                    FileManager.getFileManagerInterface().saveFile(rightEditor.getText(), FileManagerInterface.SideOfEditor.Right);
                } catch (FileNotFoundException e1) {
                    FileExplorer fileSaveExplorer = new FileSaveExplorer();
                    File file = fileSaveExplorer.getDialog(btnCompare);
                    if (file == null) return;
                    try {
                        FileManager.getFileManagerInterface().saveFile(rightEditor.getText(), file.getAbsolutePath(), FileManagerInterface.SideOfEditor.Right);
                    } catch (FileNotFoundException e2) {
                        caution.noticeSaveWindow(FileManagerInterface.SideOfEditor.Left);
                    }
                }
            }
        }
    }

    //TODO Merge
    //어떤 블록이 선택되었는지 알아낼 수 있어야 한다.

    /*
    * isCompare이 true인 상태
    * merge하면 compare가 꺼진다.
    * edit랑 merge가 될 경우는 isCompared를 false라고 .. 그 때의 버튼 비활성화 여부 체크해주기
    * isCompare == true면,,, EditPane에서 블럭 선택 가능-선택된곳은 다른색으로 표시..
    * Merge버튼 활성화 조건 1)양 EditPane에 FileLoad되어있음
    * */
    @FXML // merge-to-right 버튼이 클릭되었을 때의 동작
    private void onBtnMergeToRightClicked(ActionEvent event) {


        // 테스트 중

        if(!(FileManager.getFileManagerInterface().merge(FileManagerInterface.SideOfEditor.Right))){
            Caution caution = new Caution();
            caution.noticeMergeFailure();
        }
        else{
            FileManager.getFileManagerInterface().cancelCompare();
            leftEditor.update(FileManagerInterface.SideOfEditor.Left);
            rightEditor.update(FileManagerInterface.SideOfEditor.Right);
        }

    }

    @FXML // merge-to-left 버튼이 클릭되었을 때의 동작
    private void onBtnMergeToLeftClicked(ActionEvent event) {

        if(!(FileManager.getFileManagerInterface().merge(FileManagerInterface.SideOfEditor.Left))){
            Caution caution = new Caution();
            caution.noticeMergeFailure();
        }
        else{
            FileManager.getFileManagerInterface().cancelCompare();
            leftEditor.update(FileManagerInterface.SideOfEditor.Left);
            rightEditor.update(FileManagerInterface.SideOfEditor.Right);
        }


    }

    @FXML
    private void onMenuBtnResetClicked(ActionEvent event) {

    }

    @FXML
    private void onMenuBtnCloseClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onMenuBtnCopyToRightClicked(ActionEvent event) {
        onBtnMergeToRightClicked(event);
    }

    @FXML
    private void onMenuBtnCopyToLeftClicked(ActionEvent event) {
        onBtnMergeToLeftClicked(event);
    }

    @FXML
    private void onMenuBtnCopyToCompareClicked(ActionEvent event) {
        onBtnCompareClicked(event);
    }

    @FXML
    private void onMenuBtnHelpClicked(ActionEvent event) {
    }

    @FXML
    private void onMenuBtnAboutClicked(ActionEvent event) {
    }

}
