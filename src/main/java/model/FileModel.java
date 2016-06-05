package model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Elliad on 2016-05-08.
 */
public class FileModel implements FileModelInterface {

    private ArrayList<LineInterface> lineArrayList = new ArrayList<>();//데이터를 줄 단위로 저장하는 arraylist
    private boolean isEdited = false;//이 파일이 수정됬는지를 저장하는 변수

    private final ReadOnlyStringWrapper statusString = new ReadOnlyStringWrapper("Ready(No file is loaded)");
    private final ReadOnlyStringWrapper filePath = new ReadOnlyStringWrapper("새 파일");
    private final BooleanProperty isEditedProperty = new SimpleBooleanProperty(isEdited);
    private final ListProperty<LineInterface> contentListProperty = new SimpleListProperty<>(FXCollections.observableArrayList(lineArrayList));

    private ArrayList<LineInterface> compareLineArrayList; //컴페어 결과를 저장하고 있는 어레이리스트
    private File file; //연결되있는 파일

    /**
     * 가지고 있는 파일의 내용을  String의 형태로 반환합니다.
     * @return String 읽어진 파일의 내용
     */
    @Override
    public String toString() {
        String ret = "";
        for(LineInterface s : lineArrayList)
            ret += s.getContent(false);
        if(ret.length() >=1) // 파일이 빈 파일이 아닐 때
        ret = ret.substring(0,ret.length()-1);//맨 마지막의 개행 제거
        return ret;
    }
    /**
     * LineArrayList의 내용을 string을 받아서 갱신합니다.
     * @param args 갱신할 내용
     */
    @Override
    public void updateArrayList(String args) {
        lineArrayList = new ArrayList<>();
        for(String s : args.split("\n",0xffffffff))
            lineArrayList.add(new Line(s));
        this.contentListProperty.set(FXCollections.observableArrayList(lineArrayList));
    }
    /**
     * Plain Text 형식의 파일의 Path를 받아 내부 Text를 읽습니다.
     * @param filePath 파일의 경로
     */
    @Override
    public void readFile(String filePath) throws FileNotFoundException, UnsupportedEncodingException{ // 파일명 받기 및 읽기.
        init();
        Scanner  in;
        file= new File(filePath);
        in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(filePath))));
        String tempString;
        lineArrayList = new ArrayList<>();
        while(in.hasNextLine()) {
            tempString = in.nextLine(); //임시 텍스트에 개행을 제외한 한 줄을 불러온다
            lineArrayList.add(new Line(tempString));//Line Arraylist에 읽어온 값을 추가한다
        }
        in.close();
        this.contentListProperty.set(FXCollections.observableArrayList(lineArrayList));
        this.statusString.set("File Loaded Successfully");
        this.filePath.set(filePath);
    }
    /**
     * 저장된 파일 정보를 이용해서 파일을 읽게 합니다. 연결된 파일이 없으면 예외를 발생시킵니다.
     */
    @Override
    public void readFile() throws FileNotFoundException, UnsupportedEncodingException {
        if(file == null) throw new FileNotFoundException();
        else readFile(file.getPath());
    }
    /**
     * 현재 파일이 로드되있다면 파일을 현재 ArrayList의 내용으로 덮어씌웁니다.
     * FileNotFoundException이 발생할 수 있습니다.
     */
    @Override
    public void writeFile() throws FileNotFoundException, SecurityException{ // 파일명 받기 및 읽기
        if(file == null) throw new FileNotFoundException();
        else writeFile(file.getPath());
    }
    /**
     * FilePath의 경로에 파일을 새로 만들어서 ArrayList의 내용으로 덮어씌웁니다.
     * FileNotFoundException이 발생할 수 있습니다.
     * @param FilePath 파일을 저장할 경로
     */
    @Override
    public void writeFile(String FilePath) throws FileNotFoundException, SecurityException{ // 파일명 받기 및 읽기
        file = new File(FilePath);//새로운 경로에 파일 생성
        PrintWriter out = new PrintWriter(file.getPath());
        String tstring ="";
        for (LineInterface i : lineArrayList) {
            tstring += i.getContent(false);
        }
        tstring = tstring.substring(0,tstring.length()-1);
        out.print(tstring);
        out.close();

        // property change
        this.statusString.set("File Written successfully");
        this.filePath.set(file.getPath());
        this.isEdited = false;
        this.isEditedProperty.set(false);
    }

    /**
     * 파일 내용을 저장하는 ArrayList의 Clone을 반환합니다.
     * @return Line을 가지는 Arraylist의 clone
     */
    @Override
    public ArrayList<LineInterface> getLineArrayList() {
        contentListProperty.set(FXCollections.observableArrayList(lineArrayList));
        return (ArrayList<LineInterface>) lineArrayList.clone();
    }
    /**
     * 비교 결과의 내용을 저장하는 compareLineArrayList의 참조를 반환합니다.
     sideeffect = view의 listView를 갱신한다.
     * @return compareLineArrayList의 참조
     */
    @Override
    public ArrayList<LineInterface> getCompareLineArrayList() {
        this.contentListProperty.set(FXCollections.observableArrayList(compareLineArrayList));
        return compareLineArrayList;
    }
    /**
     * 비교 결과의 내용을 저장하는 compareLineArrayList의 참조를 받아와서 그 값으로 변경합니다.
     * sideeffect = view의 listView를 갱신한다.
     * @param lineArrayList 값을 갱신할 linearrayList
     */
    @Override
    public void setCompareLineArrayList(ArrayList<LineInterface> lineArrayList)
    {
        compareLineArrayList = lineArrayList;

        this.contentListProperty.set(null);
        this.contentListProperty.set(FXCollections.observableArrayList(compareLineArrayList));
    }
    /**
     * compareLineArrayList의 lineNum번째 line에게 클릭되었다는 메시지를 전한다
     * @param lineNum 클릭된 Line의 인덱스
     */
    @Override
    public void clickLine(int lineNum) {
        compareLineArrayList.get(lineNum).clickBlock();
    }
    /**
     * compareLineArrayList의 lineNum번째 line에게 클릭되었다는 메시지를 전한다
     * sideeffect = view의 listView를 갱신한다.
     */
    @Override
    public void updateHighlight() {
        this.contentListProperty.set(null);
        this.contentListProperty.set(FXCollections.observableArrayList(compareLineArrayList));
    }
    /**
     * 연결된 파일이 있는지 없는지를 반환한다
     * @return 연결된 파일이 있는지 없는지를 반환
     */
    @Override
    public boolean isFileExist()
    {
        return file != null;
    }

    /**
     * 연결된 파일의 경로를 반환한다
     * @return 연결된 파일의 경로를 반환 연결된 파일이 없으면 null을 반환
     */
    @Override
    public String getFilePath()
    {
        if(file == null)
        {
            return null;
        }
        else
        {
            return file.getPath();
        }
    }

    /**
     * 모델을 초기화한다
     */
    public void init()
    {
        lineArrayList = new ArrayList<>();
        isEdited = false;
        file = null;
        compareLineArrayList = new ArrayList<>();
        this.isEditedProperty.set(false);
        this.filePath.set("새 파일");
        this.contentListProperty.set(FXCollections.observableArrayList(lineArrayList));
        this.statusString.set("Ready(No file is loaded)");
    }

    /**
     * 이 모델의 데이터가 수정됬을 가능성이 있는지를 받는다
     * @param value 수정됬는지 아닌지의 값
     */
    @Override
    public void setEdited(boolean value){
        isEdited = value;
        this.isEditedProperty.set(value);
    }
    /**
     * 이 모델의 데이터가 수정됬을 가능성이 있는지를 받는다
     * @return boolean 수정됬는지 아닌지를 저장
     */
    @Override
    public boolean getEdited(){
        return isEdited;
    }
    /**
     * 현재 Model의 상태를 반환합니다.
     * @return SimpleStringProperty 현재 Model의 상태
     */
    public ReadOnlyStringProperty getStatus() {
        return statusString;
    }

    @Override
    public ReadOnlyStringProperty filePathProperty() {
        return this.filePath;
    }

    @Override
    public ListProperty<LineInterface> listProperty() {
        return this.contentListProperty;
    }

    @Override
    public ReadOnlyBooleanProperty isEditedProperty() {
        return this.isEditedProperty;
    }

}
