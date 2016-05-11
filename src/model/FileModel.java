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
    private static File fileL;
    private static File fileR;
    private static String stringL = "";
    private static String stringR = "";
    private FileModel() { // 기본 생성자
    }
    public static FileModel getModel() { // 1개의 객체를 유지하기 위한 싱글톤
       if(instance == null)
           instance = new FileModel();
        return instance;
    }

    public boolean readFileR(String fileRPath) { // 파일명 받기 및 읽기.
        fileR= new File(fileRPath);
        try(Scanner in = new Scanner(new FileReader(fileRPath)))
        {
            stringR = in.useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean readFileL(String fileLPath) { // 파일명 받기 및 읽기.
        fileL = new File(fileLPath);
        try(Scanner in = new Scanner(new FileReader(fileLPath)))
        {
            stringL = in.useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public String getStringR()
    {
        return stringR;
    }

    public String getStringL()
    {
        return stringL;
    }

    public boolean writeFileL(String fileLPath) { // 파일명 받기 및 읽기
        try(  PrintWriter out = new PrintWriter(fileLPath) ){
            out.print(stringL);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean writeFileR(String fileRPath) { // 파일명 받기 및 읽기
        try(  PrintWriter out = new PrintWriter(fileRPath) ){
            out.print(stringR);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
