package model;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by ano on 2016. 5. 18..
 */

//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileManagerTest {
    private static FileManager fileManager;


    @Before
    public void setupTest () throws FileNotFoundException, UnsupportedEncodingException, LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException
    {
        fileManager = (FileManager)FileManager.getFileManagerInterface();
        fileManager.setDependency(new FileModel(), new FileModel()); //싱글톤 패턴떄문에 이지목 코드가 남아있는 친구
        fileManager.resetModel(FileManagerInterface.SideOfEditor.Left);
        fileManager.resetModel(FileManagerInterface.SideOfEditor.Right);
        fileManager.loadFile( getClass().getResource("../AA.txt").getPath(), FileManagerInterface.SideOfEditor.Left);//a,b,c,d
        fileManager.loadFile( getClass().getResource("../BB.txt").getPath(), FileManagerInterface.SideOfEditor.Right);//a,b,d,c
        System.out.println("start");
    }

    @Test
    public void setCompareTest() throws LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException{
        fileManager.setCompare();
        assertTrue(fileManager.getComparing());

        int[][] arr = {{0, 0, 0, 0, 0}, {0, 1, 1, 1, 1}, {0, 1, 2, 2, 2}, {0, 1, 2, 2, 3}, {0, 1, 2, 3, 3}};

        //L이 a b c d, R이 a b d c 일떄의 DP 테이블
        assertArrayEquals(arr, fileManager.getArrayLCS()); //테이블 검사

        ArrayList<Line> testLineArrayListL = new ArrayList<Line>();
        ArrayList<Line> testLineArrayListR = new ArrayList<Line>();
        ArrayList<Block> testBlockArrayList = new ArrayList<Block>();

        testBlockArrayList.add(new Block(4, 5));
        testBlockArrayList.add(new Block(2, 3));
        assertEquals(testBlockArrayList.size(),Line.getBlockArray().size());//블럭 크기 같은지 검사
        for (int i = 0; i < testBlockArrayList.size(); i++) {
            assertEquals(testBlockArrayList.get(i).startLineNum, Line.getBlockArray().get(i).startLineNum);
            assertEquals(testBlockArrayList.get(i).endLineNum, Line.getBlockArray().get(i).endLineNum);//블럭 같게 생겼는지 검사
        }
        testLineArrayListL.add(new Line("a",-1,false));
        testLineArrayListL.add(new Line("b",-1,false));
        testLineArrayListL.add(new Line("",1,true));
        testLineArrayListL.add(new Line("c",-1,false));
        testLineArrayListL.add(new Line("d",0,false));
        testLineArrayListR.add(new Line("a",-1,false));
        testLineArrayListR.add(new Line("b",-1,false));
        testLineArrayListR.add(new Line("d",1,false));
        testLineArrayListR.add(new Line("c",-1,false));
        testLineArrayListR.add(new Line("",0,true));
        for (int i = 0; i < testLineArrayListR.size(); i++) {
            assertTrue(testLineArrayListR.get(i).equals(fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Right).get(i)));
            assertTrue(testLineArrayListL.get(i).equals(fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left).get(i)));
            //텍스트 어레이 같게 생겼는지 검사
        }

    }
    @Test
    public void cancelCompareTest() throws LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException{
        fileManager.setCompare();
        fileManager.cancelCompare();
        assertTrue(false == fileManager.getComparing());

    }
    @Test
    public void clickLine()  throws LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException
    {
        fileManager.setCompare();
        fileManager.clickLine(4);
        assertTrue(fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left).get(4).getHighlight() == Line.LineHighlight.selected);
    }

    @Test
    public void updateLineArrayListTest(){
        ArrayList<LineInterface> testLeftLineArrayList = new ArrayList<>();
        String testString = "asdf\nrrr";
        fileManager.updateLineArrayList(testString,FileManagerInterface.SideOfEditor.Left);
        testLeftLineArrayList.add(new Line("asdf"));
        testLeftLineArrayList.add(new Line("rrr"));
        ArrayList<LineInterface> actualLeftLineArrayList = fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left);
        assertEquals(testLeftLineArrayList.size(),actualLeftLineArrayList.size());
        assertTrue(testString.equals(fileManager.getString(FileManagerInterface.SideOfEditor.Left)));
        for(int i = 0; i < testLeftLineArrayList.size(); i++){
            assertTrue(testLeftLineArrayList.get(i).equals(actualLeftLineArrayList.get(i)));
        }
    }
    @Test
    public void getLineArrayListTest(){
        ArrayList<LineInterface> testLeftLineArrayList = new ArrayList<>();
        ArrayList<LineInterface> testRightLineArrayList = new ArrayList<>();
        testLeftLineArrayList.add(new Line("a"));
        testLeftLineArrayList.add(new Line("b"));
        testLeftLineArrayList.add(new Line("c"));
        testLeftLineArrayList.add(new Line("d"));
        testRightLineArrayList.add(new Line("a"));
        testRightLineArrayList.add(new Line("b"));
        testRightLineArrayList.add(new Line("d"));
        testRightLineArrayList.add(new Line("c"));
        ArrayList<LineInterface> actualLeftLineArrayList = fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left);
        ArrayList<LineInterface> actualRightLineArrayList = fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Right);
        assertEquals(testLeftLineArrayList.size(),actualLeftLineArrayList.size());
        assertEquals(testRightLineArrayList.size(),actualRightLineArrayList.size());
        for(int i = 0; i < testLeftLineArrayList.size(); i++){
            assertTrue(testLeftLineArrayList.get(i).equals(actualLeftLineArrayList.get(i)));
            assertTrue(testRightLineArrayList.get(i).equals(actualRightLineArrayList.get(i)));

       }
    }

    @Test
    public void MergeTest()   throws LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException
    {
        fileManager.setCompare();
        //[abcd ] [ab dc] 의 경우
        ArrayList<Line> testLineArrayListL = new ArrayList<Line>();
        ArrayList<Line> testLineArrayListR = new ArrayList<Line>();
        testLineArrayListL.add(new Line("a",-1,false));
        testLineArrayListL.add(new Line("b",-1,false));
        testLineArrayListL.add(new Line("c",-1,false));
        testLineArrayListL.add(new Line("d",-1,false));
        testLineArrayListR.add(new Line("a",-1,false));
        testLineArrayListR.add(new Line("b",-1,false));
        testLineArrayListR.add(new Line("c",-1,false));
        testLineArrayListR.add(new Line("d",-1,false));
        fileManager.clickLine(2);
        fileManager.clickLine(4);
        fileManager.merge(FileManagerInterface.SideOfEditor.Right);
        for (int i = 0; i < testLineArrayListL.size(); i++) {
            System.out.print(i);
            System.out.println(fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left).get(i).getContent(true) + " Left ");
            assertTrue(testLineArrayListL.get(i).equals(fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left).get(i)));
        }
        for (int i = 0; i < testLineArrayListR.size(); i++) {
            System.out.print(i);
            System.out.println(fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Right).get(i).getContent(true) + " Right");
            assertTrue(testLineArrayListR.get(i).equals(fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Right).get(i)));
        }
    }
    @Test
    public void resetModelTest(){
        fileManager.resetModel(FileManagerInterface.SideOfEditor.Left);
        assertNull(fileManager.getFilePath(FileManagerInterface.SideOfEditor.Left));
        assertTrue(new ArrayList<LineInterface>().equals(fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left)));
        assertFalse(fileManager.getEdited(FileManagerInterface.SideOfEditor.Left));
        assertFalse(fileManager.getComparing());
    }

    @After
    public void tearDown(){
        EasyMock.verify();
    }



}