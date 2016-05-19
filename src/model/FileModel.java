package model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Elliad on 2016-05-08.
 */
public class FileModel {

    private File file; //로드하고있는 파일
    public boolean isCompeared = false;//현재 비교를 하는 중이냐고
    private ArrayList<Line> lineArrayList = new ArrayList<Line>();//데이터를 줄 단위로 저장하는 arraylist
    private ArrayList<Line> comparedLineArrayList;
    protected ListProperty<String> listProperty = new SimpleListProperty<>();
    ObservableList<Integer> diffList = FXCollections.observableArrayList();


    private boolean isFileExist     = false;//불러온 파일이 있느냐를 저장하는 변수
    private boolean isEdited        = false;//이 파일이 수정됬는지를 저장하는 변수


    private SimpleStringProperty statusString; //현재 파일을 읽는지 로드중인지 그런 상태를 표시하는 문장

    public FileModel()
    {
        statusString = new SimpleStringProperty("Ready(No file is loaded)");
    }
    /**
     * FileModel으로투버 compare중이라는 것을 Compared된 array와 함께 전달합니다
     * @param clArrayList compare결과로 인해 새롭게 출력해햐하는 Line들을 가지고 있는 어레이리스트
     * @return
     */
    public void setCompare(ArrayList<Line> clArrayList)
    {
        isCompeared = true;
        comparedLineArrayList = clArrayList;
    }

    /**
     * FileModel으로부터 compare가 cancel됬다는 것을 받습니다
     * @return
     */
    public void cancleCompare()
    {
        isCompeared = false;
    }


    /**
     * Plain Text 형식의 파일의 Path를 받아 내부 Text를 읽습니다.
     * @param filePath 파일의 경로
     * @return boolean 파일 읽기에 대한 성공 여부
     */
    public boolean readFile(String filePath) throws FileNotFoundException, UnsupportedEncodingException{ // 파일명 받기 및 읽기.
        file= new File(filePath);
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8")));
        String tempString = "";
        lineArrayList = new ArrayList<Line>();
        while(in.hasNextLine()) {
            tempString = in.nextLine(); //임시 텍스트에 개행을 제외한 한 줄을 불러온다
            lineArrayList.add(new Line(tempString));//Line Arraylist에 읽어온 값을 추가한다
        }
        in.close();
        this.isFileExist = true;
        //listProperty.set(FXCollections.observableArrayList(lineArrayList));  리스트프로퍼티가 String인터페이스만 되서 묶어놓음 @승현

        statusString.set("File Loaded Successfully");
        return true;
    }

    /**
     * 읽어진 파일의 내용을 String의 형태로 반환합니다.
     * @return String 읽어진 파일의 내용
     */
    @Override
    public String toString() {
        String ret = "";
        for(Line s : lineArrayList)
            ret += s.getLine(false);
        ret = ret.substring(0,ret.length()-1);//맨 마지막의 개행 제거
        return ret;
    }

    /**
     * isCompared의 값에 따라서 파일 내용을 저장하는 ArrayList의 Clone을 반환합니다.
     * @return isCompared에 따른 지금 출력해야하는 Line을 가지는 Arraylist의 clone
     */
    public ArrayList<Line> getLineArrayList() {
        if(isCompeared) {
            return (ArrayList<Line>) comparedLineArrayList.clone();
        }
        else return (ArrayList<Line>) lineArrayList.clone();
    }
    /**
     * 파일의 내용을 갱신합니다.
     * @param args 갱신할 내용
     */
    public void updateArrayList(String args) {
        lineArrayList = new ArrayList<Line>();
        for(String s : args.split("\n"))
            lineArrayList.add(new Line(s));
    }

    /**
     * 현재 파일이 로드되있다면 파일을 현재 ArrayList의 내용으로 덮어씌웁니다.
     * FileNotFoundException이 발생할 수 있습니다.
     * @return boolean 파일 쓰기 성공 여부
     */
    public boolean writeFile() throws FileNotFoundException, SecurityException{ // 파일명 받기 및 읽기
        try( PrintWriter out = new PrintWriter(file.getPath()) ){
            String tstring ="";
            for (Line i : lineArrayList) {
                tstring+=i.getLine(false);
            }
            tstring = tstring.substring(0,tstring.length()-1);
            out.print(tstring);
            out.close();
        }
        catch (FileNotFoundException e)//파일이 없어져있는 경우
        {
            if(writeFile(file.getPath()))//이전에 있는 파일 경로를 이용해서 그 자리에 다시 파일 생성 시도
            {
                return true; //잘됐넹
            }
            else
            {
                throw new FileNotFoundException();//잘안됬으니 예외 던지기
            }
        }
        statusString.set("File Written successfully");
        return true;
    }
    /**
     * FilePath의 경로에 파일을 새로 만들어서 ArrayList의 내용으로 덮어씌웁니다.
     * FileNotFoundException이 발생할 수 있습니다.
     * @param FilePath 파일을 저장할 경로
     * @return boolean 파일 쓰기 성공 여부
     */
    public boolean writeFile(String FilePath) throws FileNotFoundException, SecurityException{ // 파일명 받기 및 읽기
        file = new File(FilePath);//새로운 경로에 파일 생성
        try( PrintWriter out = new PrintWriter(file.getPath()) ){
            String tstring ="";
            for (Line i : lineArrayList) {
                tstring+=i.getLine(false);
            }
            tstring = tstring.substring(0,tstring.length()-1);
            out.print(tstring);
            out.close();
        }
        statusString.set("File Written successfully");
        return true;
    }


    /**
     * 현재 Model의 상태를 반환합니다.
     * @return SimpleStringProperty 현재 Model의 상태
     */
    public SimpleStringProperty getStatus() {
        //TODO SAFE GETTER로 만들기 위한 CLONE 필요
        return statusString;
    }

    public ListProperty<String> getListProperty(){
        return listProperty;
    }

    /**
     * 현재 DiffList의 상태를 반환합니다.
     * @return ObservableList 현재 diffrence를 나타내는 List
     */
    public ObservableList<Integer> getList(){
        return diffList;
    }

    public boolean getFileExistFlag()   { return isFileExist; }
    public boolean getEditedFlag()      { return isEdited; }

}
