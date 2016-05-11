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
        FileModel.getModel().readFiles("A.txt", "B.txt");
        //assertArrayEquals(FileModel.fileA.);
        assertNotNull(FileModel.fileA);
        assertNotNull(FileModel.fileB);
    }

    @Test
    public void fileWriteTest() {

    }
}
