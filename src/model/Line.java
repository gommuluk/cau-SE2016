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

    @Override
    public int getBlockIndex() { return  blockIndex; }
    @Override
    public boolean getIsWhiteSpace() { return isWhitespace; }
    @Override
    public LineHighlight getHighlight()
    {
        if(blockIndex == -1 || blockArrayList == null)
            this.lineStatus = LineHighlight.unHighlighted;

        else if(isWhitespace)
            this.lineStatus = LineHighlight.whitespace;

        else try {
                if (blockArrayList.get(blockIndex).getSelected())
                    this.lineStatus = LineHighlight.selected;

                else
                    this.lineStatus = LineHighlight.isDifferent;
            }catch(ArrayIndexOutOfBoundsException e)
            {
                e.printStackTrace();
                System.out.print("Block index problem");
                this.lineStatus = LineHighlight.unHighlighted;
            }
        return this.lineStatus;
    }

    public static void setBlockArrayList(ArrayList<Block> bArrayList) {
        blockArrayList = bArrayList;
    }

    @Override
    public void clickBlock() {
        blockArrayList.get(blockIndex).click();
    }

    @Override
    public String getContent(boolean isLastLine) {
        if(isLastLine) return content;
        else return content + "\n";
    }

    public static ArrayList<Block> getBlockArray() { return blockArrayList; }//@@only for testing

}
