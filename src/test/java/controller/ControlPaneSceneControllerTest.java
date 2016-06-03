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
import org.testfx.matcher.base.NodeMatchers;
import utils.TestUtils;

import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * Created by SH on 2016-06-03.
 */
public class ControlPaneSceneControllerTest extends ApplicationTest {

    private Stage s;

    @Override
    public void start(Stage stage) {
        s = TestUtils.startStage(stage);
    }

    @Before
    public void setUp() {

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
    public void ControlPaneSceneInitalMenuItemClickTest(){
        Node[] menuItems = { find("#menuFile"), find("#menuEdit"), find("#menuHelps") };

        // 처음에는 모든 메뉴아이템이 활성화 상태이다.
        for(Node node : menuItems){
            clickOn(node);
            verifyThat(node, NodeMatchers.isEnabled());
        }

    }


    @After
    public void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }

}