package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import mockInterface.FileDialogInterface;
import model.FileManager;
import model.FileManagerInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import utils.TestUtils;

import java.util.concurrent.TimeoutException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;


/**
 * Created by Elliad on 2016-05-13.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditorSceneControllerTest extends ApplicationTest{

    private Stage s;
    private HighlightEditorInterface editor;

    private Button btnFileSave, btnFileOpen;
    private ToggleButton btnEdit;

    private Button btnCompare, btnMergeLeft, btnMergeRight;
    private FileManagerInterface.SideOfEditor side = null;
    private BooleanProperty isFocused = new SimpleBooleanProperty(false);

    @Override
    public void start(Stage stage) {
        s = TestUtils.startStage(stage);
    }

    @Test
    public void saveTest() {
        EditorSceneController ctrr = new EditorSceneController();
        ctrr.useSaveActionMethod();
        ctrr.testHelper(editor, btnFileSave, btnFileOpen, btnEdit);
    }

    @Test
    public void EditorSceneInitialButtonEnableTest(){
        Node[] buttons = { find("#btnFileOpen"), find("#btnFileSave"), find("#btnEdit") };

        for(Node node : buttons){
            verifyThat(node, NodeMatchers.isEnabled());
        }
    }

    @Test
    public void EditorSceneButtonLoadTest(){

        Node[] buttons = { find("#btnFileOpen"), find("#btnFileSave"), find("#btnEdit") };
        clickOn(buttons[0]);

        // 닫았는데 0이면 파일츄저가 안뜬거다.
        closeCurrentWindow();
        assertEquals(1, listTargetWindows().size());

        // OS 종속적인 Dialog 는 testFX로 테스팅이 불가능하다. 따라서 mock을 생성함.
        FileDialogInterface fileDialogMock = createMock(FileDialogInterface.class);
        expect(fileDialogMock.getPath()).andReturn( getClass().getResource("test1-1.txt").getPath() );
        replay(fileDialogMock);

        verify(fileDialogMock);

    }

    @Test
    public void EditorSceneButtonEditClickTest(){

        Node[] buttons = { find("#btnFileOpen"), find("#btnFileSave"), find("#btnEdit") };
        clickOn(buttons[2]);

        // Edit 버튼을 누르면 나머지는 비활성화 되어야 한다.
        verifyThat(buttons[0], NodeMatchers.isDisabled());
        verifyThat(buttons[1], NodeMatchers.isDisabled());

        // Edit 버튼을 누르면 textArea가 보여야 한다.
        Node textArea = find("#editor");
        verifyThat(textArea, NodeMatchers.isVisible());

        clickOn(buttons[2]);
    }

    @Before
    public void setUp() throws TimeoutException {

    }

    @After
    public void tearDown() throws TimeoutException {
        FxToolkit.cleanupStages();
        FxToolkit.hideStage();

        release(new KeyCode[] {});
        release(new MouseButton[] {});
        System.out.println("호출");
    }

    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }

}