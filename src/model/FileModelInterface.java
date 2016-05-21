package model;

import javax.sql.rowset.serial.SerialException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by ano on 2016. 5. 21..
 */
public interface FileModelInterface {
    String toString(); //파일모델의 라인 어레이 리스트를 스트링으로 빼서 반환합니다
    void updateArrayList(String content); //content의 정보로 어레이리스트를 갱신한다
    void readFile() throws FileNotFoundException, UnsupportedEncodingException;
    void readFile(String filePath) throws FileNotFoundException, UnsupportedEncodingException;
    void writeFile() throws FileNotFoundException, SecurityException;
    void writeFile(String filePath) throws FileNotFoundException, SecurityException;
    ArrayList<LineInterface> getLineArrayList();
    ArrayList<LineInterface> getCompareLineArrayList();
    void setCompareLineArrayList(ArrayList<LineInterface> compareLineArrayList);
    void clickLine(int lineNum);
    boolean isFileExist();




}
