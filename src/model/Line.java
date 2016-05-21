package model;
import java.util.ArrayList;

/**
 * Created by ano on 2016. 5. 18..
 */
public class Line implements LineInterface {

    private String content;//이 라인이 가지고 있는 컨텐츠
    private LineHighlight lineStatus = LineHighlight.unHighlighted; //현재 라인의 상태
    private int blockIndex; // 이 라인이 속해있는 블럭의 index. -1이면 속하는 블럭이 없다는 것
    private boolean isWhitespace;//compare로 생긴 공백 줄이면 true;
    private static ArrayList<Block> blockArrayList;//블럭을 가지고 있는 arraylist

    public Line(String input)
    {
        content = input;
        blockIndex = -1;
    }

    public Line(String input,int index, boolean whiteSpace)
    {
        content = input;
        blockIndex = index;
        isWhitespace = whiteSpace;
    }

    public String getLine(boolean isLastLine) //마지막 줄이면 개행을 없이 그것이 아니면 개행 있이 content를 반환합니다
    {
        if(isLastLine) return content;
        else return content + "\n";
    }

    public int getBlockIndex() { return  blockIndex; }
    public boolean getIsWhiteSpace() { return isWhitespace; }

    public LineHighlight getHighlight()
    {
        if(blockIndex == -1)
            this.lineStatus = LineHighlight.unHighlighted;

        else if(isWhitespace)
            this.lineStatus = LineHighlight.whitespace;

        else if(blockArrayList.get(blockIndex).getSelected())
            this.lineStatus = LineHighlight.selected;

        else
            this.lineStatus = LineHighlight.isDifferent;

        return this.lineStatus;
    }

    @Override
    public void setBlockArrayList(ArrayList<Block> blockArrayList) {

    }

    @Override
    public void clickBlock() {

    }

    @Override
    public String getContent(boolean isLastLine) {

        if(isLastLine) return content;
        else return content + "\n";
    }

    public static void setBlockArray(ArrayList<Block> inArrayList)
    {
        blockArrayList = inArrayList;
    }
    public static ArrayList<Block> getBlockArray() { return blockArrayList; }//only for testing

}
