package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by SH on 2016-05-30.
 */
public class AboutSceneController extends Stage implements AboutSceneDialog {

    @FXML
    private BorderPane aboutScene;

    public AboutSceneController(){
        try {
            // Load root Layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/aboutScene.fxml"));
            loader.setController(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(loader.load());

            // init
            super.initModality(Modality.APPLICATION_MODAL);
            super.initOwner(aboutScene.getScene().getWindow());
            super.setResizable(false);

            setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
