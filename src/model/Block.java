package model;

/**
 * Created by ano on 2016. 5. 18..
 */
public class Block {
    private int startLineNum;//이 블럭이 시작하는 라인의 숫자
    private int endLineNum;//이 블럭이 끝나는 라인의 숫자
    private boolean isSelected;
    public boolean getSelected()
    {
        return isSelected;
    }
    public void click()//이 블럭에게 클릭 이벤트를 줍니당
    {
        if(isSelected) isSelected = false;
        else isSelected = true;
    }




}
