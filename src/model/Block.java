package model;

import java.util.Objects;

/**
 * Created by ano on 2016. 5. 18..
 */
public class Block {
    public int startLineNum;//이 블럭이 시작하는 라인의 숫자
    public int endLineNum;//이 블럭이 끝나는 라인의 숫자
    private boolean isSelected; //이 블럭이 선택됬는지를 가지는 변수

    public boolean getSelected()
    {
        return isSelected;
    } //선택되고 있는지를 반환한다
    public void setLineNum(int start, int end)//시작줄하고 끝줄을 지정한다. 필요없으니 지워야됨
    {
        startLineNum = start;
        endLineNum = end;
    }
    public Block(int start, int end)//시작줄하고 끝줄을 생성과 동시에 지정한다
    {
        startLineNum = start;
        endLineNum = end;
    }
    public Block(){}

    public void click()//이 블럭이 클릭됬을때
    {
        if(isSelected) isSelected = false;
        else isSelected = true;
    }





}
