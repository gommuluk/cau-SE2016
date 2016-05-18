package model;

import java.util.ArrayList;

/**
 * Created by ano on 2016. 5. 13..
 */
public class FileManager {

    private static FileManager instance;
    private FileModel FileModelL = new FileModel();
    private FileModel FileModelR = new FileModel();
    private FileManager(){}

    /**
     * FileManager 객체를 반환합니다.
     * @return FileManager 양 쪽 Editor에 할당된 파일들을 관리하는 매니저
     */
    public static FileManager getFileManager() { // 1개의 객체를 유지하기 위한 싱글톤
        if(instance == null)
            instance = new FileManager();
        return instance;
    }

    /**
     * Left Editor에 할당된 File Model을 반환합니다.
     * @return FileModel 왼쪽 Editor에 할당된 FileModel
     */
    public FileModel getFileModelL()
    {
        return FileModelL;
    }

    /**
     * Right Editor에 할당된 File Model을 반환합니다.
     * @return FileModel 오른쪽 Editor에 할당된 FileModel
     */
    public FileModel getFileModelR()
    {
        return FileModelR;
    }
    private int[][] arrayLCS;
    public void runLCS()
    {
        arrayLCS = new int[FileModelL.getlLneArrayList().size() + 1][FileModelR.getlLneArrayList().size() + 1];//초기화
        buildArrayLCS(arrayLCS);
    }
    private int max(int a, int b)
    {
        if(a > b) return a;
        else return b;
    }

    private void buildArrayLCS(int[][] arr)
    {
        ArrayList<Line> leftArr = FileModelL.getlLneArrayList(); // width
        ArrayList<Line> rightArr = FileModelR.getlLneArrayList(); // height
        int width = arr.length;
        int height = arr[0].length;
        for(int i = 0; i <= width; i++)
        {
            for(int j = 0; j <= height ; j++)
            {
                if(i == 0 || j == 0) arr[i][j] = 0;
                else
                {
                    if(leftArr.get(i-1) == rightArr.get(j-1))
                    {
                        arr[i][j] = arr[i-1][j-1] + 1;
                    }
                    else
                    {
                        arr[i][j] = max(arr[i-1][j],arr[i][j-1]);
                    }
                }
            }
        }



    }

}
