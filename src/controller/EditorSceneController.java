package controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SH on 2016-05-11.
 */
public class EditorSceneController {

    @FXML private TextArea textArea;
    @FXML private ListView<String> listView;
    @FXML private Button btnFileOpen;

    protected List<String> test = new ArrayList<>();
    protected ListProperty<String> listProperty = new SimpleListProperty<>();

    @FXML
    private void initialize(){
        Platform.runLater(()-> {
            _syncEditorScrollWithHighlightList();
            listView.itemsProperty().bind(listProperty);
            test.add("enter");
        });
    }

    @FXML // Editor에서 키보드입력이 있을 때의 동작
    private void onTextAreaKeyPressed(KeyEvent event){
        if( event.getCode().compareTo(KeyCode.ENTER) == 0 ){
            test.add("enter");
            listProperty.set(FXCollections.observableArrayList(test));
        }
    }

    @FXML // 불러오기 버튼을 클릭했을 때의 동작
    private void onTBBtnLoadClicked(ActionEvent event) {
        Stage s = (Stage)btnFileOpen.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(s);
    }

    @FXML // 저장 버튼을 클릭했을 때의 동작
    private void onTBBtnSaveClicked(ActionEvent event) {

    }

    @FXML // 수정 버튼을 클릭했을 때의 동작
    private void onTBBtnEditClicked(ActionEvent event) {

    }


    private void _syncEditorScrollWithHighlightList(){
        // ListView와 TextArea의 스크롤을 동기화시킨다.
        DoubleProperty textAreaScrollTo = textArea.scrollTopProperty();
        ScrollBar listViewScrollTo = (ScrollBar) listView.lookup(".scroll-bar:vertical");

        textAreaScrollTo.bindBidirectional(listViewScrollTo.valueProperty());
    }

}
