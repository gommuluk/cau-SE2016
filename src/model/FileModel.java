package model;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Elliad on 2016-05-08.
 */
public class FileModel {

    private File file; //로드하고있는 파일
    private ArrayList<String> stringArrayList = new ArrayList<String>();//데이터를 줄 단위로 저장하는 arraylist
    private SimpleStringProperty statusString; //현재 파일을 읽는지 로드중인지 그런 상태를 표시하는 문장

    public FileModel()
    {
        statusString = new SimpleStringProperty("Ready(No file is loaded)");
    }


    public boolean readFile(String filePath) { // 파일명 받기 및 읽기.
        file= new File(filePath);
        try(Scanner in = new Scanner(new FileReader(filePath)))
        {
            String tempString = "";
            while(in.hasNext()) {
                tempString = in.next(); //임시 텍스트에 개행을 제외한 한 줄을 불러온다
                if(in.hasNext()) tempString +="\n"; //다음 줄이 없을 때는 개행을 추가하지 않는다.
                stringArrayList.add(tempString);
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return false;
        }
        statusString.set("File Loaded Successfully");
        return true;
    }
    @Override
    public String toString() {
        String ret = "";
        for(String s : stringArrayList)
            ret += s;
        return ret;
    }

    public void updateArrayList(String args) {
        stringArrayList = new ArrayList<String>();
        for(String s : args.split("\n"))
            stringArrayList.add(s + "\n");

    }
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

    public SimpleStringProperty getStatus() {
        //TODO SAFE GETTER로 만들기 위한 CLONE 필요
        return statusString;
    }


}
