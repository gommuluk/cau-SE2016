package controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import model.FileManager;
import model.Line;

import java.io.IOException;
import java.util.ArrayList;

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
                _syncEditorsScroll();
                //_syncEditorContentWithHighlightList();
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

        this.highlightList.setItems(FXCollections.observableArrayList(
                FileManager.getFileManager().getFileModelL().getLineArrayList()
        ));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setHighlightLines(ArrayList<Line> lines) {
        highlightList.setItems(FXCollections.observableArrayList(lines));
    }

    @Override
    public void changeMode(boolean isEditable) {
        if( isEditable ){
            this.highlightList.setVisible(false);
            this.editor.setVisible(true);
        } else {
            this.highlightList.setVisible(true);
            this.editor.setVisible(false);
        }
    }

    @Override
    public String getText(){
        return editor.getText();
    }

    @Override
    public TextArea getTextArea() {
        return this.editor;
    }

    @Override
    public ListView getHighlightListView() {
        return this.highlightList;
    }


    private void _syncEditorsScroll(){
        // ListView와 TextArea의 스크롤을 동기화시킨다.
        ScrollBar textAreaVScroll = (ScrollBar) editor.lookup(".scroll-bar:vertical");
        ScrollBar listViewVScroll = (ScrollBar) highlightList.lookup(".scroll-bar:vertical");

        ScrollBar textAreaHScroll = (ScrollBar) editor.lookup(".scroll-bar:horizontal");
        ScrollBar listViewHScroll = (ScrollBar) highlightList.lookup(".scroll-bar:horizontal");

        textAreaVScroll.valueProperty().bindBidirectional(listViewVScroll.valueProperty());
        textAreaHScroll.valueProperty().bindBidirectional(listViewHScroll.valueProperty());
    }


    @SuppressWarnings("unchecked")
    private void _enableHighLights(){

        if( highlightList != null ) {
            highlightList.setCellFactory(list -> new ListCell<Line>() {
                BooleanBinding invalid ;
//                {
//                    if( this.getParent().getParent().getId().contentEquals("leftEditor") ) {
//                        invalid = Bindings.createBooleanBinding(
//                                () -> FileManager.getFileManager().getFileModelL().getList().contains(getIndex()),
//                                indexProperty(), itemProperty(), FileManager.getFileManager().getFileModelL().getList()
//                        );
//
//                    } else {
//                        invalid = Bindings.createBooleanBinding(
//                                () -> FileManager.getFileManager().getFileModelR().getList().contains(getIndex()),
//                                indexProperty(), itemProperty(), FileManager.getFileManager().getFileModelR().getList()
//                        );
//                    }
//
//                    invalid.addListener((obs, wasInvalid, isNowInvalid) -> {
//                        if (isNowInvalid) {
//                            setStyle("-fx-background-color:yellowgreen;");
//                        } else {
//                            setStyle("");
//                        }
//                    });
//
//                }

                @Override
                protected void updateItem(Line item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getLine(false));
                }
            });
        }

    }

}
