package controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import model.FileManager;

import java.io.IOException;

/**
 * Created by SH on 2016-05-19.
 */
public class HighlightEditorController extends AnchorPane implements HighlightEditorInterface {

    @FXML private TextArea editor;
    @FXML private ListView highlightList;

    /* 생성자 */
    public HighlightEditorController() {
        super();

        try {
            FXMLLoader loader = new FXMLLoader(HighlightEditorController.class.getResource("/view/highlightEditor.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

            Platform.runLater(()->{
                _syncEditorScrollWithHighlightList();
                _syncEditorContentWithHighlightList();
                _enableHighLights();

                FileManager.getFileManager().getFileModelL().getList().addAll(0, 1, 2, 4, 10, 20);
            });

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEditable(boolean value){
        editor.setEditable(value);
    }

    @Override
    public boolean isEditable(){
        return editor.isEditable();
    }

    @Override
    public void setText(String s){
        this.editor.setText(s);
    }

    @Override
    public void setHighlightLines(int... lines) {

    }

    @Override
    public String getText(){
        return editor.getText();
    }


    private void _syncEditorScrollWithHighlightList(){
        // ListView와 TextArea의 스크롤을 동기화시킨다.
        ScrollBar textAreaScrollTo = (ScrollBar) editor.lookup(".scroll-bar:vertical");
        ScrollBar listViewScrollTo = (ScrollBar) highlightList.lookup(".scroll-bar:vertical");

        textAreaScrollTo.valueProperty().bindBidirectional(listViewScrollTo.valueProperty());
        listViewScrollTo.valueProperty().bindBidirectional(textAreaScrollTo.valueProperty());
    }

    private void _syncEditorContentWithHighlightList(){

        if( this.getParent().getParent().getId().contentEquals("leftEditor")) {
            highlightList.itemsProperty().bind(FileManager.getFileManager().getFileModelL().getListProperty());
        } else {
            highlightList.itemsProperty().bind(FileManager.getFileManager().getFileModelR().getListProperty());
        }

    }

    private void _enableHighLights(){

        if( highlightList != null ) {


            highlightList.setCellFactory(list -> new ListCell<String>() {
                BooleanBinding invalid ;
                {
                    if( this.getParent().getParent().getId().contentEquals("leftEditor") ) {
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
