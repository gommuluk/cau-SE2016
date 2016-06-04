package controller;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.FileManager;
import model.FileManagerInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.support.WaitUntilSupport;
import org.testfx.util.WaitForAsyncUtils;
import utils.TestUtils;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * Created by SH on 2016-06-04.
 */
public class TabPaneSceneControllerTest extends ApplicationTest {

    private Stage s;
    private Node leftSplit, rightSplit;
    private HighlightEditorInterface leftEditor, rightEditor;

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(() -> new Stage());
    }

    @Override
    public void start(Stage stage) {
        s = TestUtils.startStage(stage);

        if( s.getScene() != null ) {
            leftSplit = s.getScene().lookup("#leftSplit");
            rightSplit = s.getScene().lookup("#rightSplit");

            leftEditor = (HighlightEditorController)leftSplit.lookup("#editor");
            rightEditor = (HighlightEditorController)rightSplit.lookup("#editor");
        }
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
        FxToolkit.cleanupStages();
    }


    @Before
    public void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void TabPaneSceneBothSideLoadTest() throws FileNotFoundException, UnsupportedEncodingException {
            String leftFile = getClass().getResource("../test1-1.txt").getPath();
            String rightFile = getClass().getResource("../test1-2.txt").getPath();

            FileManager.getFileManagerInterface().loadFile(leftFile, FileManagerInterface.SideOfEditor.Left);
            FileManager.getFileManagerInterface().loadFile(rightFile, FileManagerInterface.SideOfEditor.Right);

            assertEquals( FileManager.getFileManagerInterface().getString(FileManagerInterface.SideOfEditor.Left),
                    "a\nb\nc\nd\ne\nc\n\ne\nd\nd\ne\nd\nc\n\nd\nd\nd\nd\nd" );

            assertEquals( FileManager.getFileManagerInterface().getString(FileManagerInterface.SideOfEditor.Right),
                    "e\nb\nc\nd\ne\nc\n\ne\nd\nd\ne\nd\nc\n\nd\nd\nd\nd\ne" );
    }

    @Test
    public void TabPaneSceneSplitPaneDragTest(){
        Node divider = find(".split-pane-divider");
        drag(divider).moveBy(-1000, 0);
        drag(divider).moveBy(1000, 0);
    }


    @After
    public void tearDown() throws TimeoutException {

        FxToolkit.cleanupStages();
        FxToolkit.hideStage();

        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }


}