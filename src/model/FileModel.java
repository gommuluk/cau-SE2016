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

    private ArrayList<Line> lineArrayList = new ArrayList<Line>();//데이터를 줄 단위로 저장하는 arraylist
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
     * Plain Text 형식의 파일의 Path를 받아 내부 Text를 읽습니다.
     * @param filePath 파일의 경로
     * @return boolean 파일 읽기에 대한 성공 여부
     */
    public boolean readFile(String filePath) { // 파일명 받기 및 읽기.
        file= new File(filePath);
        try {
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
            ret += s.content;
        return ret;
    }

    /**
     * 파일 내용을 저장하는 ArrayList의 Clone을 반환합니다.
     * @return file에 내용에 대한 Arraylist의 clone
     */
    public ArrayList<Line> getlLneArrayList() {
        return (ArrayList<Line>) lineArrayList.clone();
    }
    /**
     * 파일의 내용을 갱신합니다.
     * @param args 갱신할 내용
     */
    public void updateArrayList(String args) {
        lineArrayList = new ArrayList<Line>();
        for(String s : args.split("\n"))
            lineArrayList.add(new Line(s + "\n"));
    }

    /**
     * 파일을 현재 ArrayList의 내용으로 덮어씌웁니다.
     * FileNotFoundException이 발생할 수 있습니다.
     * @return boolean 파일 쓰기 성공 여부
     */
    public boolean writeFile() { // 파일명 받기 및 읽기
        try(  PrintWriter out = new PrintWriter(file.getPath()) ){
            String tstring ="";
            for (Line i : lineArrayList) {
                tstring+=i.content;
            }
            out.print(tstring);
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
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
