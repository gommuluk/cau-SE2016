package model;

import javafx.beans.property.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by ano on 2016. 5. 13..
 */
public class FileManager implements FileManagerInterface {

    private boolean isComparing = false;
    private final BooleanProperty isCompareProperty = new SimpleBooleanProperty(isComparing);

    private static FileManagerInterface instance;//싱글톤을 위한 객체
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
     * @return FileManager 양 쪽 Editor에 할당된 파일들을 관리하는 매니저
     */
    public static FileManagerInterface getFileManagerInterface() { // 1개의 객체를 유지하기 위한 싱글톤
        if (instance == null) instance = new FileManager();
        return instance;
    }

    /**
     * side에 맞는 LineArrayList를 compare상태에 따라서 반환해줍니다.
     * @return FileManager 양 쪽 Editor에 할당된 파일들을 관리하는 매니저
     */
    @Override
    public ArrayList<LineInterface> getLineArrayList(FileManagerInterface.SideOfEditor side) {
        if (side == SideOfEditor.Left) {
            if (isComparing){
                return fileModelL.getCompareLineArrayList();
            }
            else return fileModelL.getLineArrayList();
        } else {
            if (isComparing) return fileModelR.getCompareLineArrayList();
            else return fileModelR.getLineArrayList();
        }
    }

    /**
     * side에 맞는 FileModel의 LineArrayList를 content의 내용으로 바꿉니다
     * @param content 갱신할 내용을 가지고 있는 String
     * @param side 갱신을 할 파일모델의 위치
     */
    @Override
    public void updateLineArrayList(String content, SideOfEditor side) {
        if (side == SideOfEditor.Left) fileModelL.updateArrayList(content);
        else fileModelR.updateArrayList(content);
    }
    /**
     * side에 맞는 FileModel의 LineArrayList를 content의 내용으로 바꾸고 연결된 파일 경로에 저장합니다
     * @param content 갱신할 내용을 가지고 있는 String
     * @param side 갱신을 할 파일모델의 위치
     */
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
    /**
     * side에 맞는 FileModel의 LineArrayList를 content의 내용으로 바꾸고 filePath 경로에 저장합니다
     * @param content 갱신할 내용을 가지고 있는 String
     * @param filepath 저장할 경로
     * @param side 갱신을 할 파일모델의 위치
     */
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
    /**
     * side에 맞는 FileModel의 LineArrayList를 content의 내용으로 바꾸고 filePath 경로에 저장합니다
     * @param filepath 불러올 경로
     * @param side 갱신을 할 파일모델의 위치
     */
    @Override
    public void loadFile(String filepath, SideOfEditor side) throws FileNotFoundException, UnsupportedEncodingException {
        if (side == SideOfEditor.Left) {
            fileModelL.readFile(filepath);
            fileModelL.getLineArrayList();
        } else {
            fileModelR.readFile(filepath);
            fileModelR.getLineArrayList();
        }
    }
    /**
     * compare를 실행하고 isComparing을 True로 한다
     */
    @Override
    public void setCompare() throws LeftEditorFileCanNotCompareException, RightEditorFileCanNotCompareException {
        if (!(fileModelL.isFileExist() && !fileModelL.getEdited())) throw new LeftEditorFileCanNotCompareException();
        if (!(fileModelR.isFileExist() && !fileModelR.getEdited())) throw new RightEditorFileCanNotCompareException();
        arrayLCS = new int[fileModelL.getLineArrayList().size() + 1][fileModelR.getLineArrayList().size() + 1];//LCS 배열의 초기화
        buildArrayLCS();// 배열 구성
        backTrackingLCS(); //diff구현 및 compare array저장
        isComparing = true;
        isCompareProperty.set(true);
    }

    /**
     * compare를 취소한다.
     */
    @Override
    public void cancelCompare() {
        isComparing = false;
        isCompareProperty.set(false);
    }

    /**
     * comparing상태일 경우 lineNum번째 줄에 클릭을 했다는 것을 전달한다.
     * @param lineNum 클릭이 된 LineNumber
     */
    @Override
    public void clickLine(int lineNum) {
        if (isComparing) {

            fileModelL.clickLine(lineNum);//왼쪽이든 오른쪽이든 상관없음! 일부러 사이드 검사를 안해줬습니다
            fileModelL.updateHighlight();
            fileModelR.updateHighlight();
        }
    }

    /**
     * toSide에게 다른 사이드로부터 Merge를 실행 후. 양 파일 모델의 LineArrayList를 갱신한다. 머지가 불가능 상황이면 실행을 하지 않고 불가능하다고 반환한다.
     * @param toSide  머지를 당할  side
     * @return 머지가 실행됬는지 안됬는지를 저장
     */
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
        ArrayList<LineInterface> toFileManagerCompareLineArrayList = toFileManager.getCompareLineArrayList();
        ArrayList<LineInterface> fromFileManagerCompareLineArrayList = fromFileManager.getCompareLineArrayList();

