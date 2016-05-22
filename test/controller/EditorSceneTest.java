package controller;

import model.FileManagerInterface;
import org.easymock.EasyMock;
import org.junit.Test;

import java.io.IOException;

import static org.easymock.EasyMock.createMock;


/**
 * Created by Elliad on 2016-05-13.
 */
public class EditorSceneTest {
    FileManagerInterface mock;

    @Test
    public void saveTest() throws IOException {
        mock = createMock(FileManagerInterface.class);
        EditorSceneController ctrr = new EditorSceneController();
        ctrr.useSaveActionMethod();
    }


}
