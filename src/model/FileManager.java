package model;


/**
 * Created by ano on 2016. 5. 13..
 */
public class FileManager {
    private static FileManager instance;
    private static FileModel FileModelL = new FileModel();
    private static FileModel FileModelR = new FileModel();
    private FileManager(){}

    public static FileManager getFileManager() { // 1개의 객체를 유지하기 위한 싱글톤
        if(instance == null)
            instance = new FileManager();
        return instance;
    }

    public static FileModel GetFileModelL()
    {
        return FileModelL;
    }
    public static FileModel GetFileModelR()
    {
        return FileModelR;
    }





}
