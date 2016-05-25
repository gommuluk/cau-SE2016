package model;

import com.sun.media.sound.RIFFInvalidDataException;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.geometry.Side;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ano on 2016. 5. 13..
 */
public class FileManager implements FileManagerInterface {

    private boolean isComparing;
    private static FileManagerInterface instance;
    private FileModelInterface fileModelL;
    private FileModelInterface fileModelR;
    private ArrayList<Block> blockArrayList;
    private int[][] arrayLCS;


    private FileManager() {
        fileModelL = new FileModel();
        fileModelR = new FileModel();
    }

    /**
     * FileManager 객체를 반환합니다.
     *
     * @return FileManager 양 쪽 Editor에 할당된 파일들을 관리하는 매니저
     */
    public static FileManagerInterface getFileManagerInterface() { // 1개의 객체를 유지하기 위한 싱글톤
        if (instance == null) instance = new FileManager();
        return instance;
    }

    public void setFileModelL(FileModel L) //@@for mock testing by using Easymock
    {
        fileModelL = L;
    }

    public void setFileModelR(FileModel R) //@@for mock testing by using Easymock
    {
        fileModelR = R;
    }

    /**
     * Left Editor에 할당된 File Model을 반환합니다.
     *
     * @return FileModel 왼쪽 Editor에 할당된 FileModel
     */
    public FileModelInterface getFileModelL() {
        return fileModelL;
    }

    /**
     * Right Editor에 할당된 File Model을 반환합니다.
     *
     * @return FileModel 오른쪽 Editor에 할당된 FileModel
     */
    public FileModelInterface getFileModelR() {
        return fileModelR;
    }


    //FileManager Interface 구현 시작
    @Override
    public ArrayList<LineInterface> getLineArrayList(FileManagerInterface.SideOfEditor side) {
        if (side == SideOfEditor.Left) {
            if (isComparing) return fileModelL.getCompareLineArrayList();
            else return fileModelL.getLineArrayList();
        } else {
            if (isComparing) return fileModelR.getCompareLineArrayList();
            else return fileModelR.getLineArrayList();
        }
    }

    @Override
    public void updateLineArrayList(String content, SideOfEditor side) {
        if (side == SideOfEditor.Left) fileModelL.updateArrayList(content);
        else fileModelR.updateArrayList(content);
    }

    @Override
    public void saveFile(String content, FileManagerInterface.SideOfEditor side) throws FileNotFoundException, SecurityException {
        if (side == SideOfEditor.Left) {
            fileModelL.updateArrayList(content);
            fileModelL.writeFile();
        } else {
            fileModelR.updateArrayList(content);
            fileModelR.writeFile();
        }
    }

    @Override
    public void saveFile(String content, String filepath, FileManagerInterface.SideOfEditor side) throws FileNotFoundException, SecurityException {
        if (side == SideOfEditor.Left) {
            fileModelL.updateArrayList(content);
            fileModelL.writeFile(filepath);
        } else {
            fileModelR.updateArrayList(content);
            fileModelR.writeFile(filepath);
        }
    }

    @Override
    public ArrayList<LineInterface> loadFile(String filepath, FileManagerInterface.SideOfEditor side) throws FileNotFoundException, UnsupportedEncodingException {
        if (side == SideOfEditor.Left) {
            fileModelL.readFile(filepath);
            return fileModelL.getLineArrayList();
        } else {
            fileModelR.readFile(filepath);
            return fileModelR.getLineArrayList();
        }
    }

    @Override
    public void setCompare() throws LeftEditorFileNotFoundException, RightEditorFileNotFoundException {
        if (!fileModelL.isFileExist()) throw new LeftEditorFileNotFoundException();
        if (!fileModelR.isFileExist()) throw new RightEditorFileNotFoundException();
        arrayLCS = new int[fileModelL.getLineArrayList().size() + 1][fileModelR.getLineArrayList().size() + 1];//LCS 배열의 초기화
        buildArrayLCS();// 배열 구성
        backTrackingLCS(); //diff구현 및 compare array저장
        isComparing = true;
    }

    @Override
    public void cancelCompare() {
        isComparing = false;
    }

    @Override
    public void clickLine(int lineNum) {//미구현
        if (isComparing) fileModelL.clickLine(lineNum);//왼쪽이든 오른쪽이든 상관없음

    }

