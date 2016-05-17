package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.*;
import model.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional; //

/**
 * Created by SH on 2016-05-15.
 */
public class ButtonController {

    @FXML private AnchorPane editorScene;

    @FXML private AnchorPane editorToolbarScene = null;
    @FXML private ToggleButton btnEdit;
    @FXML private Button btnFileOpen;
    @FXML private Button btnFileSave;



    @FXML
    public void initialize(){
        Platform.runLater(()->{
            if( this.editorToolbarScene != null ) {
                this.editorScene = (AnchorPane)((BorderPane)editorToolbarScene.getParent().getParent()).getCenter();
//                this.textArea = (TextArea)editorScene.getChildren().get(1);
            }
        });
    }
}