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
        String testR = "fdsa";
        String testL = "asdf\nasdf";
        assertTrue(FileModel.getModel().readFileR("B.txt"));
        assertTrue(FileModel.getModel().readFileL("A.txt"));
        //System.out.print("Test : [" + FileModel.getModel().getStringL());
        //TODO 여기 테스트 수정 필요
        //assertArrayEquals(FileModel.getModel().getStringR().,testR.toCharArray());
        //assertArrayEquals(FileModel.getModel().getStringL().toCharArray(),testL.toCharArray());



    }

    @Test
    public void fileWriteTest() {
        assertTrue(FileModel.getModel().writeFileL("AA.txt"));
        assertTrue(FileModel.getModel().writeFileR("BB.txt"));
        String testR = "fdsa";
        String testL = "asdf\nasdf";
        String SavedR = "";
        String SavedL = "";
        try(Scanner in = new Scanner(new FileReader("AA.txt")))
        {
            SavedL = in.useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try(Scanner in = new Scanner(new FileReader("BB.txt")))
        {
            SavedR = in.useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertArrayEquals(SavedR.toCharArray(),testR.toCharArray());
        assertArrayEquals(SavedL.toCharArray(),testL.toCharArray());


    }
}
