package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ano on 2016. 5. 13..
 */
public class FileManager implements FileManagerInterface{

    private static FileManagerInterface instance;
    private FileModel FileModelL = new FileModel();
    private FileModel FileModelR = new FileModel();
    private ArrayList<Block> blockArrayList;

    private FileManager() {
    }

    /**
     * FileManager 객체를 반환합니다.
     *
     * @return FileManager 양 쪽 Editor에 할당된 파일들을 관리하는 매니저
     */
    public static FileManagerInterface getFileManagerInterface() { // 1개의 객체를 유지하기 위한 싱글톤
        if (instance == null)
            instance = new FileManager();
        return instance;
    }

    /**
     * Left Editor에 할당된 File Model을 반환합니다.
     *
     * @return FileModel 왼쪽 Editor에 할당된 FileModel
     */
    public FileModel getFileModelL() {
        return FileModelL;
    }

    /**
     * Right Editor에 할당된 File Model을 반환합니다.
     *
     * @return FileModel 오른쪽 Editor에 할당된 FileModel
     */
    public FileModel getFileModelR() {
        return FileModelR;
    }

    private int[][] arrayLCS;

    public void compare() {

        arrayLCS = new int[FileModelL.getLineArrayList().size() + 1][FileModelR.getLineArrayList().size() + 1];//LCS 배열의 초기화
        buildArrayLCS();// 배열 구성
        backTrackingLCS(); //diff구현 및 compare array저장

    }

    private int max(int a, int b) {
        if (a > b) return a;
        else return b;
    }

