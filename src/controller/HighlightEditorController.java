package controller;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.FileManager;
import model.FileManagerInterface;
import model.LineInterface;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by SH on 2016-05-19.
 */
public class HighlightEditorController extends AnchorPane implements HighlightEditorInterface {

    @FXML private TextArea editor;
    @FXML private ListView highlightList;

    private boolean isEditMode = false;

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
                _initEditor();
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
    @SuppressWarnings("unchecked")
    public void setText(FileManagerInterface.SideOfEditor side, String s){
        this.editor.setText(s);

        if( side == FileManagerInterface.SideOfEditor.Left ) {
            update(FileManagerInterface.SideOfEditor.Left);
        } else {
            update(FileManagerInterface.SideOfEditor.Right);
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public void setHighlightLines(ArrayList<LineInterface> lines) {
        highlightList.setItems(FXCollections.observableArrayList(lines));
    }

    @Override
    public void setEditMode(boolean isEditMode) {
        if( isEditMode ){
            this.highlightList.setVisible(false);
            this.editor.setVisible(true);
            this.isEditMode = true;
        } else {
            this.highlightList.setVisible(true);
            this.editor.setVisible(false);
            this.isEditMode = false;
        }
    }

    @Override
    public boolean isEditMode() {
        return this.isEditMode;
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

    @Override
    @SuppressWarnings("unchecked")
    public void update(FileManagerInterface.SideOfEditor side) {
        System.out.println("업데이트 호출");
        FileManager.getFileManagerInterface().updateLineArrayList(editor.getText(), side);
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
    private void _initEditor(){

        if( highlightList != null ) {

            highlightList.setCellFactory(list -> new ListCell<LineInterface>() {
                BooleanBinding invalid ;
                {
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

                }

                @Override
                public void updateSelected(boolean selected) {
                    super.updateSelected(selected);
                    if(selected){
                        LineInterface item = getItem();

//                        if( item.getHighlight() == LineInterface.LineHighlight.isDifferent )
//                            setStyle("-fx-background-color: #44d7ba");

                        System.out.println("selected");
                    }
                }

                @Override
                protected void updateItem(LineInterface item, boolean empty) {
                    super.updateItem(item, empty);

                    setStyle(null); setText(null);

                    if( item != null ) {
                        if( !empty ) setText(item.getContent(false));
                        else {
                            setStyle("-fx-background-color: transparent");
                        }

                        switch(item.getHighlight()){
                            case unHighlighted: setStyle("-fx-background-color:transparent;"); break;
                            case isDifferent: setStyle("-fx-font-fill:black; -fx-background-color: #EDE98D;"); break;
                            case whitespace: setStyle("-fx-background-color: #EEEEEE"); break;
                            case selected: setStyle("-fx-background-color: #44d7ba"); break;
                        }

                        //System.out.println(item.getBlockIndex()+" : "+item.getContent(false));
                    }

                }

            });
            highlightList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        }

    }


    //
    @FXML
    public void handleMouseClick(MouseEvent arg0) {
        //System.out.println(highlightList.getSelectionModel().getSelectedIndex());
        FileManager.getFileManagerInterface().clickLine(highlightList.getSelectionModel().getSelectedIndex());
    }
}
