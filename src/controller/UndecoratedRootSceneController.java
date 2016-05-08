package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Delta;

/**
 * Created by SH on 2016-05-07.
 */
public class UndecoratedRootSceneController {

    @FXML private AnchorPane shadowPane;
    @FXML private BorderPane mainPane;
    @FXML private Label labelTitle;

    private Stage mainWindow = null;
    private BoundingBox savedBounds = null;
    private Delta dragDelta;

    private boolean isMaximized = false;
    private boolean isDraggable = false;


    // 생성자
    public UndecoratedRootSceneController(){
        this.dragDelta = new Delta();
    }

    // setter
    public void setStage(Stage s){
        this.mainWindow = s;
    }


    @FXML // FXML이 로딩되면 불리는 콜백함수
    private void initialize(){
        labelTitle.setText("TEST");
    }

    @FXML // 최소화버튼액션
    private void onBtnMinimizeClicked(){
        // JavaFX Application Thread(UI Thread)에서 최소화 동작수행.
        mainWindow.setIconified(true);
    }

    @FXML // 최대화/복구 버튼액션
    private void onBtnMaxmizeOrRestoreClicked(){

        // JavaFX Application Thread(UI Thread)에서 최대화/최소화 동작수행.
        if ( isMaximized )
            _minimize();
        else
            _maximize();

    }

    //TODO 리펙토링이 필요한 함수입니다.
    private void _minimize(){
        mainWindow.setX(savedBounds.getMinX());
        mainWindow.setY(savedBounds.getMinY());
        mainWindow.setWidth(savedBounds.getWidth());
        mainWindow.setHeight(savedBounds.getHeight());

        shadowPane.setStyle("-fx-padding: 5 5 5 5;");
        mainPane.setStyle("-fx-border-width: 1px;");
        savedBounds = null;

        isMaximized = false;
    }
    private void _maximize(){
        ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(
                mainWindow.getX(),
                mainWindow.getY(),
                mainWindow.getWidth(),
                mainWindow.getHeight()
        );

        Screen screen = screensForRectangle.get(0);
        Rectangle2D visualBounds = screen.getVisualBounds();

        savedBounds = new BoundingBox(
                mainWindow.getX(),
                mainWindow.getY(),
                mainWindow.getWidth(),
                mainWindow.getHeight()
        );

        shadowPane.setStyle("-fx-padding: 0;");
        mainPane.setStyle("-fx-border-width: 0;");

        mainWindow.setX(visualBounds.getMinX());
        mainWindow.setY(visualBounds.getMinY());
        mainWindow.setWidth(visualBounds.getWidth());
        mainWindow.setHeight(visualBounds.getHeight());
        isMaximized = true;
    }


    @FXML // 닫기버튼액션
    private void onBtnCloseClicked(){
        // JavaFX Application Thread(UI Thread)에서 닫기 동작수행.
        mainWindow.fireEvent(new WindowEvent(mainWindow, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @FXML // 컨트롤Pane을 클릭했을때의 액션
    private void onControlPaneClicked(MouseEvent event){
        if( event.getClickCount() == 2 ){

            if( isMaximized )
                _minimize();
            else
                _maximize();

            event.consume();
        }
    }

    @FXML // 컨트롤Pane을 롱-클릭했을때의 액션
    private void onControlPanePressed(MouseEvent event){
        if( !isMaximized ) {
            dragDelta.x = event.getSceneX();
            dragDelta.y = event.getSceneY();
        }
    }

    @FXML // 컨트롤Pane을 드래그했을때의 액션
    private void onControlPaneDragged(MouseEvent event){
        if( !isMaximized ) {
            mainWindow.setX(event.getScreenX() - dragDelta.x);
            mainWindow.setY(event.getScreenY() - dragDelta.y);
        } else {
            _minimize();
        }
    }

}
