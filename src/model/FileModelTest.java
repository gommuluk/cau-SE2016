package model;

import org.junit.Test;

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
        FileModel testFileModel = new FileModel();
        ArrayList<String> testArraylist = new ArrayList<String>();
        testArraylist.add("asdf\n");
        testArraylist.add("asdf");
        assertTrue(testFileModel.readFile("A.txt"));
        //assertEquals(testFileModel.getStringArrayList(),testArraylist);


    }

    @Test
    public void fileWriteTest() {
        FileModel testFileModel = new FileModel();
        //assertTrue(testFileModel.writeFile("AA.txt"));
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
        //assertEquals(testFileModel.getStringArrayList(),testArraylist);

    }
    @Test
    public void getStatusTest()
    {
        FileModel testFileModel = new FileModel();
        String testStatus = "Ready(No file is loaded)";
        assertEquals(testFileModel.getStatus(),testStatus);
        assertTrue(testFileModel.readFile("A.txt"));
        testStatus = "File Loaded Successfully";
        assertEquals(testFileModel.getStatus(),testStatus);
        //testFileModel.writeFile("AA.txt");
        testStatus = "File Written successfully";
        assertEquals(testFileModel.getStatus(),testStatus);
    }

}
