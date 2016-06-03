package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import model.FileManager;
import model.FileManagerInterface;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by Elliad on 2016-05-13.
 */
public class EditorSceneControllerTest{
    private HighlightEditorInterface editor;

    private Button btnFileSave, btnFileOpen;
    private ToggleButton btnEdit;

    private Button btnCompare, btnMergeLeft, btnMergeRight;
    private FileManagerInterface.SideOfEditor side = null;
    private BooleanProperty isFocused = new SimpleBooleanProperty(false);

    @Test
    public void saveTest() {
        EditorSceneController ctrr = new EditorSceneController();
        ctrr.useSaveActionMethod();
        ctrr.testHelper(editor, btnFileSave, btnFileOpen, btnEdit);
    }



}