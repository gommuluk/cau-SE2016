package controller;

import javafx.application.Platform;
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
        Platform.runLater(this::_initEditorReference);
    }

    private void _initEditorReference() {
        Node root = controlPane.getScene().getRoot();
        this.leftEditor = (HighlightEditorInterface) ((BorderPane) root.lookup("#leftEditor")).getCenter().lookup("#editor");
        this.rightEditor = (HighlightEditorInterface) ((BorderPane) root.lookup("#rightEditor")).getCenter().lookup("#editor");
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
        FileManager.getFileManagerInterface().resetModel(FileManagerInterface.SideOfEditor.Left);
        FileManager.getFileManagerInterface().resetModel(FileManagerInterface.SideOfEditor.Right);
        leftEditor.reset(FileManagerInterface.SideOfEditor.Left);
        rightEditor.reset(FileManagerInterface.SideOfEditor.Right);
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
    private void onMenuBtnAboutClicked() {
       Caution.CautionFactory(Caution.CautionType.AboutNotice, FileManagerInterface.SideOfEditor.Left);
    }

}
