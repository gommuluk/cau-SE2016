package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.FileManager;
import model.FileManagerInterface;

import java.io.*;

/**
 * Created by SH on 2016-05-11.
 */
public class EditorSceneController {

    @FXML private HighlightEditorInterface editor;

    @FXML private Button btnFileSave;
    @FXML private Button btnFileOpen;

    private Button btnCompare, btnMergeLeft, btnMergeRight;

    @FXML
    private void initialize(){
        Platform.runLater(this::_getBtnsReference);
    }

    private void _getBtnsReference(){
        Node root = editor.getScene().getRoot();

        this.btnMergeLeft = (Button)root.lookup("#btnMergeLeft");
        this.btnMergeRight = (Button)root.lookup("#btnMergeRight");
        this.btnCompare = (Button)root.lookup("#btnCompare");
    }

    @FXML // Editor에서 키보드입력이 있을 때의 동작
    private void onTextAreaKeyPressed(KeyEvent event){

    }

    @FXML // 불러오기 버튼을 클릭했을 때의 동작
    private void onTBBtnLoadClicked(ActionEvent event) {
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
                onTBBtnSaveClicked(event);


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
                        //TODO 파일이 존재하는데, 저장을 하지 않고 load를 눌렀는데, 거기서도 저장 안 함을 눌렀을 경우.. 어찌되는 거지 ㅠㅠ
                    }


                }
*/

            }

            FileExplorer fileLoadExplorer = new FileLoadExplorer();
            File selectedFile = fileLoadExplorer.getDialog(btnFileOpen);

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
    private void onTBBtnSaveClicked(ActionEvent event) { //UnsupportedEncoingException 추가
        FileManagerInterface.SideOfEditor side;
        if(editor.getParent().getParent().getId().equals("leftEditor"))
            side = FileManagerInterface.SideOfEditor.Left;
        else
            side = FileManagerInterface.SideOfEditor.Right;
        Caution caution = new Caution();
        if(caution.getSaveWindow(side).get() == caution.getSavebtn()) {
            try {
                FileManager.getFileManagerInterface().saveFile(editor.getText(), side);

            } catch (FileNotFoundException e) {
                FileExplorer fileSaveExplorer = new FileSaveExplorer();
                File file = fileSaveExplorer.getDialog(btnFileSave);
                if (file == null) return;
                try {
                    FileManager.getFileManagerInterface().saveFile(editor.getText(), file.getAbsolutePath(), side);
                } catch (FileNotFoundException e1) {
                    caution.noticeSaveWindow(FileManagerInterface.SideOfEditor.Left);
                }
            }
        }

    }


    @FXML // 수정 버튼을 클릭했을 때의 동작
    //TODO 나머지 버튼들 활성화/비활성화 조절
    private void onTBBtnEditClicked(ActionEvent event) {
        boolean flag = false;

        Node root = editor.getScene().getRoot();
        FileManagerInterface.SideOfEditor side, oppositeSide;

        if(FileManager.getFileManagerInterface().getComparing()) {
            FileManager.getFileManagerInterface().cancelCompare();
            flag = true;
        }


        if(editor.getParent().getParent().getId().equals("leftEditor")) {
            side = FileManagerInterface.SideOfEditor.Left;
            oppositeSide = FileManagerInterface.SideOfEditor.Right;
        }
        else {
            side = FileManagerInterface.SideOfEditor.Right;
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

        FileManager.getFileManagerInterface().setEdited(side, true);

    }

    //Method for testing
    public void useSaveActionMethod() throws IOException {
        onTBBtnSaveClicked(new ActionEvent());
    }
}
