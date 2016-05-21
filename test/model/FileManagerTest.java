package model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by ano on 2016. 5. 18..
 */
public class FileManagerTest {
    static FileManager fileManager;
    @BeforeClass
    public static void setupTest () throws FileNotFoundException, UnsupportedEncodingException
    {
        //fileManager = FileManager.getFileManager();
        fileManager.getFileModelL().readFile("AA.txt");
        fileManager.getFileModelR().readFile("BB.txt");//파일을 로드해둔다
        //fileManager.compare();

    }

    @Test
    public void buildArrayLCSTest() {
        int[][] arr = {{0, 0, 0, 0, 0}, {0, 1, 1, 1, 1}, {0, 1, 2, 2, 2}, {0, 1, 2, 2, 3}, {0, 1, 2, 3, 3}};


        //L이 a b c d, R이 a b d c 일떄의 DP 테이블
        assertArrayEquals(arr, fileManager.getArrayLCS()); //테이블 검사
    }
    @Test
    public void backTrackingLCSTest(){
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
            //assertEquals(testLineArrayListR.get(i).getLine(true),fileManager.getFileModelR().getLineArrayList().get(i).getLine(true));
            assertEquals(testLineArrayListR.get(i).getBlockIndex(),fileManager.getFileModelR().getLineArrayList().get(i).getBlockIndex());
            assertEquals(testLineArrayListR.get(i).getIsWhiteSpace(),fileManager.getFileModelR().getLineArrayList().get(i).getIsWhiteSpace());
            //assertEquals(testLineArrayListL.get(i).getLine(true),fileManager.getFileModelL().getLineArrayList().get(i).getLine(true));
            assertEquals(testLineArrayListL.get(i).getBlockIndex(),fileManager.getFileModelL().getLineArrayList().get(i).getBlockIndex());
            assertEquals(testLineArrayListL.get(i).getIsWhiteSpace(),fileManager.getFileModelL().getLineArrayList().get(i).getIsWhiteSpace());
            //텍스트 어레이 같게 생겼는지 검사

        }

    }


}
