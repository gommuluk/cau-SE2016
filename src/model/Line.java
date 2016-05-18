package model;
import java.util.ArrayList;
/**
 * Created by ano on 2016. 5. 18..
 */
public class Line {
    private String content;//이 라인이 가지고 있는 컨텐츠

    private int blockIndex; // 이 라인이 속해있는 블럭의 index. -1이면 속하는 블럭이 없다는 것
    private boolean isWhitespace;//compare로 생긴 공백 줄이면 true;
    private static ArrayList<Block> blockArrayList;//블럭을 가지고 있는 arraylist
    public Line(String input)
    {
        content = input;
    }
    public String getLine(boolean isLastLine) //마지막 줄이면 개행을 없이 그것이 아니면 개행 있이 content를 반환합니다
    {
        if(isLastLine) return content;
        else return content + "\n";
    }


    public enum Highlight//하이라이트 객체
    {
        unHilighted, whitespace, isDifferent,selected
    }
    public Highlight getHighlight()
    {
        if(blockIndex == -1) return Highlight.unHilighted;
        else if(isWhitespace) return Highlight.whitespace;
        else if(blockArrayList.get(blockIndex).getSelected()) return Highlight.selected;
        else return Highlight.isDifferent;
    }

    public void setBlockArray(ArrayList<Block> inArrayList)
    {
        blockArrayList = inArrayList;
    }



}