        System.out.println("Model debug blockArrayList size : " + blockArrayList.size());
        System.out.println("Model to compareLineArrayList size : " +toFileManager.getCompareLineArrayList().size() + "Model from compareLineArrayList size : " +fromFileManager.getCompareLineArrayList().size() );
        for (Block b : blockArrayList) {
            System.out.println("startLineNum : " + b.startLineNum + " endLineNum : " + b.endLineNum);
            int count = b.endLineNum - b.startLineNum;//지워야 될 블럭의 갯수

            System.out.println("Model debug count  : " + count);
            int insertNum = b.startLineNum; //이 줄에 넣을거임
            if (b.getSelected()) {//선택된 블럭이면
                System.out.println("block is clicked;");
                for (int i = 0; i < count; i++) {
                    toFileManagerCompareLineArrayList.remove(insertNum);//복사대상이 되는 블럭을 지운다
                }
                for (int i = 0; i < count; i++) {
                    LineInterface tempL = fromFileManagerCompareLineArrayList.get(insertNum); //넣을 줄
                    if (tempL.getIsWhiteSpace()) fromFileManagerCompareLineArrayList.remove(insertNum); //공백줄이면 지워줌
                    else {
                        LineInterface insertL = new Line(tempL.getContent(true));
                        toFileManagerCompareLineArrayList.add(insertNum, insertL);//아니면 넣어주고
                        insertNum++;//하나 넣어줬으니 하나 올려주고
                    }
                }
            } else {//선택 안됬으면 공백을 제거하는 작업을 해준다
                for (int i = 0; i < count; i++) {
                    LineInterface tempL = fromFileManagerCompareLineArrayList.get(insertNum);//복사대상이 되는 줄. 의미 없는듯
                    if (tempL.getIsWhiteSpace()) fromFileManagerCompareLineArrayList.remove(insertNum);//공백이면 제거
                    else {
                        insertNum++;
                    }
                }
                count = b.endLineNum - b.startLineNum;
                insertNum = b.startLineNum;//시작줄 초기화
                for (int i = 0; i < count; i++) {
                    LineInterface tempR = toFileManagerCompareLineArrayList.get(insertNum);
                    if (tempR.getIsWhiteSpace()) toFileManagerCompareLineArrayList.remove(insertNum);
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
        System.out.println("from : "  + ret);
        toFileManager.updateArrayList(ret);
        ret = "";
        for (LineInterface s : fromFileManager.getCompareLineArrayList())
            ret += s.getContent(false);
        ret = ret.substring(0, ret.length() - 1);//맨 마지막의 개행 제거

        System.out.println("to : "  + ret);
        fromFileManager.updateArrayList(ret);
        cancelCompare();

        if(toSide == SideOfEditor.Left) this.fileModelL.setEdited(true);
        else this.fileModelR.setEdited(true);

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
        if (side == SideOfEditor.Left) return fileModelL.getFilePath();
        else return fileModelR.getFilePath();
    }

    @Override
    public ReadOnlyStringProperty getStatus(SideOfEditor side) {
        return side == SideOfEditor.Left ? fileModelL.getStatus() : fileModelR.getStatus();
    }

    private int max(int a, int b) {
        return a >b ? a : b;
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
        ArrayList<LineInterface> cLineArrayListL = new ArrayList<>();
        ArrayList<LineInterface> cLineArrayListR = new ArrayList<>();
        blockArrayList = new ArrayList<>();//블럭 어레이리스트를 새로 만든다.
        Line.setBlockArrayList(blockArrayList);
        int i = fileModelL.getLineArrayList().size();
        int j = fileModelR.getLineArrayList().size();//끝점부터 시작한다.
        int startLineNum; //블럭 시작줄
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
                    startLineNum = count;
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
                    endLineNum = count;
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
        return side == SideOfEditor.Left ? fileModelL.filePathProperty() : fileModelR.filePathProperty();
    }

    @Override
    public ListProperty<LineInterface> listProperty(SideOfEditor side) {
        return side == SideOfEditor.Left ? fileModelL.listProperty() : fileModelR.listProperty();
    }

    @Override
    public ReadOnlyBooleanProperty isEditedProperty(SideOfEditor side) {
        return side == SideOfEditor.Left ? fileModelL.isEditedProperty() : fileModelR.isEditedProperty();
    }

    @Override
    public ReadOnlyBooleanProperty isCompareProperty() {
        return this.isCompareProperty;
    }

    @Override
    public boolean getComparing()
    {
        return isComparing;
    }

    @Override
    public void setEdited(SideOfEditor side) {
        if(side == SideOfEditor.Left){
            fileModelL.setEdited(true);
        }
        else{
            fileModelR.setEdited(true);
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


    public void resetModel(SideOfEditor sideOfEditor) {
        isComparing = false;
        isCompareProperty.set(false);

        if (sideOfEditor == FileManagerInterface.SideOfEditor.Left) {
            this.fileModelL.init();
        } else this.fileModelR.init();
    }
    /*
    Dependency Injection method for Mock testing using EasyMock
     */
    public void setDependency(FileModelInterface fl, FileModelInterface fr){
        fileModelL = fl;
        fileModelR = fr;
    }
}
