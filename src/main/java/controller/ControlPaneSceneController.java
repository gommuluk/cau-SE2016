package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.FileManager;
import model.FileManagerInterface;
import model.LeftEditorFileCanNotCompareException;
import model.RightEditorFileCanNotCompareException;

import java.io.File;
import java.io.FileNotFoundException;

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
        Platform.runLater(()->{
            _initEditorReference();
        });
    }

    private void _initEditorReference(){
        Node root = controlPane.getScene().getRoot();
        this.leftEditor  = (HighlightEditorInterface)((BorderPane)root.lookup("#leftEditor")).getCenter().lookup("#editor");
        this.rightEditor = (HighlightEditorInterface)((BorderPane)root.lookup("#rightEditor")).getCenter().lookup("#editor");
    }

    private void _syncListViewScrolls(){
        ScrollBar leftListViewVScroll = (ScrollBar) leftEditor.getHighlightListView().lookup(".scroll-bar:vertical");
        ScrollBar rightListViewVScroll = (ScrollBar) rightEditor.getHighlightListView().lookup(".scroll-bar:vertical");
        leftListViewVScroll.valueProperty().bindBidirectional(rightListViewVScroll.valueProperty());
    }


    @FXML // 비교 버튼이 클릭되었을 때의 동작
    private void onBtnCompareClicked() {
        if(leftEditor.isEditMode() || rightEditor.isEditMode()) {
            Caution.CautionFactory(Caution.CautionType.CompareNotice, FileManagerInterface.SideOfEditor.Left);
        }
        else {
            try {
                FileManager.getFileManagerInterface().setCompare(); // 양쪽 파일 없는거 감지 가능
                btnMergeLeft.setDisable(false);
                btnMergeRight.setDisable(false);
                btnCompare.setDisable(true);

            } catch (LeftEditorFileCanNotCompareException e) {
                if (Caution.CautionFactory(Caution.CautionType.SaveChoice, FileManagerInterface.SideOfEditor.Left)) {
                    try {
                        FileManager.getFileManagerInterface().saveFile(leftEditor.getText(), FileManagerInterface.SideOfEditor.Left);
                    } catch (FileNotFoundException e1) {
                        FileExplorer fileSaveExplorer = new FileSaveExplorer();
                        File file = fileSaveExplorer.getDialog(btnCompare, FileManagerInterface.SideOfEditor.Left);
                        if (file == null) return;
                        try {
                            FileManager.getFileManagerInterface().saveFile(leftEditor.getText(), file.getAbsolutePath(), FileManagerInterface.SideOfEditor.Left);
                        } catch (FileNotFoundException e2) {
                            Caution.CautionFactory(Caution.CautionType.SaveNotice, FileManagerInterface.SideOfEditor.Left);
                        }
                    } finally {
                        onBtnCompareClicked();
                    }
                }
            } catch (RightEditorFileCanNotCompareException e) {
                if (Caution.CautionFactory(Caution.CautionType.SaveChoice, FileManagerInterface.SideOfEditor.Right)) {
                    try {
                        FileManager.getFileManagerInterface().saveFile(rightEditor.getText(), FileManagerInterface.SideOfEditor.Right);
                    } catch (FileNotFoundException e1) {
                        FileExplorer fileSaveExplorer = new FileSaveExplorer();
                        File file = fileSaveExplorer.getDialog(btnCompare, FileManagerInterface.SideOfEditor.Right);
                        if (file == null) return;
                        try {
                            FileManager.getFileManagerInterface().saveFile(rightEditor.getText(), file.getAbsolutePath(), FileManagerInterface.SideOfEditor.Right);
                        } catch (FileNotFoundException e2) {
                            Caution.CautionFactory(Caution.CautionType.SaveChoice, FileManagerInterface.SideOfEditor.Right);
                        }
                    } finally {
                        onBtnCompareClicked();
                    }
                }
            }
        }
    }

    //어떤 블록이 선택되었는지 알아낼 수 있어야 한다.

    /*
    * isCompare이 true인 상태
    * merge하면 compare가 꺼진다.
    * edit랑 merge가 될 경우는 isCompared를 false라고 .. 그 때의 버튼 비활성화 여부 체크해주기
    * isCompare == true면,,, EditPane에서 블럭 선택 가능-선택된곳은 다른색으로 표시..
    * Merge버튼 활성화 조건 1)양 EditPane에 FileLoad되어있음
    * */
    @FXML // merge-to-right 버튼이 클릭되었을 때의 동작
    private void onBtnMergeToRightClicked() {

        // 테스트 중

        if(!(FileManager.getFileManagerInterface().merge(FileManagerInterface.SideOfEditor.Right))){
            Caution.CautionFactory(Caution.CautionType.MergeNotice, FileManagerInterface.SideOfEditor.Right);
        }
        else{
            FileManager.getFileManagerInterface().cancelCompare();
            btnMergeRight.setDisable(true);
            btnMergeLeft.setDisable(true);
            btnCompare.setDisable(false);
            rightEditor.setText(FileManagerInterface.SideOfEditor.Right, FileManager.getFileManagerInterface().getString(FileManagerInterface.SideOfEditor.Right));

        }

    }

    @FXML // merge-to-left 버튼이 클릭되었을 때의 동작
    private void onBtnMergeToLeftClicked() {

        if(!(FileManager.getFileManagerInterface().merge(FileManagerInterface.SideOfEditor.Left))){
            Caution.CautionFactory(Caution.CautionType.MergeNotice, FileManagerInterface.SideOfEditor.Left);

        }
        else {
            FileManager.getFileManagerInterface().cancelCompare();
            btnMergeRight.setDisable(true);
            btnMergeLeft.setDisable(true);
            btnCompare.setDisable(false);
            leftEditor.setText(FileManagerInterface.SideOfEditor.Left, FileManager.getFileManagerInterface().getString(FileManagerInterface.SideOfEditor.Left));
        }


    }

    @FXML
    private void onMenuBtnResetClicked() {

    }

    @FXML
    private void onMenuBtnCloseClicked() {
        System.exit(0);
    }

    @FXML
    private void onMenuBtnCopyToRightClicked() {
        onBtnMergeToRightClicked();
    }

    @FXML
    private void onMenuBtnCopyToLeftClicked() {
        onBtnMergeToLeftClicked();
    }

    @FXML
    private void onMenuBtnCopyToCompareClicked() {
        onBtnCompareClicked();
    }

    @FXML
    private void onMenuBtnHelpClicked() {
    }

    @FXML
    private void onMenuBtnAboutClicked() {
       Caution.CautionFactory(Caution.CautionType.AboutNotice, FileManagerInterface.SideOfEditor.Left);
    }

    public Button getBtnCompare() {
        return this.btnCompare;
    }

    public Button getBtnMergeLeft() {
        return this.btnMergeLeft;
    }

    public Button getBtnMergeRight() {
        return this.btnMergeRight;
    }
}
