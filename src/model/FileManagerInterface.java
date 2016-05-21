package model;


import javafx.beans.property.ReadOnlyStringProperty;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by ano on 2016. 5. 21..
 */
public interface FileManagerInterface {

    enum SideOfEditor{Left, Right} //왼쪽 패널인지 오른쪽 패널인지

    ArrayList<LineInterface> getLineArrayList(SideOfEditor side); //Side의 Line ArrayList를 가져옴
    void updateLineArrayList(String content, SideOfEditor side);//side의 컨텐츠를 string으로 갱신함

    void saveFile(String newContents, String filePath, SideOfEditor side) throws FileNotFoundException, SecurityException; //Side에 파일경로로 저장함.
    void saveFile(String newContents, SideOfEditor side) throws FileNotFoundException, SecurityException; //Side에 기존 파일 경로로 저장함. false를 반환하는 경우는 기존 파일이 없을 경우이다
    ArrayList<LineInterface> loadFile(String filePath, SideOfEditor side) throws FileNotFoundException, UnsupportedEncodingException; //filePath에 있는 파일을 side에 로드
    void setCompare() throws LeftEditorFileNotFoundException, RightEditorFileNotFoundException; //비교 실행. 비교할 수 없는 조건이면 false를 반환

    void cancelCompare(); //비교 취소
    void clickLine(int lineNum); //lineNum번째 line을 클릭했다고 FileManager에게 알려줌
    boolean merge(SideOfEditor toSide); //다른사이드부터  toSide로 파일을 머지함
    String getString(SideOfEditor side);//해당 side의 content를 string으로 받아옴
    String getFilePath(SideOfEditor side);
    ReadOnlyStringProperty getStatus(SideOfEditor side);

    int[][] getArrayLCS();//@@for debug
}
