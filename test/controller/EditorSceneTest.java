package controller;

import model.FileManagerInterface;
import org.junit.Test;

import java.io.IOException;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;


/**
 * Created by Elliad on 2016-05-13.
 */
public class EditorSceneTest {
    FileManagerInterface mock;

    @Test
    public void saveTest() {
        mock = createMock(FileManagerInterface.class);
        ControlPaneSceneController ctrr = new ControlPaneSceneController();
        assertEquals(ctrr.getBtnCompare().isDisable(), false);

    }



}
