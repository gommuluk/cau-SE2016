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
        String testR = "fdsa";
        String testL = "asdf";
        assertTrue(FileModel.getModel().readFileR("B.txt"));
        assertTrue(FileModel.getModel().readFileL("A.txt"));

        assertArrayEquals(FileModel.getModel().getStringR().toCharArray(),testR.toCharArray());
        assertArrayEquals(FileModel.getModel().getStringL().toCharArray(),testL.toCharArray());

    }

    @Test
    public void fileWriteTest() {

    }
}
