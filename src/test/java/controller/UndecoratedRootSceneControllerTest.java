package controller;

import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.GeometryMatchers;
import org.testfx.util.WaitForAsyncUtils;
import utils.FxImageComparison;
import utils.TestUtils;

import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;


/**
 * Created by SH on 2016-06-03.
 */

public class UndecoratedRootSceneControllerTest extends ApplicationTest implements FxImageComparison {

    private Stage stage;

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(Stage::new);
    }

    @Override
    public void start(Stage stage) {
        this.stage = TestUtils.startStage(stage);
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    @Before
    public void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        stage.setX(50); stage.setY(50);
    }

    @After
    public void tearDown() throws TimeoutException {

        FxToolkit.cleanupStages();
        FxToolkit.hideStage();

        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Test
    public void undecoratedRootSceneDoubleClickTest() {

        final double EPSILON = 40;
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int)screen.getWidth(), screenHeight = (int)screen.getHeight();
        final int originWidth = (int) stage.getWidth(), originHeight = (int) stage.getHeight();

        // maximize & minimize
        doubleClickOn("#controlPane");
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(screenWidth, stage.getWidth(), EPSILON);
        assertEquals(screenHeight, stage.getHeight(), EPSILON);

        // minimize
        doubleClickOn("#controlPane");
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(originWidth, stage.getWidth(), EPSILON);
        assertEquals(originHeight, stage.getHeight(), EPSILON);

    }

    @Test
    public void undecoratedRootSceneDragTest() {

        final double EPSILON = 10; // 오차범위(엡실론)
        int expectedX, expectedY;
        final GridPane mainPaneControlPane = find("#controlPane");
        final Random r = new Random();

        expectedX = Math.abs(r.nextInt(1024));
        expectedY = Math.abs(r.nextInt(768));

        // Drag & Move this window
        drag(mainPaneControlPane).moveTo(_getSceneCoord(expectedX, expectedY));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(expectedX, mainPaneControlPane.getScene().getWindow().getX(), EPSILON);
        assertEquals(expectedY, mainPaneControlPane.getScene().getWindow().getY(), EPSILON);
    }

    @Test
    public void undecoratedRootSceneButtonMinimizeTest() {

        clickOn("#btnMinimize");
        assertTrue(stage.isIconified());

        WaitForAsyncUtils.waitForAsyncFx(5000, ()-> stage.setIconified(false));
    }

    @Test
    public void undecoratedRootSceneResizableZoneCursorTest(){
        final int MARGIN = 5;
        final Node mainPane = find("#mainPane");

        moveTo(stage.getX() + stage.getWidth() - MARGIN, stage.getY() + stage.getHeight()/2);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(Cursor.E_RESIZE, mainPane.getCursor());

        moveTo(stage.getX() + stage.getWidth()/2, stage.getY() + stage.getHeight() - MARGIN);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(Cursor.S_RESIZE, mainPane.getCursor());

    }

    @Test
    public void undecoratedRootSceneResizeTest(){
        final int MARGIN = 5;

        final double prevWidth = stage.getWidth();

        drag(stage.getX() + stage.getWidth() - MARGIN, stage.getY() + stage.getHeight()/2).moveBy(300, 0);
        assertEquals(prevWidth+300, stage.getWidth(), 0);

        drag(stage.getX() + stage.getWidth() - MARGIN, stage.getY() + stage.getHeight()/2).moveBy(-300, 0);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(prevWidth, stage.getWidth(), 0);

        // 비정상적인 케이스(최소 넓이 보장)
        drag(stage.getX() + stage.getWidth() - MARGIN, stage.getY() + stage.getHeight()/2).moveBy(-300, 0);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(prevWidth, stage.getWidth(), 0);

    }

    @Test
    public void undecoratedRootSceneButtonCloseTest(){

        final boolean[] isClosed = { false };

        stage.setOnCloseRequest(event -> isClosed[0] = true);
        clickOn("#btnClose");
        WaitForAsyncUtils.waitForFxEvents();

        assertTrue(isClosed[0]);
    }

    @Test
    public void undecoratedRootSceneButtonMaximizeOrRestoreTest() {

        BoundingBox savedBounds = new BoundingBox(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());

        ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(
                stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight()
        );

        clickOn("#btnMaximize");
        WaitForAsyncUtils.waitForFxEvents();

        Screen screen = screensForRectangle.get(0);
        Rectangle2D visualBounds = screen.getVisualBounds();
        Dimension2D programBounds = new Dimension2D(stage.getWidth(), stage.getHeight());
        verifyThat(programBounds, GeometryMatchers.hasDimension(visualBounds.getWidth(), visualBounds.getHeight()));

        clickOn("#btnMaximize");
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat( new Dimension2D(savedBounds.getWidth(), savedBounds.getHeight()),
                GeometryMatchers.hasDimension(stage.getWidth(), stage.getHeight()) );

    }

    @Test
    public void undecoratedRootSceneRenderingTest() throws IOException {
        Node tPane = find("#tabPane");
        assertNotNull("tabPane is null", tPane);
        assertSnapshotsEqual(getClass().getResource("../undecoratedRootScene.png").getPath(), tPane, 5);
    }

    private Point2D _getSceneCoord(int x, int y){

        final GridPane mainPaneControlPane = find("#controlPane");
        final int RESIZE_MARGIN = 5;
        double halfWidth = mainPaneControlPane.getWidth() / 2;
        final int CONTROL_PANE_HEIGHT = 18;

        return new Point2D( RESIZE_MARGIN + halfWidth + x, CONTROL_PANE_HEIGHT + RESIZE_MARGIN + y );
    }

    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }

}