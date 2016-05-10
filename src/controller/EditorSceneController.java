package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.converter.DoubleStringConverter;

/**
 * Created by SH on 2016-05-11.
 */
public class EditorSceneController {

    @FXML private ListView<String> listView;
    @FXML private TextArea textArea;

    @FXML
    private void initialize(){

        // ListView와 TextArea의 스크롤을 동기화시킨다.
        DoubleProperty textAreaScrollTo = textArea.scrollTopProperty();
        ObjectProperty listViewScrollTo = listView.onScrollToProperty();

    }

}
