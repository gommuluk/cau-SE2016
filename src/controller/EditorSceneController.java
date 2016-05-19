package controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileManager;

import java.io.*;
import java.util.Optional;

/**
 * Created by SH on 2016-05-11.
 */
public class EditorSceneController {

    @FXML private TextArea textArea;
    @FXML private ListView<String> listView;
    @FXML private Button btnFileSave;
    @FXML private Button btnFileOpen;

    private Button btnCompare, btnMergeLeft, btnMergeRight;


    public EditorSceneController(){
        FileManager.getFileManager().getFileModelL().getList().addAll(0, 1, 2, 4, 10, 20);
    }

    @FXML
    private void initialize(){

        Platform.runLater(()-> {

            _getBtnsReference();
            _syncEditorScrollWithHighlightList();
            _syncEditorContentWithHighlightList();
            _enableHighLights();

            listView.scrollTo(0);
        });
    }

    private void _getBtnsReference(){

        Node root = textArea.getScene().getRoot();

        this.btnMergeLeft = (Button)root.lookup("#btnMergeLeft");
        this.btnMergeRight = (Button)root.lookup("#btnMergeRight");
        this.btnCompare = (Button)root.lookup("#btnCompare");

    }

    private void _syncEditorScrollWithHighlightList(){
        // ListView와 TextArea의 스크롤을 동기화시킨다.
        ScrollBar textAreaScrollTo = (ScrollBar) textArea.lookup(".scroll-bar:vertical");
        ScrollBar listViewScrollTo = (ScrollBar) listView.lookup(".scroll-bar:vertical");
        System.out.println(textAreaScrollTo.getValue());
        System.out.println(listViewScrollTo.getValue());

        textAreaScrollTo.valueProperty().bindBidirectional(listViewScrollTo.valueProperty());
        listViewScrollTo.valueProperty().bindBidirectional(textAreaScrollTo.valueProperty());
    }
    private void _syncEditorContentWithHighlightList(){
        if( textArea.getParent().getParent().getId().contentEquals("leftEditor")) {
            listView.itemsProperty().bind(FileManager.getFileManager().getFileModelL().getListProperty());
        } else {
            listView.itemsProperty().bind(FileManager.getFileManager().getFileModelR().getListProperty());
        }
    }
    private void _enableHighLights(){

        if( listView != null ) {

            listView.setCellFactory(list -> new ListCell<String>() {
                BooleanBinding invalid ;
                {
                    if( textArea.getParent().getParent().getId().contentEquals("leftEditor") ) {
                        invalid = Bindings.createBooleanBinding(
                                () -> FileManager.getFileManager().getFileModelL().getList().contains(getIndex()),
                                indexProperty(), itemProperty(), FileManager.getFileManager().getFileModelL().getList()
                        );

                    } else {
                        invalid = Bindings.createBooleanBinding(
                                () -> FileManager.getFileManager().getFileModelR().getList().contains(getIndex()),
                                indexProperty(), itemProperty(), FileManager.getFileManager().getFileModelR().getList()
                        );
                    }

                    invalid.addListener((obs, wasInvalid, isNowInvalid) -> {
                        if (isNowInvalid) {
                            setStyle("-fx-background-color:yellowgreen;");
                        } else {
                            setStyle("");
                        }
                    });

                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            });
        }

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
            System.out.println(textArea.getParent().getParent().getId());
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
    private void onTBBtnSaveClicked(ActionEvent event) throws FileNotFoundException, UnsupportedEncodingException { //UnsupportedEncoingExceoption 추가

        //TODO isEdited가 true인 경우, 버튼 활성화
        try {
                                                                                                    /* 저장 버튼이 속한 패널을 판단하고,
                                                                                                       해당 패널의 Text를 읽어서
                                                                                                       파일 객체를 갱신하고, 파일에 덮어씌운다. */
            if (textArea.getParent().getParent().getId().equals("leftEditor")) {
                //TODO isEdited 상태로 저장 여부 결정
                String s = textArea.getText();
                FileManager.getFileManager().getFileModelL().updateArrayList(s);
                FileManager.getFileManager().getFileModelL().writeFile();

            } else {
                String s = textArea.getText();
                //TODO isEdited 상태로 저장 여부 결정
                FileManager.getFileManager().getFileModelR().updateArrayList(s);
                FileManager.getFileManager().getFileModelR().writeFile();

            }
            //TODO 성공시 isEdited를 false로 갱신해야 하는데..\
            //TODO 파일을 실제로 저장해보면 개행 처리가 안 됨 ㅜㅠ
            //TODO COMPARE버튼이 눌러져있다면, 취소

        } catch (Exception e) { // FileNotFound 등 Exception에 대한 처리.
            // TODO 새 파일을 만들겠냐는 선택지 부여
            // TODO 만들겠다고 하면 파일 생성
            // TODO 만들지 않겠다고 하면 EDIT PANE을 비우고, 파일과의 연결을 끊는다.


            //로드가 되지 않은 채로 저장을 눌렀다든가.
            //edit만 하고 저장을 눌렀다든가?

            // TODO 경고창 매서드로 따로 만들기

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Simple Merge - 소공 2팀");
            alert.setHeaderText(null);
            alert.setContentText("새로운 파일로 저장하시겠습니까?");

            ButtonType buttonTypeSave = new ButtonType("저장");
            ButtonType buttonTypeNotSave = new ButtonType("저장 안 함");
            ButtonType buttonTypeCancel = new ButtonType("취소");

            alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeNotSave, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeSave){
                // TODO 새로운 파일 저장 시스템을 띄운다
                // TODO 새 경로에 새 이름으로 저장

                Stage s = (Stage) btnFileSave.getScene().getWindow();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                        new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                        new FileChooser.ExtensionFilter("All Files", "*.*"));

                File file = fileChooser.showSaveDialog(s);

                file = new File(file.getAbsolutePath());
                FileOutputStream fileOut = new FileOutputStream(file);

                if(textArea.getParent().getParent().getId().equals("leftEditor")) {
                    FileManager.getFileManager().getFileModelL().readFile(file.getPath());
                }
                else {
                    FileManager.getFileManager().getFileModelR().readFile(file.getPath());
                }



            } else if (result.get() == buttonTypeNotSave) {
                // TODO 진행을 취소..
            } else {
                //취소->아무것도 하지 않음.
            }

            // TODO 성공하면, isEdited를 false로 바꾼다.
            // TODO 실패하면, 실패했다는 알림을 표시하고 진행 중지


        }
    }

    @FXML // 수정 버튼을 클릭했을 때의 동작
    //TODO 나머지 버튼들 활성화/비활성화 조절
    private void  onTBBtnEditClicked(ActionEvent event) {
        if(!textArea.isEditable()) {    // edit 모드로 진입
            textArea.    setEditable(true);
            btnFileOpen.  setDisable(true);
            btnFileSave.  setDisable(true);
            btnCompare.   setDisable(true);
            btnMergeLeft. setDisable(true);
            btnMergeRight.setDisable(true);
            //TODO STATUS 갱신 editing으로
        }
        else {                          // edit 모드 탈출
            textArea.    setEditable(false);
            btnFileOpen.  setDisable(false);
            btnFileSave.  setDisable(false);
            btnCompare.   setDisable(false);
            btnMergeLeft. setDisable(false);
            btnMergeRight.setDisable(false);
        }

    }


}
