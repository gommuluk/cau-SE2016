package etc;

import controller.TabPaneController;
import controller.UndecoratedRootSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private AnchorPane undecoratedRootScene, tabPane;
    private BorderPane undecoratedMainPane;

    @Override
    public void start(Stage primaryStage) throws Exception{

        // Member variable initialize
        this.primaryStage = primaryStage;

        // Stage Setting
        this.primaryStage.setTitle("TEST!");
        this.primaryStage.initStyle(StageStyle.TRANSPARENT);
        this.primaryStage.getIcons().add(new Image("file:res/icon.png"));

        // initialize
        _initUndecoratedRootScene();
        _initTabPane();

        // Stage Show!

        this.primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void _initUndecoratedRootScene(){

        try {
            // Load root Layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/undecoratedRootScene.fxml"));
            undecoratedRootScene = loader.load();
            undecoratedMainPane = (BorderPane)undecoratedRootScene.getChildren().get(0);

            // Show the scene containing the root layout.
            Scene scene = new Scene(undecoratedRootScene);
            scene.setFill(Color.TRANSPARENT);

            // Controller
            UndecoratedRootSceneController controller = loader.getController();
            controller.setStage(this.primaryStage);

            primaryStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void _initTabPane(){

        try {
            // Load root Layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/tabPane.fxml"));

            tabPane = loader.load();

            // Controller
            TabPaneController controller = loader.getController();

            // attach on the center of UndecoratedRoot
            undecoratedMainPane.setManaged(true);
            undecoratedMainPane.setCenter(tabPane);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
