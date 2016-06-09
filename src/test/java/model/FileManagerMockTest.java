package model;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by ano on 2016. 5. 18..
 */

public class FileManagerMockTest {
    private static FileManager fileManager;
    private static FileModelInterface leftFileModelMock;
    private static FileModelInterface rightFileModelMock;

    //테스트를 위한 준비를 합니다
    @Before
    public void setUpTestMock() throws FileNotFoundException, LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException {

        fileManager = (FileManager)FileManager.getFileManagerInterface();
        fileManager.cancelCompare();//초기
        leftFileModelMock = EasyMock.createMock(FileModelInterface.class);
        rightFileModelMock = EasyMock.createMock(FileModelInterface.class);
        fileManager.setDependency(leftFileModelMock,rightFileModelMock);

    }

    //컴페어가 set되고 cancel되는지를 확인하는 테스트
    @Test
    public void setandcancelCompareTest() throws LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException{
        int[][] arr = {{0, 0, 0, 0, 0}, {0, 1, 1, 1, 1}, {0, 1, 2, 2, 2}, {0, 1, 2, 2, 3}, {0, 1, 2, 3, 3}};
        //L이 a b c d, R이 a b d c 일떄의 DP 테이블
        ArrayList<Line> testLineArrayListL = new ArrayList<>();
        ArrayList<Line> testLineArrayListR = new ArrayList<>();
        ArrayList<LineInterface> testLineArrayListMockL = new ArrayList<>();
        ArrayList<LineInterface> testLineArrayListMockR = new ArrayList<>();
        ArrayList<LineInterface> testLeftLineArrayList = new ArrayList<>();
        ArrayList<LineInterface> testRightLineArrayList = new ArrayList<>();
        ArrayList<Block> testBlockArrayList = new ArrayList<>();
        {
            testLeftLineArrayList.add(new Line("a"));
            testLeftLineArrayList.add(new Line("b"));
            testLeftLineArrayList.add(new Line("c"));
            testLeftLineArrayList.add(new Line("d"));
            testRightLineArrayList.add(new Line("a"));
            testRightLineArrayList.add(new Line("b"));
            testRightLineArrayList.add(new Line("d"));
            testRightLineArrayList.add(new Line("c"));
            testLineArrayListL.add(new Line("a", -1, false));
            testLineArrayListL.add(new Line("b", -1, false));
            testLineArrayListL.add(new Line("", 1, true));
            testLineArrayListL.add(new Line("c", -1, false));
            testLineArrayListL.add(new Line("d", 0, false));
            testLineArrayListMockL.add(new Line("a", -1, false));
            testLineArrayListMockL.add(new Line("b", -1, false));
            testLineArrayListMockL.add(new Line("", 1, true));
            testLineArrayListMockL.add(new Line("c", -1, false));
            testLineArrayListMockL.add(new Line("d", 0, false));
            testLineArrayListR.add(new Line("a", -1, false));
            testLineArrayListR.add(new Line("b", -1, false));
            testLineArrayListR.add(new Line("d", 1, false));
            testLineArrayListR.add(new Line("c", -1, false));
            testLineArrayListR.add(new Line("", 0, true));
            testLineArrayListMockR.add(new Line("a", -1, false));
            testLineArrayListMockR.add(new Line("b", -1, false));
            testLineArrayListMockR.add(new Line("d", 1, false));
            testLineArrayListMockR.add(new Line("c", -1, false));
            testLineArrayListMockR.add(new Line("", 0, true));
        }
        EasyMock.expect(leftFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testLeftLineArrayList.clone()).anyTimes();
        EasyMock.expect(rightFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testRightLineArrayList.clone()).anyTimes();
        rightFileModelMock.setCompareLineArrayList(testLineArrayListMockR);
        leftFileModelMock.setCompareLineArrayList(testLineArrayListMockL);
        EasyMock.expect(rightFileModelMock.getCompareLineArrayList()).andReturn((ArrayList<LineInterface>)testLineArrayListMockR);
        EasyMock.expect(leftFileModelMock.getCompareLineArrayList()).andReturn((ArrayList<LineInterface>)testLineArrayListMockL);
        EasyMock.expectLastCall();
        EasyMock.expect(rightFileModelMock.isFileExist()).andReturn(true);
        EasyMock.expect(leftFileModelMock.isFileExist()).andReturn(true);
        EasyMock.expect(leftFileModelMock.getEdited()).andReturn(false);
        EasyMock.expect(rightFileModelMock.getEdited()).andReturn(false);
        EasyMock.replay(leftFileModelMock);
        EasyMock.replay(rightFileModelMock);
        fileManager.setCompare();
        assertArrayEquals(arr, fileManager.getArrayLCS()); //테이블 검사
        assertTrue(fileManager.getComparing());
        testBlockArrayList.add(new Block(4, 5));
        testBlockArrayList.add(new Block(2, 3));
        assertEquals(testBlockArrayList.size(),Line.getBlockArray().size());//블럭 크기 같은지 검사
        for (int i = 0; i < testBlockArrayList.size(); i++) {
            assertEquals(testBlockArrayList.get(i).startLineNum, Line.getBlockArray().get(i).startLineNum);
            assertEquals(testBlockArrayList.get(i).endLineNum, Line.getBlockArray().get(i).endLineNum);//블럭 같게 생겼는지 검사
        }
        ArrayList<LineInterface> gettedLineArrayListR = fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Right);
        ArrayList<LineInterface> gettedLineArrayListL = fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left);

        for (int i = 0; i < testLineArrayListR.size(); i++) {
            assertTrue(testLineArrayListR.get(i).equals(gettedLineArrayListR.get(i)));
            assertTrue(testLineArrayListL.get(i).equals(gettedLineArrayListL.get(i)));
            //텍스트 어레이 같게 생겼는지 검사
        }
        fileManager.cancelCompare();
        assertTrue(!fileManager.getComparing());
    }

    @Test(expected = LeftEditorFileCanNotCompareException.class)
    public void setCompareTest_LeftFileisnotLoaded() throws LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException {
        EasyMock.expect(leftFileModelMock.isFileExist()).andReturn(false).anyTimes();
        EasyMock.expect(leftFileModelMock.getEdited()).andReturn(false).anyTimes();
        EasyMock.replay(leftFileModelMock);
        EasyMock.replay(rightFileModelMock);
        fileManager.setCompare();
    }
    @Test(expected = RightEditorFileCanNotCompareException.class)
    public void setCompareTest_RightFileisnotLoaded() throws LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException {
        EasyMock.expect(leftFileModelMock.isFileExist()).andReturn(true).anyTimes();
        EasyMock.expect(leftFileModelMock.getEdited()).andReturn(false).anyTimes();
        EasyMock.expect(rightFileModelMock.isFileExist()).andReturn(false).anyTimes();
        EasyMock.expect(rightFileModelMock.getEdited()).andReturn(false).anyTimes();
        EasyMock.replay(leftFileModelMock);
        EasyMock.replay(rightFileModelMock);
        fileManager.setCompare();
    }


    //clickLine함수가 제대로 작동하는지에 대한 테스트
    @Test
    public void clickLine()  throws LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException
    {        ArrayList<Line> testLineArrayListL = new ArrayList<>();
        ArrayList<Line> testLineArrayListR = new ArrayList<>();
        ArrayList<LineInterface> testLineArrayListMockL = new ArrayList<>();
        ArrayList<LineInterface> testLineArrayListMockR = new ArrayList<>();
        ArrayList<LineInterface> testLeftLineArrayList = new ArrayList<>();
        ArrayList<LineInterface> testRightLineArrayList = new ArrayList<>();

        ArrayList<Block> testBlockArrayList = new ArrayList<>();
        {
            testLeftLineArrayList.add(new Line("a"));
            testLeftLineArrayList.add(new Line("b"));
            testLeftLineArrayList.add(new Line("c"));
            testLeftLineArrayList.add(new Line("d"));
            testRightLineArrayList.add(new Line("a"));
            testRightLineArrayList.add(new Line("b"));
            testRightLineArrayList.add(new Line("d"));
            testRightLineArrayList.add(new Line("c"));
            testLineArrayListL.add(new Line("a", -1, false));
            testLineArrayListL.add(new Line("b", -1, false));
            testLineArrayListL.add(new Line("", 1, true));
            testLineArrayListL.add(new Line("c", -1, false));
            testLineArrayListL.add(new Line("d", 0, false));
            testLineArrayListMockL.add(new Line("a", -1, false));
            testLineArrayListMockL.add(new Line("b", -1, false));
            testLineArrayListMockL.add(new Line("", 1, true));
            testLineArrayListMockL.add(new Line("c", -1, false));
            testLineArrayListMockL.add(new Line("d", 0, false));
            testLineArrayListR.add(new Line("a", -1, false));
            testLineArrayListR.add(new Line("b", -1, false));
            testLineArrayListR.add(new Line("d", 1, false));
            testLineArrayListR.add(new Line("c", -1, false));
            testLineArrayListR.add(new Line("", 0, true));
            testLineArrayListMockR.add(new Line("a", -1, false));
            testLineArrayListMockR.add(new Line("b", -1, false));
            testLineArrayListMockR.add(new Line("d", 1, false));
            testLineArrayListMockR.add(new Line("c", -1, false));
            testLineArrayListMockR.add(new Line("", 0, true));
        }
        //EasyMock.expect(leftFileModelMock.getCompareLineArrayList()).andReturn(testLineArrayListMockL);
        //EasyMock.expect(rightFileModelMock.getCompareLineArrayList()).andReturn(testLineArrayListMockR);
        EasyMock.expect(leftFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testLeftLineArrayList.clone()).anyTimes();
        EasyMock.expect(rightFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testRightLineArrayList.clone()).anyTimes();
        rightFileModelMock.setCompareLineArrayList(testLineArrayListMockR);
        leftFileModelMock.setCompareLineArrayList(testLineArrayListMockL);
        EasyMock.expectLastCall();
        EasyMock.expect(rightFileModelMock.isFileExist()).andReturn(true);
        EasyMock.expect(leftFileModelMock.isFileExist()).andReturn(true);
        EasyMock.expect(leftFileModelMock.getEdited()).andReturn(false);
        EasyMock.expect(rightFileModelMock.getEdited()).andReturn(false);
        leftFileModelMock.clickLine(4);
        leftFileModelMock.updateHighlight();
        rightFileModelMock.updateHighlight();

        EasyMock.expect(leftFileModelMock.getCompareLineArrayList()).andReturn((ArrayList<LineInterface>)testLineArrayListMockL);
        EasyMock.replay(leftFileModelMock);
        EasyMock.replay(rightFileModelMock);
        fileManager.setCompare();
        testLineArrayListMockL.get(4).clickBlock();
        fileManager.clickLine(4);
        assertTrue(fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left).get(4).getHighlight() == Line.LineHighlight.selected);
    }
    //라인이 제대로 갱신되는지를 확인하는 테스트
    @Test
    public void updateLineArrayListTest(){
        ArrayList<LineInterface> testLeftLineArrayList = new ArrayList<>();
        String testString = "asdf\nrrr";
        testLeftLineArrayList.add(new Line("asdf"));
        testLeftLineArrayList.add(new Line("rrr"));
        leftFileModelMock.updateArrayList(testString);
        EasyMock.expect(leftFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testLeftLineArrayList.clone());
        //asyMock.expect(leftFileModelMock.toString()).andReturn("asdf\nrrr"); toString cannot be mocked @@ cannot be tested
        EasyMock.replay(leftFileModelMock);
        EasyMock.replay(rightFileModelMock);
        fileManager.updateLineArrayList(testString,FileManagerInterface.SideOfEditor.Left);
        ArrayList<LineInterface> actualLeftLineArrayList = fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left);
        assertEquals(testLeftLineArrayList.size(),actualLeftLineArrayList.size());
        //assertTrue(testString.equals(fileManager.getString(FileManagerInterface.SideOfEditor.Left)));
        for(int i = 0; i < testLeftLineArrayList.size(); i++){
            assertTrue(testLeftLineArrayList.get(i).equals(actualLeftLineArrayList.get(i)));
        }
    }
    @Test
    public void updateLineArrayListTest_updateNull(){
        ArrayList<LineInterface> testLeftLineArrayList = new ArrayList<>();
        String testString = null;
        testLeftLineArrayList.add(new Line(""));
        leftFileModelMock.updateArrayList(testString);
        EasyMock.expect(leftFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testLeftLineArrayList.clone());
        EasyMock.replay(leftFileModelMock);
        EasyMock.replay(rightFileModelMock);
        fileManager.updateLineArrayList(testString,FileManagerInterface.SideOfEditor.Left);
        ArrayList<LineInterface> actualLeftLineArrayList = fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left);
        assertEquals(testLeftLineArrayList.size(),actualLeftLineArrayList.size());
        for(int i = 0; i < testLeftLineArrayList.size(); i++){
            assertTrue(testLeftLineArrayList.get(i).equals(actualLeftLineArrayList.get(i)));
        }
    }
    //Line ArrayList를 받아오는지에 대한 테스트
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
        EasyMock.expect(leftFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testLeftLineArrayList.clone());
        EasyMock.expect(rightFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testRightLineArrayList.clone());
        EasyMock.replay(leftFileModelMock);
        EasyMock.replay(rightFileModelMock);
        System.out.println(fileManager.getComparing());
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
    public void getLineArrayListTest_notLoaded(){
        EasyMock.expect(leftFileModelMock.getLineArrayList()).andReturn(new ArrayList<>());
        EasyMock.replay(leftFileModelMock);
        EasyMock.replay(rightFileModelMock);
        assertTrue(fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left).equals(new ArrayList<>()));
    }
    //정상적으로 merge가 되는지에 대한 테스트
    @Test
    public void MergeTest()   throws LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException
    {
        ArrayList<Line> testLineArrayListL = new ArrayList<>();
        ArrayList<Line> testLineArrayListR = new ArrayList<>();
        ArrayList<LineInterface> testLineArrayListMockL = new ArrayList<>();
        ArrayList<LineInterface> testLineArrayListMockR = new ArrayList<>();
        ArrayList<LineInterface> testLeftLineArrayList = new ArrayList<>();
        ArrayList<LineInterface> testRightLineArrayList = new ArrayList<>();
        {
            testLeftLineArrayList.add(new Line("a"));
            testLeftLineArrayList.add(new Line("b"));
            testLeftLineArrayList.add(new Line("c"));
            testLeftLineArrayList.add(new Line("d"));
            testRightLineArrayList.add(new Line("a"));
            testRightLineArrayList.add(new Line("b"));
            testRightLineArrayList.add(new Line("d"));
            testRightLineArrayList.add(new Line("c"));
            testLineArrayListL.add(new Line("a", -1, false));
            testLineArrayListL.add(new Line("b", -1, false));
            testLineArrayListL.add(new Line("", 1, true));
            testLineArrayListL.add(new Line("c", -1, false));
            testLineArrayListL.add(new Line("d", 0, false));
            testLineArrayListMockL.add(new Line("a", -1, false));
            testLineArrayListMockL.add(new Line("b", -1, false));
            testLineArrayListMockL.add(new Line("", 1, true));
            testLineArrayListMockL.add(new Line("c", -1, false));
            testLineArrayListMockL.add(new Line("d", 0, false));
            testLineArrayListR.add(new Line("a", -1, false));
            testLineArrayListR.add(new Line("b", -1, false));
            testLineArrayListR.add(new Line("d", 1, false));
            testLineArrayListR.add(new Line("c", -1, false));
            testLineArrayListR.add(new Line("", 0, true));
            testLineArrayListMockR.add(new Line("a", -1, false));
            testLineArrayListMockR.add(new Line("b", -1, false));
            testLineArrayListMockR.add(new Line("d", 1, false));
            testLineArrayListMockR.add(new Line("c", -1, false));
            testLineArrayListMockR.add(new Line("", 0, true));
        }
        EasyMock.expect(leftFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testLeftLineArrayList.clone()).times(4);
        EasyMock.expect(rightFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testRightLineArrayList.clone()).times(4);
        rightFileModelMock.setCompareLineArrayList(testLineArrayListMockR);
        leftFileModelMock.setCompareLineArrayList(testLineArrayListMockL);
        EasyMock.expect(rightFileModelMock.getCompareLineArrayList()).andReturn(testLineArrayListMockR).anyTimes();
        EasyMock.expect(leftFileModelMock.getCompareLineArrayList()).andReturn(testLineArrayListMockL).anyTimes();
        EasyMock.expectLastCall();
        EasyMock.expect(rightFileModelMock.isFileExist()).andReturn(true);
        EasyMock.expect(leftFileModelMock.isFileExist()).andReturn(true);
        EasyMock.expect(leftFileModelMock.getEdited()).andReturn(false);
        EasyMock.expect(rightFileModelMock.getEdited()).andReturn(false);
        leftFileModelMock.clickLine(2);
        leftFileModelMock.updateHighlight();
        rightFileModelMock.updateHighlight();
        leftFileModelMock.clickLine(4);
        rightFileModelMock.setEdited(true);

        leftFileModelMock.updateHighlight();
        rightFileModelMock.updateHighlight();

        leftFileModelMock.updateArrayList("a\nb\nc\nd");
        rightFileModelMock.updateArrayList("a\nb\nc\nd");

        testLineArrayListL.clear();
        testLineArrayListR.clear();

        testLineArrayListL.add(new Line("a",-1,false));
        testLineArrayListL.add(new Line("b",-1,false));
        testLineArrayListL.add(new Line("c",-1,false));
        testLineArrayListL.add(new Line("d",-1,false));
        testLineArrayListR.add(new Line("a",-1,false));
        testLineArrayListR.add(new Line("b",-1,false));
        testLineArrayListR.add(new Line("c",-1,false));
        testLineArrayListR.add(new Line("d",-1,false));
        EasyMock.expect(leftFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testLineArrayListL.clone()).times(1);
        EasyMock.expect(rightFileModelMock.getLineArrayList()).andReturn((ArrayList<LineInterface>)testLineArrayListR.clone()).times(1);
        EasyMock.replay(leftFileModelMock);
        EasyMock.replay(rightFileModelMock);
        fileManager.setCompare();
        //[abcd ] [ab dc] 의 경우
        fileManager.clickLine(2);
        testLineArrayListMockL.get(4).clickBlock();
        testLineArrayListMockL.get(2).clickBlock();
        fileManager.clickLine(4);
        fileManager.merge(FileManagerInterface.SideOfEditor.Right);
        ArrayList<LineInterface> gettedLineArrayListR = fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Right);
        ArrayList<LineInterface> gettedLineArrayListL = fileManager.getLineArrayList(FileManagerInterface.SideOfEditor.Left);

        for (int i = 0; i < testLineArrayListL.size(); i++) {
            System.out.print(i);
            System.out.println(gettedLineArrayListL.get(i).getContent(true) + " Left ");
            assertTrue(testLineArrayListL.get(i).equals(gettedLineArrayListL.get(i)));
        }
        for (int i = 0; i < testLineArrayListR.size(); i++) {
            System.out.print(i);
            System.out.println(gettedLineArrayListR.get(i).getContent(true) + " Right");
            assertTrue(testLineArrayListR.get(i).equals(gettedLineArrayListR.get(i)));
        }
    }

    @After
    public void tearDown(){
        EasyMock.verify(leftFileModelMock);
        EasyMock.verify(rightFileModelMock);
        fileManager.setDependency(new FileModel(), new FileModel()); //싱글톤 패턴떄문에 이지목 코드가 남아있는 친구
    }



}