    private void buildArrayLCS() {//LCS를 위한 행렬을 구성한다
        int[][] arr = arrayLCS;
        ArrayList<Line> leftArr = FileModelL.getLineArrayList(); // width
        ArrayList<Line> rightArr = FileModelR.getLineArrayList(); // height

        int width = arr.length;
        int height = arr[0].length;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                if (i == 0 || j == 0) arr[i][j] = 0;
                else {
                    //System.out.println(leftArr.get(i - 1).content.compareTo(rightArr.get(j - 1).content));
                    if (leftArr.get(i - 1).getLine(true).compareTo(rightArr.get(j - 1).getLine(true)) == 0) {//개행 없이 비교해야되기떄문에 getLine에 true를 넘겨줘서 받아옴
                        arr[i][j] = arr[i - 1][j - 1] + 1;
                    } else {
                        arr[i][j] = max(arr[i - 1][j], arr[i][j - 1]);
                    }
                }
            }
        }


    }

    private void backTrackingLCS()//LCS 행렬을 이용해서 DIFF를 한다
    {
        int[][] arr = arrayLCS;
        ArrayList<Line> leftArr = FileModelL.getLineArrayList(); // width
        ArrayList<Line> rightArr = FileModelR.getLineArrayList(); // height
        ArrayList<Line> cLineArrayListL = new ArrayList<Line>();
        ArrayList<Line> cLineArrayListR = new ArrayList<Line>();
        blockArrayList = new ArrayList<Block>();//블럭 어레이리스트를 새로 만든다.
        Line.setBlockArray(blockArrayList);
        int i = FileModelL.getLineArrayList().size();
        int j = FileModelR.getLineArrayList().size();//끝점부터 시작한다.
        int startLineNum = 0; //블럭 시작줄
        int endLineNum = 0;//블럭 끝나는 줄
        int numBlock = 0; //현재 만든 블럭의 수
        Block tempBlock = null;

        int count = i + j - arr[i][j];//공백이 삽입 된 결과 총 라인의 길이가 되는 것
        //백트래킹 초기화작업 끝
        //백트래킹 시작
        while(true)
        {
            if(i == 0 && j == 0) {
                if(tempBlock != null)//마지막 블럭을 안넣었으면 넣어준다
                {
                    endLineNum = count;
                    tempBlock.setLineNum(startLineNum,endLineNum);
                    blockArrayList.add(tempBlock);
                    tempBlock = null;
                }
                break;
            }//비교끝
            if(i == 0) //위로 올라간다
            {
                cLineArrayListL.add(new Line("",numBlock,true));
                cLineArrayListR.add(new Line(rightArr.get(j-1).getLine(true),numBlock,false));
                j--;
                if(tempBlock == null)
                {
                    startLineNum = count;
                    tempBlock = new Block();//새 블럭을 만들어줌
                }
            }
            if(j == 0) //왼쪽으로 이동한다
            {
                cLineArrayListL.add(new Line(leftArr.get(i-1).getLine(true),numBlock,false));
                cLineArrayListR.add(new Line("",numBlock,true));
                i--;
                if(tempBlock == null)
                {
                    endLineNum = count;
                    tempBlock = new Block();//새 블럭을 만들어줌
                }
            }
            if(leftArr.get(i - 1).getLine(true).compareTo(rightArr.get(j - 1).getLine(true)) == 0)//같으면 대각선 위로 간다. 블럭 갱신이 일어남
            {
                cLineArrayListL.add(new Line(leftArr.get(i-1).getLine(true),-1,false));
                cLineArrayListR.add(new Line(rightArr.get(j-1).getLine(true),-1,false));//블럭에 속하지 않음
                i--;j--;
                if(tempBlock != null)//블럭이 있으니 반영을 해주지 않으면 안되잖아?
                {
                    startLineNum = count;
                    tempBlock.setLineNum(startLineNum,endLineNum);
                    blockArrayList.add(tempBlock);
                    tempBlock = null;
                    numBlock++;
                }

            }
            else if (arr[i][j-1] > arr[i-1][j])//위쪽방향이 더 클 경우 위쪽방향으로 향한다
            {
                cLineArrayListL.add(new Line("",numBlock,true));
                cLineArrayListR.add(new Line(rightArr.get(j-1).getLine(true),numBlock,false));
                j--;
                if(tempBlock == null)
                {
                    endLineNum = count;
                    tempBlock = new Block();//새 블럭을 만들어줌
                }
            }
            else
            {
                cLineArrayListL.add(new Line(leftArr.get(i-1).getLine(true),numBlock,false));
                cLineArrayListR.add(new Line("",numBlock,true));
                i--;
                if(tempBlock == null)
                {
                    endLineNum = count;
                    tempBlock = new Block();//새 블럭을 만들어줌
                }
            }
            count--;//줄 수 하나 추가해줌

        }
        //백트레킹으로 block하고 line이 잘 완성됬을 예정
        Collections.reverse(cLineArrayListL);//라인들은 반대로 저장됨
        Collections.reverse(cLineArrayListR);
        FileModelL.setCompare(cLineArrayListL);//변경된 arrayList를 넘겨줌
        FileModelR.setCompare(cLineArrayListR);
    }


    public int[][] getArrayLCS() {
        return arrayLCS.clone();
    }
    private void resetModel(FileManagerInterface.SideOfEditor sideOfEditor) {
        if(sideOfEditor == FileManagerInterface.SideOfEditor.Left) {
            this.FileModelL = new FileModel();
        }
        else this.FileModelR = new FileModel();
    }


    //TODO 아래의 Override func 구현 필요

    @Override
    public ArrayList<LineInterface> getLineArrayList(SideOfEditor side) {
        return null;
    }

    @Override
    public boolean SaveFile(String newContents, String filePath, SideOfEditor side) {
        return false;
    }

    @Override
    public boolean SaveFile(String newContents, SideOfEditor side) {
        return false;
    }

    @Override
    public ArrayList<LineInterface> LoadNewFile(String filePath, SideOfEditor side) {
        return null;
    }

    @Override
    public boolean setCompare() {
        return false;
    }

    @Override
    public void cancelCompare() {

    }

    @Override
    public void clickLine(int lineNum) {

    }

    @Override
    public void merge(SideOfEditor toSide) {

    }
}
