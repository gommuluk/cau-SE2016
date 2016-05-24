package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.FileManager;
import model.FileManagerInterface;

import java.io.File;
import java.io.*;

/**
 * Created by SH on 2016-05-11.
 */
public class EditorSceneController {

    @FXML private HighlightEditorInterface editor;

    @FXML private Button btnFileSave;
    @FXML private Button btnFileOpen;
    @FXML private Label filePath;

    private Button btnCompare, btnMergeLeft, btnMergeRight;



    @FXML
    private void initialize(){

        Platform.runLater(()-> {
            _getBtnsReference();
        });
    }

    private void _getBtnsReference(){

        Node root = editor.getScene().getRoot();

        this.btnMergeLeft = (Button)root.lookup("#btnMergeLeft");
        this.btnMergeRight = (Button)root.lookup("#btnMergeRight");
        this.btnCompare = (Button)root.lookup("#btnCompare");

    }

    @FXML // Editor에서 키보드입력이 있을 때의 동작
    private void onTextAreaKeyPressed(KeyEvent event){
        if( event.getCode().compareTo(KeyCode.ENTER) == 0 ){
            System.out.println("enter");
        }
    }

    @FXML // 불러오기 버튼을 클릭했을 때의 동작
    private void onTBBtnLoadClicked(ActionEvent event) {
        //File chooser code
        try {
            FileManagerInterface.SideOfEditor side;

            if( editor.getParent().getParent().getId().contentEquals("leftEditor") ) {
                side = FileManagerInterface.SideOfEditor.Left;
            } else {
                side = FileManagerInterface.SideOfEditor.Right;
            }



            if(FileManager.getFileManagerInterface().getEdited(side)) {

                SavingCaution caution = new SavingCaution();
                if(FileManager.getFileManagerInterface().getFilePath(side) == null){

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
                        //TODO 파일이 존재하는데, 저장을 하지 않고 load를 눌렀는데, 거기서도 저장 안 함을 눌렀을 경우
                    }


                }
            }


            Stage s = (Stage) btnFileOpen.getScene().getWindow();

            FileExplorer fileLoadExplorer = new FileLoadExplorer();
            File selectedFile = fileLoadExplorer.getDialog(btnFileOpen);

            //선택된 파일의 Text를 해당되는 Edit Pane에 띄워준다.
            if(selectedFile == null) return ;
            FileManager.getFileManagerInterface().loadFile(selectedFile.getPath(), side);
            editor.setText(side, FileManager.getFileManagerInterface().getString(side));

            filePath.setText(selectedFile.getPath());

        }
        catch(UnsupportedEncodingException e) {                                                                        // TODO Exception 별 처리 필요.
            e.printStackTrace();
            System.out.println("Exception.");
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML // 저장 버튼을 클릭했을 때의 동작
    private void onTBBtnSaveClicked(ActionEvent event) throws IOException { //UnsupportedEncoingException 추가
        FileManagerInterface.SideOfEditor side;
        if(editor.getParent().getParent().getId().equals("leftEditor"))
            side = FileManagerInterface.SideOfEditor.Left;
        else
            side = FileManagerInterface.SideOfEditor.Right;

        try {
            String s = editor.getText();
            FileManager.getFileManagerInterface().saveFile(s, side);


            FileManager.getFileManagerInterface().cancelCompare();

        } catch (Exception e) { // FileNotFound 등 Exception에 대한 처리.
            //로드가 되지 않은 채로 저장을 눌렀다든가.
            //edit만 하고 저장을 눌렀다든가?


            SavingCaution caution = new SavingCaution();

            if (caution.getWindow(side).get() == caution.getSavebtn()){
                FileExplorer fileSaveExplorer = new FileSaveExplorer();
                File file = fileSaveExplorer.getDialog(btnFileSave);
                if(file == null) return ;
                if(editor.getParent().getParent().getParent().getId().equals("leftEditor"))
                    FileManager.getFileManagerInterface().saveFile(editor.getText(), file.getAbsolutePath(), FileManagerInterface.SideOfEditor.Left);
                else
                    FileManager.getFileManagerInterface().saveFile(editor.getText(), file.getAbsolutePath(), FileManagerInterface.SideOfEditor.Right);
            }

            // TODO 실패하면, 실패했다는 알림을 표시하고 진행 중지


        }
    }

    @FXML // 수정 버튼을 클릭했을 때의 동작
    //TODO 나머지 버튼들 활성화/비활성화 조절
    private void onTBBtnEditClicked(ActionEvent event) {
        FileManagerInterface.SideOfEditor side;
        if(editor.getParent().getParent().getId().equals("leftEditor"))
            side = FileManagerInterface.SideOfEditor.Left;
        else
            side = FileManagerInterface.SideOfEditor.Right;


        if(FileManager.getFileManagerInterface().getComparing())
            FileManager.getFileManagerInterface().cancelCompare();
        if(!editor.isEditable()) {    // edit 모드로 진입
            editor.      setEditable(true);
            editor.      setEditMode(true);
            btnFileOpen.  setDisable(true);
            btnFileSave.  setDisable(true);
            btnCompare.   setDisable(true);
            btnMergeLeft. setDisable(true);
            btnMergeRight.setDisable(true);


        }
        else {                          // edit 모드 탈출
            Node root = editor.getScene().getRoot();

            editor.      setEditable(false);
            editor.      setEditMode(false);
            btnFileOpen.  setDisable(false);
            btnFileSave.  setDisable(false);
            //TODO 버튼 DISABLE 조건..
            //다른 패널의 에디트 버튼 상태 조회 필요

            if(side == FileManagerInterface.SideOfEditor.Right) {
                if (!((ToggleButton)root.lookup("#leftEditor").lookup("#btnEdit")).isSelected()) {
                    btnCompare.setDisable(false);
                    btnMergeLeft.setDisable(false);
                    btnMergeRight.setDisable(false);
                }
            }
            else {
                if (!((ToggleButton)root.lookup("#rightEditor").lookup("#btnEdit")).isSelected()) {

                    btnCompare.setDisable(false);
                    btnMergeLeft.setDisable(false);
                    btnMergeRight.setDisable(false);
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
