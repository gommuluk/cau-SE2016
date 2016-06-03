package utils;

import controller.UndecoratedRootSceneControllerTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by SH on 2016-06-03.
 */
public class TestUtils {

    public static Stage startStage(Stage stage){

        try {
            Parent undecoratedRootScene = FXMLLoader.load(
                    UndecoratedRootSceneControllerTest.class.getResource("../fxml/undecoratedRootScene.fxml")
            );

            Scene scene = new Scene(undecoratedRootScene);
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
//            stage.initStyle(StageStyle.TRANSPARENT);

            stage.show();
            stage.toFront();

            return stage;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
