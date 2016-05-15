package model;


/**
 * Created by ano on 2016. 5. 13..
 */
public class FileManager {

    private static FileManager instance;
    private FileModel FileModelL = new FileModel();
    private FileModel FileModelR = new FileModel();
    private FileManager(){}

    /**
     * FileManager 객체를 반환합니다.
     * @return FileManager 양 쪽 Editor에 할당된 파일들을 관리하는 매니저
     */
    public static FileManager getFileManager() { // 1개의 객체를 유지하기 위한 싱글톤
        if(instance == null)
            instance = new FileManager();
        return instance;
    }

    /**
     * Left Editor에 할당된 File Model을 반환합니다.
     * @return FileModel 왼쪽 Editor에 할당된 FileModel
     */
    public FileModel GetFileModelL()
    {
        return FileModelL;
    }

    /**
     * Right Editor에 할당된 File Model을 반환합니다.
     * @return FileModel 오른쪽 Editor에 할당된 FileModel
     */
    public FileModel GetFileModelR()
    {
        return FileModelR;
    }


}
