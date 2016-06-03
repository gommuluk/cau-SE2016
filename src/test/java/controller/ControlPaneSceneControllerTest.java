package controller;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import utils.TestUtils;

import java.util.concurrent.TimeoutException;

/**
 * Created by SH on 2016-06-03.
 */
public class ControlPaneSceneControllerTest extends ApplicationTest {

    private Stage s;

    @Override
    public void start(Stage stage) {
        s = TestUtils.startStage(stage);
    }

    /* Just a shortcut to retrieve widgets in the GUI. */
    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }

    @Before
    public void setUp() {

    }

    @Test
    public void ControlPaneSceneMinimizeButtonTest(){

    }

    @After
    public void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }



}