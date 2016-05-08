package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
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

    private boolean draggableZoneX, draggableZoneY;
    private boolean isMaximized = false, isDragging = false, initMinHeight = false, initMinWidth = false;

    private final int RESIZE_MARGIN = 5;
    private final double INIT_WIDTH = 850, INIT_HEIGHT = 650;


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


    @FXML // 메인Pane에 마우스를 올렸을때의 액션
    private void onMainPaneMouseOver(MouseEvent event){

        if ( _isInDraggableZone(event) || isDragging || !isMaximized ) {
            if (draggableZoneY) mainPane.setCursor(Cursor.S_RESIZE);
            if (draggableZoneX) mainPane.setCursor(Cursor.E_RESIZE);

        } else {
            mainPane.setCursor(Cursor.DEFAULT);
        }

    }

    @FXML // 메인Pane에서 마우스를 때었을때의 액션
    private void onMainPaneMouseReleased(){
        isDragging = false;
        mainPane.setCursor(Cursor.DEFAULT);
    }

    @FXML // 메인Pane을 드래그했을때의 액션
    private void onMainPaneDragged(MouseEvent event){

        if (!isDragging || isMaximized) return;

        if (draggableZoneY) {
            double mousey = event.getScreenY();
            double newHeight = mainWindow.getHeight() + (mousey - dragDelta.y);

            if( newHeight >= INIT_HEIGHT ){
                mainWindow.setHeight(newHeight);
                dragDelta.y = mousey;
            }
        }

        if (draggableZoneX) {
            double mousex = event.getScreenX();
            double newWidth = mainWindow.getWidth() + (mousex - dragDelta.x);

            if( newWidth >= INIT_WIDTH ) {
                mainWindow.setWidth(newWidth);
                dragDelta.x = mousex;
            }
        }
    }

    @FXML // 메인Pane을 마우스로 롱-클릭했을때의 액션
    private void onMainPanePressed(MouseEvent event){

        if (!_isInDraggableZone(event)) return;

        isDragging = true;

        if (!initMinHeight) {
            mainWindow.setMinHeight(mainPane.getHeight());
            initMinHeight = true;
        }

        if (!initMinWidth) {
            mainWindow.setMinWidth(mainPane.getWidth());
            initMinWidth = true;
        }

        dragDelta.y = event.getScreenY();
        dragDelta.x = event.getScreenX();
    }

    //TODO 리펙토링이 필요해 보이는 함수들?
    private boolean _isInDraggableZone(MouseEvent event) {
        draggableZoneY = (event.getY() > (mainPane.getHeight() - RESIZE_MARGIN));
        draggableZoneX = (event.getX() > (mainPane.getWidth() - RESIZE_MARGIN));
        return (draggableZoneY || draggableZoneX);
    }


}
