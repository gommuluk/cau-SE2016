package controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import utils.TestUtils;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;


/**
 * Created by SH on 2016-06-03.
 */

public class UndecoratedRootSceneControllerTest extends ApplicationTest {

    private Stage s;
    private GridPane mainPaneControlPane;

    @Override
    public void start(Stage stage) {
        s = TestUtils.startStage(stage);
        mainPaneControlPane = find("#controlPane");
    }

    @Before
    public void setUp() {
        s.setX(50); s.setY(50);
        WaitForAsyncUtils.waitForFxEvents(10);
    }

    @After
    public void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        FxToolkit.cleanupStages();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    @Test
    public void undecoratedRootSceneDoubleClickTest() {

        final double EPSILON = 40;
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int)screen.getWidth(), screenHeight = (int)screen.getHeight();
        final int originWidth = (int)s.getWidth(), originHeight = (int)s.getHeight();

        // maximize & minimize
        doubleClickOn(mainPaneControlPane);
        WaitForAsyncUtils.waitForFxEvents(5);

        assertEquals(screenWidth, s.getWidth(), EPSILON);
        assertEquals(screenHeight, s.getHeight(), EPSILON);

        // minimize
        doubleClickOn(mainPaneControlPane);
        WaitForAsyncUtils.waitForFxEvents(5);

        assertEquals(originWidth, s.getWidth(), EPSILON);
        assertEquals(originHeight, s.getHeight(), EPSILON);

    }

    @Test
    public void undecoratedRootSceneDragTest() {

        final double EPSILON = 10; // 오차범위(엡실론)
        int expectedX, expectedY;

        final Random r = new Random();

        expectedX = Math.abs(r.nextInt(1024));
        expectedY = Math.abs(r.nextInt(768));

        // Drag & Move this window
        drag(mainPaneControlPane).moveTo(_getSceneCoord(expectedX, expectedY));

        assertEquals(expectedX, mainPaneControlPane.getScene().getWindow().getX(), EPSILON);
        assertEquals(expectedY, mainPaneControlPane.getScene().getWindow().getY(), EPSILON);
    }

    @Test
    public void undecoratedRootSceneButtonMinimizeTest() {

        Node btnMinimize = find("#btnMinimize");

        clickOn(btnMinimize);
        assertTrue(s.isIconified());

        Platform.runLater(()->s.setIconified(false));
    }

    @Test
    public void undecoratedRootSceneResizableZoneCursorTest(){
        final int MARGIN = 5;
        final Node mainPane = find("#mainPane");

        moveTo(s.getX() + s.getWidth() - MARGIN, s.getY() + s.getHeight()/2);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(Cursor.E_RESIZE, mainPane.getCursor());

        moveTo(s.getX() + s.getWidth()/2, s.getY() + s.getHeight() - MARGIN);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(Cursor.S_RESIZE, mainPane.getCursor());

    }

    @Test
    public void undecoratedRootSceneResizeTest(){
        final int MARGIN = 5;
        final Node mainPane = find("#mainPane");

        final double prevWidth = s.getWidth();

        drag(s.getX() + s.getWidth() - MARGIN, s.getY() + s.getHeight()/2).moveBy(300, 0);
        assertEquals(prevWidth+300, s.getWidth(), 0);
    }

    @Test
    public void undecoratedRootSceneButtonCloseTest(){
        Node btnClose = find("#btnClose");
        BooleanProperty isSuccess = new SimpleBooleanProperty(false);

        s.setOnCloseRequest(event -> isSuccess.setValue(true));
        clickOn(btnClose);
        WaitForAsyncUtils.waitForAsyncFx(3000, ()->{
            assertTrue(isSuccess.getValue());
        });
    }

    @Test
    public void undecoratedRootSceneButtonMaximizeOrRestoreTest() {

        BoundingBox savedBounds = new BoundingBox(s.getX(), s.getY(), s.getWidth(), s.getHeight());
        Node btnMaximize = find("#btnMaximize");

        ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(
                s.getX(), s.getY(), s.getWidth(), s.getHeight()
        );

        clickOn(btnMaximize);
        WaitForAsyncUtils.waitForFxEvents();

        Screen screen = screensForRectangle.get(0);
        Rectangle2D visualBounds = screen.getVisualBounds();

        assertEquals(visualBounds.getWidth(), s.getScene().getWidth(), 0);
        assertEquals(visualBounds.getHeight(), s.getHeight(), 0);

        clickOn(btnMaximize);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(savedBounds.getWidth(), s.getWidth(), 0);
        assertEquals(savedBounds.getHeight(), s.getHeight(), 0);

    }


    private Point2D _getSceneCoord(int x, int y){

        final int RESIZE_MARGIN = 5;
        double halfWidth = mainPaneControlPane.getWidth() / 2;
        final int CONTROL_PANE_HEIGHT = 18;

        return new Point2D( RESIZE_MARGIN + halfWidth + x, CONTROL_PANE_HEIGHT + RESIZE_MARGIN + y );
    }

    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }

}