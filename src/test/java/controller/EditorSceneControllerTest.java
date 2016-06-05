package controller;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import mockInterface.FileDialogInterface;
import model.FileManager;
import model.FileManagerInterface;
import model.LineInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.WaitForAsyncUtils;
import utils.TestUtils;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.testfx.api.FxAssert.verifyThat;


/**
 * Created by SH on 2016-06-04.
 */

public class EditorSceneControllerTest extends ApplicationTest {

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
        FxToolkit.hideStage();
        FxToolkit.cleanupStages();
    }

    @Before
    public void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
    }

    @Test
    public void EditorSceneInitialButtonEnableTest()

    @Test
    public void LoadSuccessTest() {
        //given :
        HighlightEditorInterface editor = find("#editor");

        //when :
        clickOn("#btnFileOpen");

        assertEquals(1, listTargetWindows().size());
        type(KeyCode.S, KeyCode.R,KeyCode.C, KeyCode.ENTER);
        type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T, KeyCode.ENTER);
        type(KeyCode.R, KeyCode.E, KeyCode.S, KeyCode.O, KeyCode.U, KeyCode.R, KeyCode.C, KeyCode.E, KeyCode.S, KeyCode.ENTER);
        type(KeyCode.S, KeyCode.A, KeyCode.V, KeyCode.E, KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T,
                KeyCode.PERIOD, KeyCode.T, KeyCode.X, KeyCode.T, KeyCode.ENTER);
        //then :
        assertEquals(editor.getText(), FileManager.getFileManagerInterface().getString(FileManagerInterface.SideOfEditor.Left));
    }

    @Test
    public void SaveSuccessWithNoFileTest() {

        //when :
        clickOn("#btnEdit"); WaitForAsyncUtils.waitForFxEvents();
        clickOn("#editor"); WaitForAsyncUtils.waitForFxEvents();

        type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        clickOn("#btnEdit"); WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnFileSave"); WaitForAsyncUtils.waitForFxEvents();

        assertEquals(2, listTargetWindows().size());
        type(KeyCode.SPACE);
        type(KeyCode.S, KeyCode.R,KeyCode.C, KeyCode.ENTER);
        type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T, KeyCode.ENTER);
        type(KeyCode.R, KeyCode.E, KeyCode.S, KeyCode.O, KeyCode.U, KeyCode.R, KeyCode.C, KeyCode.E, KeyCode.S, KeyCode.ENTER);
        type(KeyCode.S, KeyCode.A, KeyCode.V, KeyCode.E, KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T,
                KeyCode.PERIOD, KeyCode.T, KeyCode.X, KeyCode.T, KeyCode.ENTER);

        String tempString = "";
        try {
            Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream("src/test/resources/savetest1.txt"))));

            while(in.hasNextLine()) {
                tempString += in.nextLine() + "\n"; //임시 텍스트에 개행을 제외한 한 줄을 불러온다
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(FileManager.getFileManagerInterface().getString(FileManagerInterface.SideOfEditor.Left) + "\n", tempString);
        File file = new File("src/test/resources/savetest1.txt");
        file.delete();

    }

    @Test
    public void SaveSuccessWithExistFileTest() {
        //given :
        HighlightEditorInterface editor = find("#editor");

        //when :
        clickOn("#btnFileOpen"); WaitForAsyncUtils.waitForFxEvents();

        assertEquals(1, listTargetWindows().size());
        type(KeyCode.S, KeyCode.R,KeyCode.C, KeyCode.ENTER);
        type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T, KeyCode.ENTER);
        type(KeyCode.R, KeyCode.E, KeyCode.S, KeyCode.O, KeyCode.U, KeyCode.R, KeyCode.C, KeyCode.E, KeyCode.S, KeyCode.ENTER);
        type(KeyCode.S, KeyCode.A, KeyCode.V, KeyCode.E, KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T,
                KeyCode.PERIOD, KeyCode.T, KeyCode.X, KeyCode.T, KeyCode.ENTER);

        clickOn("#btnEdit"); WaitForAsyncUtils.waitForFxEvents();
        clickOn("#editor"); WaitForAsyncUtils.waitForFxEvents();

        type(KeyCode.ENTER, KeyCode.S, KeyCode.A, KeyCode.V, KeyCode.E, KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        clickOn("#btnEdit"); WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnFileSave"); WaitForAsyncUtils.waitForFxEvents();

        type(KeyCode.SPACE);
        //then :
        assertEquals(editor.getText(), FileManager.getFileManagerInterface().getString(FileManagerInterface.SideOfEditor.Left));

        clickOn("#btnEdit"); WaitForAsyncUtils.waitForFxEvents();
        clickOn("#editor"); WaitForAsyncUtils.waitForFxEvents();

        type(KeyCode.BACK_SPACE, KeyCode.BACK_SPACE, KeyCode.BACK_SPACE, KeyCode.BACK_SPACE, KeyCode.BACK_SPACE,
                KeyCode.BACK_SPACE, KeyCode.BACK_SPACE, KeyCode.BACK_SPACE, KeyCode.BACK_SPACE);
        clickOn("#btnEdit"); WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnFileSave"); WaitForAsyncUtils.waitForFxEvents();

        type(KeyCode.SPACE);

        assertEquals(editor.getText(), FileManager.getFileManagerInterface().getString(FileManagerInterface.SideOfEditor.Left));

    }

    @Test
    public void DoNotSaveWithNoFileTest() {
        //when :
        clickOn("#btnEdit"); WaitForAsyncUtils.waitForFxEvents();
        clickOn("#editor"); WaitForAsyncUtils.waitForFxEvents();

        type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        clickOn("#btnEdit"); WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnFileSave"); WaitForAsyncUtils.waitForFxEvents();

        assertEquals(2, listTargetWindows().size());
        type(KeyCode.TAB, KeyCode.SPACE);
        assertEquals(1, listTargetWindows().size());
    }

    @Test
    public void DoNotSaveWithExistFileTest() {
        //given :
        HighlightEditorInterface editor = find("#editor");

        //when :
        clickOn("#btnFileOpen"); WaitForAsyncUtils.waitForFxEvents();

        assertEquals(1, listTargetWindows().size());
        type(KeyCode.S, KeyCode.R,KeyCode.C, KeyCode.ENTER);
        type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T, KeyCode.ENTER);
        type(KeyCode.R, KeyCode.E, KeyCode.S, KeyCode.O, KeyCode.U, KeyCode.R, KeyCode.C, KeyCode.E, KeyCode.S, KeyCode.ENTER);
        type(KeyCode.S, KeyCode.A, KeyCode.V, KeyCode.E, KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T,
                KeyCode.PERIOD, KeyCode.T, KeyCode.X, KeyCode.T, KeyCode.ENTER);

        clickOn("#btnEdit"); WaitForAsyncUtils.waitForFxEvents();
        clickOn("#editor"); WaitForAsyncUtils.waitForFxEvents();

        type(KeyCode.ENTER, KeyCode.S, KeyCode.A, KeyCode.V, KeyCode.E, KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        clickOn("#btnEdit"); WaitForAsyncUtils.waitForFxEvents();
        clickOn("#btnFileSave"); WaitForAsyncUtils.waitForFxEvents();

        type(KeyCode.TAB, KeyCode.SPACE);

        String tempString = "";
        try {
            Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream("src/test/resources/savetest.txt"))));

            while(in.hasNextLine()) {
                tempString += in.nextLine() + "\n"; //임시 텍스트에 개행을 제외한 한 줄을 불러온다
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertNotEquals(FileManager.getFileManagerInterface().getString(FileManagerInterface.SideOfEditor.Left) + "\n", tempString);
    }

    @Test
    public void EditorSceneButtonLoadClickTest(){

        Node btnFileOpen = find("#btnFileOpen");
        HighlightEditorInterface editor = find("#editor");
        ListView<LineInterface> highlightListView = editor.getHighlightListView();

        clickOn(btnFileOpen);

        // 닫았는데 0이면 파일츄저가 안뜬거다.
        closeCurrentWindow();
        assertEquals(1, listTargetWindows().size());

        // OS 종속적인 Dialog 는 testFX로 테스팅이 불가능하다. 따라서 mock을 생성함.
        FileDialogInterface fileDialogMock = createMock(FileDialogInterface.class);
        expect(fileDialogMock.getPath(FileManagerInterface.SideOfEditor.Left))
                .andReturn( getClass().getResource("../test1-1.txt").getPath() );
        replay(fileDialogMock);

        WaitForAsyncUtils.waitForAsyncFx(5000, ()->{

            try {
                FileManager.getFileManagerInterface().loadFile(
                        fileDialogMock.getPath(FileManagerInterface.SideOfEditor.Left),
                        FileManagerInterface.SideOfEditor.Left
                );

                String[] textContents = { "a", "b", "c", "d", "e", "c", "", "e", "d", "d", "e", "d", "c", "", "d", "d", "d", "d", "d" };
                ObservableList<LineInterface> items = highlightListView.getItems();

                for(int i=0; i<items.size(); i++) {
                    assertEquals(items.get(i).toString(), textContents[i]);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });

        verify(fileDialogMock);

    }

    @Test
    public void EditorSceneButtonEditClickTest(){

        Node[] buttons = { find("#btnFileOpen"), find("#btnFileSave"), find("#btnEdit"), find("#btnCompare") };
        clickOn(buttons[2]);
        WaitForAsyncUtils.waitForFxEvents();

        // Edit 버튼을 누르면 나머지는 비활성화 되어야 한다.
        verifyThat(buttons[0], NodeMatchers.isDisabled());
        verifyThat(buttons[1], NodeMatchers.isDisabled());
        verifyThat(buttons[3], NodeMatchers.isDisabled());

        // Edit 버튼을 누르면 textArea가 보여야 한다.
        Node textArea = find("#editor");
        verifyThat(textArea, NodeMatchers.isVisible());

        clickOn("#btnFileSave");
        assertEquals(1, listTargetWindows().size());
        clickOn("#btnFileOpen");
        assertEquals(1, listTargetWindows().size());


        clickOn(buttons[2]);

    }

    @Test
    public void EditorSceneSyncEditorAndListViewTest(){

        Node btnEdit = find("#btnEdit");
        HighlightEditorInterface editor = find("#editor");
        ListView<LineInterface> highlightListView = editor.getHighlightListView();
        TextArea textArea = editor.getTextArea();

        clickOn(btnEdit); WaitForAsyncUtils.waitForFxEvents();

        clickOn(textArea).write("test!");
        clickOn(btnEdit);

        ObservableList<LineInterface> items = highlightListView.getItems();
        assertEquals(items.get(0).toString(), "test!");

    }

    @After
    public void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        FxToolkit.cleanupStages();

        FileManager.getFileManagerInterface().resetModel(FileManagerInterface.SideOfEditor.Left);
        FileManager.getFileManagerInterface().resetModel(FileManagerInterface.SideOfEditor.Right);

        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }

    private <T extends Node> T find(final String query) {
        return lookup(query).query();
    }

}