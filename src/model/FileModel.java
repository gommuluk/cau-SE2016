package model;

import java.io.File;
import java.io.FileNotFoundException;
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
            while(in.hasNextLine())
            {
                stringR+=in.nextLine();
            }
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
            while(in.hasNextLine())
            {
                stringL+=in.nextLine();
            }

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

    public void writeFiles() { // 파일명 받기 및 읽기




    }

}
