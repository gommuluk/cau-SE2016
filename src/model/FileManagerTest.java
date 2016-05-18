package model;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;
/**
 * Created by ano on 2016. 5. 18..
 */
public class FileManagerTest {
    static FileManager fileManager;
    @BeforeClass
    public static void setupTest ()
    {
        fileManager = FileManager.getFileManager();
        fileManager.getFileModelL().readFile("AA.txt");
        fileManager.getFileModelR().readFile("BB.txt");//파일을 로드해둔다


    }

    @Test
    public void buildArrayLCSTest() {
        int[][] arr = {{0,0,0,0,0},{0,1,1,1,1},{0,1,2,2,2},{0,1,2,2,3},{0,1,2,3,3}};
        //L이 a b c d, R이 a b d c 일떄의 DP 테이블

        fileManager.runLCS();
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++)
        System.out.println(fileManager.getArrayLCS()[i][j] + " ");
        assertArrayEquals(arr, fileManager.getArrayLCS());


    }
}
