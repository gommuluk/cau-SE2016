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

    private ArrayList<String> stringArrayList = new ArrayList<>();//데이터를 줄 단위로 저장하는 arraylist
    protected ListProperty<String> listProperty = new SimpleListProperty<>();
    ObservableList<Integer> diffList = FXCollections.observableArrayList();


    private boolean isFileExist     = false;
    private boolean isEdited        = false;


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

            while(in.hasNextLine()) {
                tempString = in.nextLine(); //임시 텍스트에 개행을 제외한 한 줄을 불러온다
                if(in.hasNextLine()) tempString +="\n"; //다음 줄이 없을 때는 개행을 추가하지 않는다.
                stringArrayList.add(tempString);
            }

            in.close();
            this.isFileExist = true;
            listProperty.set(FXCollections.observableArrayList(stringArrayList));

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
        for(String s : stringArrayList)
            ret += s;
        return ret;
    }

    /**
     * 파일 내용을 저장하는 ArrayList의 Clone을 반환합니다.
     * @return file에 내용에 대한 Arraylist의 clone
     */
    public ArrayList<String> getStringArrayList() {
        return (ArrayList<String>) stringArrayList.clone();
    }
    /**
     * 파일의 내용을 갱신합니다.
     * @param args 갱신할 내용
     */
    public void updateArrayList(String args) {
        stringArrayList = new ArrayList<String>();
        for(String s : args.split("\n"))
            stringArrayList.add(s + "\n");
    }

    /**
     * 파일을 현재 ArrayList의 내용으로 덮어씌웁니다.
     * FileNotFoundException이 발생할 수 있습니다.
     * @return boolean 파일 쓰기 성공 여부
     */
    public boolean writeFile() { // 파일명 받기 및 읽기
        try(  PrintWriter out = new PrintWriter(file.getPath()) ){
            String tstring ="";
            for (String i : stringArrayList) {
                tstring+=i;
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
