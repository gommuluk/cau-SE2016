package controller;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SH on 2016-05-11.
 */
public class EditorSceneController {

    @FXML private TextArea textArea;
    @FXML private ListView<String> listView;

    protected List<String> test = new ArrayList<>();
    protected ListProperty<String> listProperty = new SimpleListProperty<>();

    @FXML
    private void initialize(){
        Platform.runLater(()-> {
            _syncEditorScrollWithHighlightList();
            listView.itemsProperty().bind(listProperty);
        });
    }

    @FXML // Editor에서 키보드입력이 있을 때의 동작
    private void onTextAreaKeyPressed(KeyEvent event){
        if( event.getCode().compareTo(KeyCode.ENTER) == 0 ){
            test.add("enter");
            listProperty.set(FXCollections.observableArrayList(test));
        }
    }

    private void _syncEditorScrollWithHighlightList(){
        // ListView와 TextArea의 스크롤을 동기화시킨다.
        DoubleProperty textAreaScrollTo = textArea.scrollTopProperty();
        ScrollBar listViewScrollTo = (ScrollBar) listView.lookup(".scroll-bar:vertical");

        textAreaScrollTo.bindBidirectional(listViewScrollTo.valueProperty());
    }

}
