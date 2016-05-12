package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
/**
 * Created by Elliad on 2016-05-08.
 */
public class FileModel {

    private File file; //로드하고있는 파일
    private ArrayList<String> stringArrayList = new ArrayList<String>();//데이터를 줄 단위로 저장하는 arraylist
    private String statusString; //현재 파일을 읽는지 로드중인지 그런 상태를 표시하는 문장

    public FileModel()
    {
        statusString = "Ready(No file is loaded)";
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
        statusString = "File Loaded Successfully";
        return true;
    }

    public ArrayList<String> getStringArrayList()
    {
        return stringArrayList;
    }


    public boolean writeFile(String filePath) { // 파일명 받기 및 읽기
        try(  PrintWriter out = new PrintWriter(filePath) ){
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
        statusString = "File Written successfully";
        return true;
    }
    public String getStatus()
    {
        return statusString;
    }


}
