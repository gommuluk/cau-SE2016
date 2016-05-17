package controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.FileManager;
import model.FileModel;


/**
 * Created by SH on 2016-05-11.
 */
public class EditorSceneController {

    @FXML private TextArea textArea;
    @FXML private ListView<String> listView;

    public EditorSceneController(){
        FileManager.getFileManager().getFileModelL().getList().addAll(0, 1, 2, 4, 10, 20);
    }

    @FXML
    private void initialize(){

        Platform.runLater(()-> {

            _syncEditorScrollWithHighlightList();
            _syncEditorContentWithHighlightList();
            _enableHighLights();

            listView.scrollTo(0);
        });
    }

    @FXML // Editor에서 키보드입력이 있을 때의 동작
    private void onTextAreaKeyPressed(KeyEvent event){
        if( event.getCode().compareTo(KeyCode.ENTER) == 0 ){
            System.out.println("enter");
        }
    }

    private void _syncEditorScrollWithHighlightList(){
        // ListView와 TextArea의 스크롤을 동기화시킨다.
        ScrollBar textAreaScrollTo = (ScrollBar) textArea.lookup(".scroll-bar:vertical");
        ScrollBar listViewScrollTo = (ScrollBar) listView.lookup(".scroll-bar:vertical");
        System.out.println(textAreaScrollTo.getValue());
        System.out.println(listViewScrollTo.getValue());

        //textAreaScrollTo.valueProperty().bindBidirectional(listViewScrollTo.valueProperty());
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

}
