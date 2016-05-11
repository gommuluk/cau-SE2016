package model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Elliad on 2016-05-08.
 */
public class FileModelTest {
    @Test
    public void singletonTest() {
        assertNotNull(FileModel.getModel());
    }

    @Test
    public void fileReadTest() {
        assertTrue(FileModel.getModel().readFileR("B.txt"));
        assertTrue(FileModel.getModel().readFileR("A.txt"));
    }

    @Test
    public void fileWriteTest() {

    }
}
