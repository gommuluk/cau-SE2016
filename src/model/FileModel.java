package model;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Elliad on 2016-05-08.
 */
public class FileModel {
    private static FileModel instance;
    public static File fileA;
    public static File fileB;

    private FileModel() { // 기본 생성자
    }
    public static FileModel getModel() { // 1개의 객체를 유지하기 위한 싱글톤
       if(instance == null)
           instance = new FileModel();
        return instance;
    }

    public void readFiles(String fileAPath, String fileBPath) { // 파일명 받기 및 읽기
        this.fileA = new File(fileAPath);
        this.fileB = new File(fileBPath);
    }

}
