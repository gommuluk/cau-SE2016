package controller;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import model.FileManager;
import model.FileManagerInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;
import utils.TestUtils;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * Created by SH on 2016-06-03.
 */
public class ControlPaneSceneControllerTest extends ApplicationTest {

    private Stage s;

    @Override
    public void init() throws Exception {
        FxToolkit.registerStage(() -> new Stage());
    }

    @Override
    public void start(Stage stage) {
        s = TestUtils.startStage(stage);
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();
    }

    @Before
    public void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void ControlPaneSceneInitialButtonClickTest(){
        Node[] buttons = { find("#btnMergeLeft"), find("#btnMergeRight"), find("#btnCompare") };

        // btnMergeLeft, btnMergeRight는 비활성화 상태이다.
        verifyThat(buttons[0], NodeMatchers.isDisabled());
        verifyThat(buttons[1], NodeMatchers.isDisabled());
        verifyThat(buttons[2], NodeMatchers.isEnabled());
    }

    @Test
    public void ControlPaneSceneInitialMenuItemClickTest(){
        Node[] menuItems = { find("#menuFile"), find("#menuEdit"), find("#menuHelps") };

        // 처음에는 모든 메뉴아이템이 활성화 상태이다.
        for(Node node : menuItems){
            clickOn(node);
            verifyThat(node, NodeMatchers.isEnabled());
        }

    }

    @Test
    public void ControlPaneSceneShortCutKeyClickTest(){

        final KeyCode SHORTCUT = System.getProperty("os.name").toLowerCase().contains("mac") ? KeyCode.COMMAND : KeyCode.CONTROL;

        push(SHORTCUT, KeyCode.RIGHT); // Copy to right
        WaitForAsyncUtils.waitForFxEvents();

        // 불가능한 창이 하나 떠야되는데 안뜨면 이상한거다.
        assertEquals(2, listWindows().size());

        closeCurrentWindow();

        push(SHORTCUT, KeyCode.LEFT); // Copy to left
        WaitForAsyncUtils.waitForFxEvents();

        // 불가능한 창이 하나 떠야되는데 안뜨면 이상한거다.
        assertEquals(2, listWindows().size());

    }

    @Test
    public void ControlPaneSceneButtonCompareTest() {

        Node btnCompare = find("#btnCompare");

        String leftFile = getClass().getResource("../test1-1.txt").getPath();
        String rightFile = getClass().getResource("../test1-2.txt").getPath();

        WaitForAsyncUtils.waitForAsyncFx(5000, ()->{
            try {
                FileManager.getFileManagerInterface().loadFile(leftFile, FileManagerInterface.SideOfEditor.Left);
                FileManager.getFileManagerInterface().loadFile(rightFile, FileManagerInterface.SideOfEditor.Right);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        clickOn(btnCompare);

        // assert 필요
        assertEquals(FileManager.getFileManagerInterface().getComparing(), true);
    }

    @After
    public void tearDown() throws TimeoutException {
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();

        FileManager.getFileManagerInterface().resetModel(FileManagerInterface.SideOfEditor.Left);
        FileManager.getFileManagerInterface().resetModel(FileManagerInterface.SideOfEditor.Right);

        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }

}