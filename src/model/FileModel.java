package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileReader;
/**
 * Created by Elliad on 2016-05-08.
 */
public class FileModel {
    private static FileModel instance;
    private static File file;
    private static String string = "";
    private FileModel() { // 기본 생성자
    }
    public static FileModel getModel() { // 1개의 객체를 유지하기 위한 싱글톤
       if(instance == null)
           instance = new FileModel();
        return instance;
    }

    public boolean readFile(String filePath) { // 파일명 받기 및 읽기.
        file= new File(filePath);
        try(Scanner in = new Scanner(new FileReader(filePath)))
        {
            string = in.useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }
    public String getString()
    {
        return string;
    }


    public boolean writeFile(String filePath) { // 파일명 받기 및 읽기
        try(  PrintWriter out = new PrintWriter(filePath) ){
            out.print(string);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
