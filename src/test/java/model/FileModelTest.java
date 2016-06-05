package model;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Elliad on 2016-05-08.
 */
public class FileModelTest {
    private FileModel testFileModel;
    private ArrayList<String> testStringArrayList;
    @Before
    public void setupTest(){
        testFileModel = new FileModel();

    }
    @Test
    public void fileReadTest() throws FileNotFoundException, UnsupportedEncodingException{
        testFileModel.readFile(getClass().getResource("../AA.txt").getPath());
        ArrayList<String>
        testStringArrayList = new ArrayList<>();
        testStringArrayList.add("a");
        testStringArrayList.add("b");
        testStringArrayList.add("c");
        testStringArrayList.add("d");
        assertEquals(testFileModel.getLineArrayList().size(), testStringArrayList.size());
        for(int i = 0; i < testStringArrayList.size(); i++){
            assertTrue(testFileModel.getLineArrayList().get(i).getContent(true).equals(testStringArrayList.get(i)));
        }
    }
    @Test
    public void updateArrayListTest() {
        testFileModel.updateArrayList("z\nx\ny\ne");
        testStringArrayList = new ArrayList<>();
        testStringArrayList.add("z");
        testStringArrayList.add("x");
        testStringArrayList.add("y");
        testStringArrayList.add("e");
        for (int i = 0; i < testStringArrayList.size(); i++) {
            assertTrue(testFileModel.getLineArrayList().get(i).getContent(true).equals(testStringArrayList.get(i)));
        }
    }
    @Test
    public void isFileExistTest()throws FileNotFoundException, UnsupportedEncodingException{
        assertFalse(testFileModel.isFileExist());
        testFileModel.readFile(getClass().getResource("../AA.txt").getPath());
        assertTrue(testFileModel.isFileExist());
    }
    @Test
    public void editedTest() throws FileNotFoundException, UnsupportedEncodingException{
        testFileModel.readFile(getClass().getResource("../AA.txt").getPath());
        assertFalse(testFileModel.getEdited());
        testFileModel.setEdited(true);
        assertTrue(testFileModel.getEdited());
        testFileModel.writeFile();
        assertFalse(testFileModel.getEdited());


    }
    @Test
    public void fileWriteTest() throws FileNotFoundException, UnsupportedEncodingException{
        FileModel testFileModel = new FileModel();
        testFileModel.readFile(getClass().getResource("../AA.txt").getPath());
        testFileModel.writeFile();
        ArrayList<String> testArraylist = new ArrayList<>();
        try(Scanner in = new Scanner(new FileReader(getClass().getResource("../AA.txt").getPath())))
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
        assertEquals(testFileModel.getLineArrayList().size(), testArraylist.size());
        for(int i = 0 ; i < testArraylist.size();i++){
            if(i != testArraylist.size() -1)
            assertTrue(testFileModel.getLineArrayList().get(i).getContent(false).equals(testArraylist.get(i)));
            else
                assertTrue(testFileModel.getLineArrayList().get(i).getContent(true).equals(testArraylist.get(i)));
        }
    }

    @Test
    //미구현
    public void getStatusTest()
    {
        /*
        FileModel testFileModel = new FileModel();
        String testStatus = "Ready(No file is loaded)";
        //assertEquals(testFileModel.getStatus(), new ReadOnlyStringWrapper(testStatus));
        //assertTrue(testFileModel.readFile("A.txt"));
        //testStatus = "File Loaded Successfully";
        //assertEquals(testFileModel.getStatus(),new ReadOnlyStringWrapper(testStatus));
        //testFileModel.writeFile("AA.txt");
        testStatus = "File Written successfully";
        //assertEquals(testFileModel.getStatus(),new ReadOnlyStringWrapper(testStatus));
    */
    }

}