    @Override
    public boolean merge(FileManagerInterface.SideOfEditor toSide) {
        if (!isComparing) return false;
        FileModelInterface fromFileManager;
        FileModelInterface toFileManager;
        if (toSide == SideOfEditor.Left) {
            toFileManager = fileModelL;
            fromFileManager = fileModelR;
        } else {
            fromFileManager = fileModelL;
            toFileManager = fileModelR;
        }

        for (Block b : blockArrayList) {

            int count = b.endLineNum - b.startLineNum;//지워야 될 블럭의 갯수
            System.out.println("count" + count);
            int insertNum = b.startLineNum; //이 줄에 넣을거임
            if (b.getSelected()) {
                for (int i = 0; i < count; i++) {
                    toFileManager.getCompareLineArrayList().remove(insertNum);//일단 지워줌
                }
                for (int i = 0; i < count; i++) {
                    LineInterface tempL = fromFileManager.getCompareLineArrayList().get(insertNum);
                    if (tempL.getIsWhiteSpace()) fromFileManager.getCompareLineArrayList().remove(insertNum);
                    else {
                        LineInterface insertL = new Line(tempL.getContent(true));
                        toFileManager.getCompareLineArrayList().add(insertNum, insertL);
                        insertNum++;
                    }
                }
            } else {
                for (int i = 0; i < count; i++) {
                    LineInterface tempL = fromFileManager.getCompareLineArrayList().get(insertNum);
                    if (tempL.getIsWhiteSpace()) fromFileManager.getCompareLineArrayList().remove(insertNum);
                    else {
                        insertNum++;
                    }
                }
                insertNum = b.startLineNum;
                for (int i = 0; i < count; i++) {
                    LineInterface tempR = toFileManager.getCompareLineArrayList().get(insertNum);
                    if (tempR.getIsWhiteSpace()) toFileManager.getCompareLineArrayList().remove(insertNum);
                    else {
                        insertNum++;
                    }

                }
            }


        }


        //이제 컴페어 어레이를 리스트 어레이로 갱신해주면 됨


        String ret = "";
        for (LineInterface s : toFileManager.getCompareLineArrayList())
            ret += s.getContent(false);
        ret = ret.substring(0, ret.length() - 1);//맨 마지막의 개행 제거
        toFileManager.updateArrayList(ret);
        ret = "";
        for (LineInterface s : fromFileManager.getCompareLineArrayList())
            ret += s.getContent(false);
        ret = ret.substring(0, ret.length() - 1);//맨 마지막의 개행 제거
        fromFileManager.updateArrayList(ret);
        cancelCompare();
        return true;
    }

    //FileManager Interface 구현 끝
    @Override
    public String getString(FileManagerInterface.SideOfEditor side) {
        if (side == SideOfEditor.Left)
            return fileModelL.toString();
        else return fileModelR.toString();
    }

    public String getFilePath(SideOfEditor side) {
        if (side == SideOfEditor.Left) return getFileModelL().getFilePath();
        else return getFileModelR().getFilePath();
    }

    @Override
    public ReadOnlyStringProperty getStatus(SideOfEditor side) {
        return side == SideOfEditor.Left ? fileModelL.getStatus() : fileModelR.getStatus();
    }

    private int max(int a, int b) {
        if (a > b) return a;
        else return b;
    }

