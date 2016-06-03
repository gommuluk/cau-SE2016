package controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import model.FileManager;
import model.FileManagerInterface;

import java.io.*;

/**
 * Created by SH on 2016-05-11.
 */
public class EditorSceneController {

    @FXML private HighlightEditorInterface editor;

    @FXML private Button btnFileSave, btnFileOpen;
    @FXML private ToggleButton btnEdit;

    private Button btnCompare, btnMergeLeft, btnMergeRight;
    private FileManagerInterface.SideOfEditor side = null;
    private BooleanProperty isFocused = new SimpleBooleanProperty(false);

    @FXML
    private void initialize(){
        Platform.runLater(()->{
            _getBtnsReference();

            isFocused.bind(editor.isFocusedProperty());

            btnFileOpen.getScene().getAccelerators().put(
                    new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN),
                    ()->{ if(isFocused.getValue()){ onTBBtnLoadClicked(); System.out.println("focused"); }
                            else System.out.println("not foucsed"); }
            );
        });
    }

    private void _getBtnsReference(){
        Node root = editor.getScene().getRoot();

        this.btnMergeLeft = (Button)root.lookup("#btnMergeLeft");
        this.btnMergeRight = (Button)root.lookup("#btnMergeRight");
        this.btnCompare = (Button)root.lookup("#btnCompare");
        if(editor.getParent().getParent().getId().equals("leftEditor"))
            side = FileManagerInterface.SideOfEditor.Left;
        else
            side = FileManagerInterface.SideOfEditor.Right;
    }

    @FXML // 불러오기 버튼을 클릭했을 때의 동작
    private void onTBBtnLoadClicked() {
        //File chooser code
        try {
            boolean flag = false;
            if(FileManager.getFileManagerInterface().getComparing()) {
                FileManager.getFileManagerInterface().cancelCompare();
                btnMergeLeft.setDisable(true);
                btnMergeRight.setDisable(true);
                btnCompare.setDisable(false);
                flag = true;
            }

            FileManagerInterface.SideOfEditor side, oppositeSide;

            if(editor.getParent().getParent().getId().equals("leftEditor")) {
                side = FileManagerInterface.SideOfEditor.Left;
                oppositeSide = FileManagerInterface.SideOfEditor.Right;
            }
            else {
                side = FileManagerInterface.SideOfEditor.Right;
                oppositeSide = FileManagerInterface.SideOfEditor.Left;
            }

            Node root = editor.getScene().getRoot();

            if(flag) {
                    ((HighlightEditorInterface)(root.lookup("#rightEditor").lookup("#editor"))).update(oppositeSide);
                    ((HighlightEditorInterface)(root.lookup("#leftEditor").lookup("#editor"))).update(side);
            }


            if(FileManager.getFileManagerInterface().getEdited(side)) {
                onTBBtnSaveClicked();


/*              if(FileManager.getFileManagerInterface().getFilePath(side) == null){

                    if(caution.getWindow(side).get() == caution.getSavebtn()){

                        FileExplorer fileSaveExplorer = new FileSaveExplorer();
                        File file = fileSaveExplorer.getDialog(btnFileOpen);
                        if(file == null) return ;
                        FileManager.getFileManagerInterface().saveFile(editor.getText(), file.getAbsolutePath(), side);

                    }
                    else{
                        editor.setText(side, "");
                    }
                    FileManager.getFileManagerInterface().setEdited(side, false);


                }
                else if(FileManager.getFileManagerInterface().getFilePath(side) != null){

                    if(caution.getWindow(side).get() == caution.getSavebtn()){

                        FileManager.getFileManagerInterface().saveFile(editor.getText(), side);
                        FileManager.getFileManagerInterface().setEdited(side, false);
                    }
                    else{
                        //파일이 존재하는데, 저장을 하지 않고 load를 눌렀는데, 거기서도 저장 안 함을 눌렀을 경우.. 어찌되는 거지 ㅠㅠ
                    }


                }
*/

            }

            FileExplorer fileLoadExplorer = new FileLoadExplorer();
            File selectedFile = fileLoadExplorer.getDialog(btnFileOpen, side);

            //선택된 파일의 Text를 해당되는 Edit Pane에 띄워준다.
            if(selectedFile == null) return ;
            FileManager.getFileManagerInterface().loadFile(selectedFile.getPath(), side);
            editor.setText(side, FileManager.getFileManagerInterface().getString(side));

        }
        catch(UnsupportedEncodingException e) {                                                                        // TODO Exception 별 처리 필요.
            e.printStackTrace();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML // 저장 버튼을 클릭했을 때의 동작
    private void onTBBtnSaveClicked() { //UnsupportedEncoingException 추가
        if(FileManager.getFileManagerInterface().getEdited(side)) {
            if (Caution.CautionFactory(Caution.CautionType.SaveChoice, side)) {
                try {
                    FileManager.getFileManagerInterface().saveFile(editor.getText(), side);

                } catch (FileNotFoundException e) {
                    FileExplorer fileSaveExplorer = new FileSaveExplorer();
                    File file = fileSaveExplorer.getDialog(btnFileSave, side);
                    if (file == null) return;
                    try {
                        FileManager.getFileManagerInterface().saveFile(editor.getText(), file.getAbsolutePath(), side);
                    } catch (FileNotFoundException e1) {
                        Caution.CautionFactory(Caution.CautionType.SaveNotice, side);
                    }
                }
            }
        }
    }


    @FXML // 수정 버튼을 클릭했을 때의 동작
    private void onTBBtnEditClicked() {
        boolean flag = false;

        Node root = editor.getScene().getRoot();
        FileManagerInterface.SideOfEditor oppositeSide;

        if(FileManager.getFileManagerInterface().getComparing()) {
            FileManager.getFileManagerInterface().cancelCompare();
            flag = true;
        }


        if(side == FileManagerInterface.SideOfEditor.Left) {
            oppositeSide = FileManagerInterface.SideOfEditor.Right;
        }
        else {
            oppositeSide = FileManagerInterface.SideOfEditor.Left;
        }


        if(!editor.isEditable()) {    // edit 모드로 진입
            editor.      setEditable(true);
            editor.      setEditMode(true);
            btnFileOpen.  setDisable(true);
            btnFileSave.  setDisable(true);
            btnCompare.   setDisable(true);
            btnMergeLeft. setDisable(true);
            btnMergeRight.setDisable(true);

            if(flag) {
                if(oppositeSide == FileManagerInterface.SideOfEditor.Right)
                    ((HighlightEditorInterface)(root.lookup("#rightEditor").lookup("#editor"))).update(oppositeSide);
                else
                    ((HighlightEditorInterface)(root.lookup("#leftEditor").lookup("#editor"))).update(oppositeSide);
            }

        }
        else {                          // edit 모드 탈출

            editor.      setEditable(false);
            editor.      setEditMode(false);
            btnFileOpen.  setDisable(false);
            btnFileSave.  setDisable(false);

            if(side == FileManagerInterface.SideOfEditor.Right) {
                if (!((ToggleButton)root.lookup("#leftEditor").lookup("#btnEdit")).isSelected()) {
                    btnCompare.setDisable(false);
                }
            }
            else {
                if (!((ToggleButton)root.lookup("#rightEditor").lookup("#btnEdit")).isSelected()) {

                    btnCompare.setDisable(false);
                }
            }

            editor.update(side);
        }

        FileManager.getFileManagerInterface().setEdited(side);

    }

    //Method for testing
    public void useSaveActionMethod() {
        onTBBtnSaveClicked();
    }

    public void testHelper(HighlightEditorInterface editor, Button saveBtn, Button openBtn, ToggleButton editBtn) {

    }
}
