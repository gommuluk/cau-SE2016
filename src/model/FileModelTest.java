package model;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Elliad on 2016-05-08.
 */
public class FileModelTest {
    @Test
    public void fileReadTest() {
        ArrayList<String> testArraylist = new ArrayList<String>();
        testArraylist.add("asdf\n");
        testArraylist.add("asdf");
        assertTrue(FileModel.getModel().readFile("A.txt"));
        assertEquals(FileModel.getModel().getStringArrayList(),testArraylist);


    }

    @Test
    public void fileWriteTest() {
        assertTrue(FileModel.getModel().writeFile("AA.txt"));
        ArrayList<String> testArraylist = new ArrayList<String>();
        try(Scanner in = new Scanner(new FileReader("AA.txt")))
        {
            String tempString = "";
            while(in.hasNext()) {
                tempString = in.next(); //임시 텍스트에 개행을 제외한 한 줄을 불러온다
                if(in.hasNext()) tempString +="\n"; //다음 줄이 없을 때는 개행을 추가하지 않는다.
                testArraylist.add(tempString);
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        assertEquals(FileModel.getModel().getStringArrayList(),testArraylist);

    }
}