    private void buildArrayLCS() {//LCS를 위한 행렬을 구성한다
        int[][] arr = arrayLCS;
        ArrayList<LineInterface> leftArr = fileModelL.getLineArrayList(); // width
        ArrayList<LineInterface> rightArr = fileModelR.getLineArrayList(); // height

        int width = arr.length;
        int height = arr[0].length;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                if (i == 0 || j == 0) arr[i][j] = 0;
                else {
                    //System.out.println(leftArr.get(i - 1).content.compareTo(rightArr.get(j - 1).content));
                    if (leftArr.get(i - 1).getContent(true).compareTo(rightArr.get(j - 1).getContent(true)) == 0) {//개행 없이 비교해야되기떄문에 getLine에 true를 넘겨줘서 받아옴
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
        ArrayList<LineInterface> leftArr = fileModelL.getLineArrayList(); // width
        ArrayList<LineInterface> rightArr = fileModelR.getLineArrayList(); // height
        ArrayList<LineInterface> cLineArrayListL = new ArrayList<LineInterface>();
        ArrayList<LineInterface> cLineArrayListR = new ArrayList<LineInterface>();
        blockArrayList = new ArrayList<Block>();//블럭 어레이리스트를 새로 만든다.
        Line.setBlockArrayList(blockArrayList);
        int i = fileModelL.getLineArrayList().size();
        int j = fileModelR.getLineArrayList().size();//끝점부터 시작한다.
        int startLineNum = 0; //블럭 시작줄
        int endLineNum = 0;//블럭 끝나는 줄
        int numBlock = 0; //현재 만든 블럭의 수
        Block tempBlock = null;

        int count = i + j - arr[i][j];//공백이 삽입 된 결과 총 라인의 길이가 되는 것
        //백트래킹 초기화작업 끝
        //백트래킹 시작
        while (true) {
            if (i == 0 && j == 0) {
                if (tempBlock != null)//마지막 블럭을 안넣었으면 넣어준다
                {
                    endLineNum = count;
                    tempBlock.setLineNum(startLineNum, endLineNum);
                    blockArrayList.add(tempBlock);
                    tempBlock = null;
                }
                break;
            }//비교끝
            else if (i == 0) //위로 올라간다
            {
                cLineArrayListL.add(new Line("", numBlock, true));
                cLineArrayListR.add(new Line(rightArr.get(j - 1).getContent(true), numBlock, false));
                j--;
                if (tempBlock == null) {
                    startLineNum = count;
                    tempBlock = new Block();//새 블럭을 만들어줌
                }
            }
            else if (j == 0) //왼쪽으로 이동한다
            {
                cLineArrayListL.add(new Line(leftArr.get(i - 1).getContent(true), numBlock, false));
                cLineArrayListR.add(new Line("", numBlock, true));
                i--;
                if (tempBlock == null) {
                    endLineNum = count;
                    tempBlock = new Block();//새 블럭을 만들어줌
                }
            }
            else if (leftArr.get(i - 1).getContent(true).compareTo(rightArr.get(j - 1).getContent(true)) == 0)//같으면 대각선 위로 간다. 블럭 갱신이 일어남
            {
                cLineArrayListL.add(new Line(leftArr.get(i - 1).getContent(true), -1, false));
                cLineArrayListR.add(new Line(rightArr.get(j - 1).getContent(true), -1, false));//블럭에 속하지 않음
                i--;
                j--;
                if (tempBlock != null)//블럭이 있으니 반영을 해주지 않으면 안되잖아?
                {
                    startLineNum = count;
                    tempBlock.setLineNum(startLineNum, endLineNum);
                    blockArrayList.add(tempBlock);
                    tempBlock = null;
                    numBlock++;
                }

            } else if (arr[i][j - 1] > arr[i - 1][j])//위쪽방향이 더 클 경우 위쪽방향으로 향한다
            {
                cLineArrayListL.add(new Line("", numBlock, true));
                cLineArrayListR.add(new Line(rightArr.get(j - 1).getContent(true), numBlock, false));
                j--;
                if (tempBlock == null) {
                    endLineNum = count;
                    tempBlock = new Block();//새 블럭을 만들어줌
                }
            } else {
                cLineArrayListL.add(new Line(leftArr.get(i - 1).getContent(true), numBlock, false));
                cLineArrayListR.add(new Line("", numBlock, true));
                i--;
                if (tempBlock == null) {
                    endLineNum = count;
                    tempBlock = new Block();//새 블럭을 만들어줌
                }
            }
            count--;//줄 수 하나 추가해줌

        }
        //백트레킹으로 block하고 line이 잘 완성됬을 예정
        Collections.reverse(cLineArrayListL);//라인들은 반대로 저장됨
        Collections.reverse(cLineArrayListR);
        fileModelL.setCompareLineArrayList(cLineArrayListL);
        fileModelR.setCompareLineArrayList(cLineArrayListR);
    }

    @Override
    public int[][] getArrayLCS() {
        return arrayLCS.clone();
    } //@@for test of jUnit

    @Override
    public ReadOnlyStringProperty filePathProperty(SideOfEditor side) {
        if( side == SideOfEditor.Left ) System.out.println("left!");
        return side == SideOfEditor.Left ? fileModelL.filePathProperty() : fileModelR.filePathProperty();
    }

    @Override
    public boolean getComparing()
    {
        return isComparing;
    }
    @Override
    public void setEdited(SideOfEditor side, boolean value) {
        if(side == SideOfEditor.Left){
            fileModelL.setEdited(value);
        }
        else{
            fileModelR.setEdited(value);
        }
    }
    @Override
    public boolean getEdited(SideOfEditor side){
        if(side == SideOfEditor.Left){
            return fileModelL.getEdited();
        }
        else{
            return fileModelR.getEdited();
        }
    }


    private void resetModel(FileManagerInterface.SideOfEditor sideOfEditor) {
        if (sideOfEditor == FileManagerInterface.SideOfEditor.Left) {
            this.fileModelL = new FileModel();
        } else this.fileModelR = new FileModel();
    }
}
