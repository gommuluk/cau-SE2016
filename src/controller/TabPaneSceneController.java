package controller;

import etc.MouseRobot;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.*;

/**
 * Created by SH on 2016-05-08.
 */
public class TabPaneSceneController {

    @FXML private TabPane tabPane;
    @FXML private AnchorPane leftEditor, rightEditor;

    private Tab currentTab;
    private List<Tab> originalTabs;
    private Map<Integer, Tab> tapTransferMap;
    private String[] stylesheets;

    private boolean isAlwaysOnTop = true;


    // 생성자
    public TabPaneSceneController(){
        originalTabs = new ArrayList<>();
        stylesheets = new String[]{};
        tapTransferMap = new HashMap<>();
    }

    // getter & setter
    public void setStylesheets(String... stylesheets) {
        this.stylesheets = stylesheets;
    }


    @FXML // FXML 로딩이 완료되면 호출되는 콜백함수
    public void initialize(){
        _init();
        _syncEditorsScrollBar();
    }

    @FXML // 탭을 드래그하기 시작하면 수행되는 액션
    private void onTabPaneDragDetected(MouseEvent event){

        if (event.getSource() instanceof TabPane) {
            Pane rootPane = (Pane) tabPane.getScene().getRoot();

            rootPane.setOnDragOver((DragEvent event1) -> {
                event1.acceptTransferModes(TransferMode.ANY);
                event1.consume();
            });

            currentTab = tabPane.getSelectionModel().getSelectedItem();
            SnapshotParameters snapshotParams = new SnapshotParameters();
            snapshotParams.setTransform(Transform.scale(0.4, 0.4));

            WritableImage snapshot = currentTab.getContent().snapshot(snapshotParams, null);
            Dragboard db = tabPane.startDragAndDrop(TransferMode.MOVE);

            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.put(DataFormat.PLAIN_TEXT, "소공 2팀");

            db.setDragView(snapshot, 40, 40);
            db.setContent(clipboardContent);
        }

        event.consume();
    }

    @FXML // 탭을 드래그완료 했을때 수행되는 액션
    private void onTabPaneDragDone(DragEvent event){
        _openTabInStage(currentTab);
        tabPane.setCursor(Cursor.DEFAULT);
        event.consume();
    }


    private void _init(){
        originalTabs.addAll(tabPane.getTabs());

        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            tapTransferMap.put(i, tabPane.getTabs().get(i));
        }

        tabPane.getTabs().stream().forEach(t -> {
            t.setClosable(false);
        });
    }
    private void _syncEditorsScrollBar(){
        DoubleProperty leftVerticalScroll = ((TextArea)leftEditor.getChildren().get(1)).scrollTopProperty();
        DoubleProperty rightVerticalScroll = ((TextArea)rightEditor.getChildren().get(1)).scrollTopProperty();

        DoubleProperty leftHorizontalScroll = ((TextArea)leftEditor.getChildren().get(1)).scrollLeftProperty();
        DoubleProperty rightHorizontalScroll = ((TextArea)rightEditor.getChildren().get(1)).scrollLeftProperty();

        leftVerticalScroll.bindBidirectional(rightVerticalScroll);
        leftHorizontalScroll.bindBidirectional(rightHorizontalScroll);
    }
    private void _openTabInStage(final Tab tab) {
        if(tab == null) return;

        int originalTab = originalTabs.indexOf(tab);
        tapTransferMap.remove(originalTab);
        Pane content = (Pane) tab.getContent();
        if (content == null) {
            throw new IllegalArgumentException("Can not detach Tab '" + tab.getText() + "': content is empty (null).");
        }
        tab.setContent(null);
        final Scene scene = new Scene(content, content.getPrefWidth(), content.getPrefHeight());
        scene.getStylesheets().addAll(stylesheets);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(tab.getText());
        stage.setAlwaysOnTop(isAlwaysOnTop);

        Point2D p = MouseRobot.getMousePosition();

        stage.setX(p.getX());
        stage.setY(p.getY());
        stage.setOnCloseRequest((WindowEvent t) -> {
            stage.close();
            tab.setContent(content);
            int originalTabIndex = originalTabs.indexOf(tab);
            tapTransferMap.put(originalTabIndex, tab);
            int index = 0;
            SortedSet<Integer> keys = new TreeSet<>(tapTransferMap.keySet());
            for (Integer key : keys) {
                Tab value = tapTransferMap.get(key);
                if(!tabPane.getTabs().contains(value)){
                    tabPane.getTabs().add(index, value);
                }
                index++;
            }
            tabPane.getSelectionModel().select(tab);
        });

        stage.setOnShown((WindowEvent t) -> {
            tab.getTabPane().getTabs().remove(tab);
        });

        stage.show();
    }
    @FXML //TODO 나중에 (Toolbar)Pane을 나눠야 됩니다.
    //TODO TabPane으로 옮겨야 합니다.
    private void onTBBtnFileOpenClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        //File selectedFile = fileChooser.showOpenDialog();
        //if (selectedFile != null) {
        //    mainStage.display(selectedFile);
        //}
    }

}