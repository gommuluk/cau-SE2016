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

    private ArrayList<LineInterface> compareLineArrayList;
    private File file; //로드하고있는 파일

    /**
     * 읽어진 파일의 내용을 String의 형태로 반환합니다.
     * @return String 읽어진 파일의 내용
     */
    @Override
    public String toString() {
        String ret = "";
        for(LineInterface s : lineArrayList)
            ret += s.getContent(false);
        if(ret.length() >=1)
        ret = ret.substring(0,ret.length()-1);//맨 마지막의 개행 제거

        System.out.println("Model System Debug log@@@@@@@@\n" + ret + "Model System Log end@@@@@@@@");

        String word = ret.substring(0, 5);
        try {
            System.out.println("ANSI(1) : " + new String(word.getBytes("windows-1252"), "US-ASCII"));

            System.out.println("utf-8(1) : " + new String(word.getBytes("utf-8"), "euc-kr"));
            System.out.println("utf-8(2) : " + new String(word.getBytes("utf-8"), "ksc5601"));
            System.out.println("utf-8(3) : " + new String(word.getBytes("utf-8"), "x-windows-949"));
            System.out.println("utf-8(4) : " + new String(word.getBytes("utf-8"), "iso-8859-1"));

            System.out.println("iso-8859-1(1) : " + new String(word.getBytes("iso-8859-1"), "euc-kr"));
            System.out.println("iso-8859-1(2) : " + new String(word.getBytes("iso-8859-1"), "ksc5601"));
            System.out.println("iso-8859-1(3) : " + new String(word.getBytes("iso-8859-1"), "x-windows-949"));
            System.out.println("iso-8859-1(4) : " + new String(word.getBytes("iso-8859-1"), "utf-8"));

            System.out.println("euc-kr(1) : " + new String(word.getBytes("euc-kr"), "ksc5601"));
            System.out.println("euc-kr(2) : " + new String(word.getBytes("euc-kr"), "utf-8"));
            System.out.println("euc-kr(3) : " + new String(word.getBytes("euc-kr"), "x-windows-949"));
            System.out.println("euc-kr(4) : " + new String(word.getBytes("euc-kr"), "iso-8859-1"));

            System.out.println("ksc5601(1) : " + new String(word.getBytes("ksc5601"), "euc-kr"));
            System.out.println("ksc5601(2) : " + new String(word.getBytes("ksc5601"), "utf-8"));
            System.out.println("ksc5601(3) : " + new String(word.getBytes("ksc5601"), "x-windows-949"));
            System.out.println("ksc5601(4) : " + new String(word.getBytes("ksc5601"), "iso-8859-1"));

            System.out.println("x-windows-949(1) : " + new String(word.getBytes("x-windows-949"), "euc-kr"));
            System.out.println("x-windows-949(2) : " + new String(word.getBytes("x-windows-949"), "utf-8"));
            System.out.println("x-windows-949(3) : " + new String(word.getBytes("x-windows-949"), "ksc5601"));
            System.out.println("x-windows-949(4) : " + new String(word.getBytes("x-windows-949"), "iso-8859-1"));
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return ret;
    }
    /**
     * 파일의 내용을 갱신합니다.
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
        FileInputStream FIS = new FileInputStream(filePath);
        byte[] BOM = new byte[4];
        try{
            FIS.read(BOM,0,4); FIS.close();

        }
        catch (IOException e)
        {

        }

        FIS = new FileInputStream(filePath);
        InputStreamReader ISR = new InputStreamReader(FIS);
        System.out.println("Encoding:"+ISR.getEncoding());
        in = new Scanner(new BufferedReader(ISR));

        String tempString;
        lineArrayList = new ArrayList<>();
        while(in.hasNextLine()) {
            tempString = in.nextLine(); //임시 텍스트에 개행을 제외한 한 줄을 불러온다
            lineArrayList.add(new Line(tempString));//Line Arraylist에 읽어온 값을 추가한다
        }
        in.close();

        //listProperty.set(FXCollections.observableArrayList(lineArrayList));  리스트프로퍼티가 String인터페이스만 되서 묶어놓음 @승현
        this.contentListProperty.set(FXCollections.observableArrayList(lineArrayList));
        this.statusString.set("File Loaded Successfully");
        this.filePath.set(filePath);
    }
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
     * isCompared의 값에 따라서 파일 내용을 저장하는 ArrayList의 Clone을 반환합니다.
     * @return isCompared에 따른 지금 출력해야하는 Line을 가지는 Arraylist의 clone
     */
    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<LineInterface> getLineArrayList() {
        contentListProperty.set(FXCollections.observableArrayList(lineArrayList));
        return (ArrayList<LineInterface>) lineArrayList.clone();
    }
    @Override
    public ArrayList<LineInterface> getCompareLineArrayList() {
        this.contentListProperty.set(FXCollections.observableArrayList(compareLineArrayList));
        return compareLineArrayList;
    }
    @Override
    public void setCompareLineArrayList(ArrayList<LineInterface> lineArrayList)
    {
        compareLineArrayList = lineArrayList;

        this.contentListProperty.set(null);
        this.contentListProperty.set(FXCollections.observableArrayList(compareLineArrayList));
        System.out.println("setCompareLineArrayList");
    }

    @Override
    public void clickLine(int lineNum) {
        compareLineArrayList.get(lineNum).clickBlock();
    }

    @Override
    public void updateHighlight() {
        this.contentListProperty.set(null);
        this.contentListProperty.set(FXCollections.observableArrayList(compareLineArrayList));
    }

    @Override
    public boolean isFileExist()
    {
        return file != null;
    }

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

    private void init()
    {
        lineArrayList = null;
        isEdited = false;
        file = null;
        compareLineArrayList =null;
        //stringoroperty 초기호
    }

    @Override
    public void setEdited(boolean value){
        isEdited = value;
        this.isEditedProperty.set(value);
    }

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
