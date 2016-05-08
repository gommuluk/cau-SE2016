package model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Elliad on 2016-05-08.
 */
public class ModelTest {
    @Test
    public void singletonTest() {
        assertNotNull(Model.getModel());
    }

    @Test
    public void fileReadTest() {
        Model.getModel().readFiles("A.txt", "B.txt");
        assertNotNull(Model.fileA);
        assertNotNull(Model.fileB);
    }

    @Test
    public void fileWriteTest() {

    }
}
