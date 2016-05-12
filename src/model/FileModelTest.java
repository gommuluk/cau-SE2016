package model;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

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
        String testString = "asdf\nasdf";
        assertTrue(FileModel.getModel().readFile("A.txt"));
        //System.out.print("Test : [" + FileModel.getModel().getStringL());
        assertArrayEquals(FileModel.getModel().getString().toCharArray(),testString.toCharArray());
    }

    @Test
    public void fileWriteTest() {
        assertTrue(FileModel.getModel().writeFile("AA.txt"));

        String testString = "asdf\nasdf";

        String savedString = "";
        try(Scanner in = new Scanner(new FileReader("AA.txt")))
        {
            savedString = in.useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertArrayEquals(savedString.toCharArray(),testString.toCharArray());



    }
}
