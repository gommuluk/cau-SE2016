package model;


import java.util.ArrayList;

/**
 * Created by ano on 2016. 5. 21..
 */
public interface FileManagerInterface {

    enum SideOfEditor{Left, Right} //왼쪽 패널인지 오른쪽 패널인지

    ArrayList<LineInterface> getLineArrayList(SideOfEditor side); //Side의 Line ArrayList를 가져옴
    boolean saveFile(String newContents, String filePath, SideOfEditor side); //Side에 파일경로로 저장함.
    boolean saveFile(String newContents, SideOfEditor side); //Side에 기존 파일 경로로 저장함. false를 반환하는 경우는 기존 파일이 없을 경우이다
    ArrayList<LineInterface> loadNewFile(String filePath, SideOfEditor side); //filePath에 있는 파일을 side에 로드
    boolean setCompare(); //비교 실행. 비교할 수 없는 조건이면 false를 반환
    void cancelCompare(); //비교 취소
    void clickLine(int lineNum); //lineNum번째 line을 클릭했다고 FileManager에게 알려줌
    void merge(SideOfEditor toSide); //다른사이드부터  toSide로 파일을 머지함

}
