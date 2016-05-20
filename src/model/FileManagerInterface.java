package model;


import java.util.ArrayList;

/**
 * Created by ano on 2016. 5. 21..
 */
public interface FileManagerInterface {

    enum SideOfEditor{Left, Right}

    ArrayList<Line> getArrayList(SideOfEditor side);
    boolean SaveFile(String filePath, SideOfEditor side);
    ArrayList<Line> LoadNewFile(String filepath, SideOfEditor side);
    boolean setCompare();
    void cancelCompare();
    void clickLine(int lineNum);
    void merge(SideOfEditor toSide);


}
