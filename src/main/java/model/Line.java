package model;
import java.util.ArrayList;

/**
 * Created by ano on 2016. 5. 18..
 */
public class Line implements LineInterface {

    private final String content;//이 라인이 가지고 있는 컨텐츠
    private final int blockIndex; // 이 라인이 속해있는 블럭의 index. -1이면 속하는 블럭이 없다는 것
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
        LineHighlight lineStatus;
        if(blockIndex == -1 || blockArrayList == null)
            lineStatus = LineHighlight.unHighlighted;
        else try {
                if (blockArrayList.get(blockIndex).getSelected())
                    lineStatus = LineHighlight.selected;

                else if(isWhitespace)
                lineStatus = LineHighlight.whitespace;
                else lineStatus = LineHighlight.isDifferent;
            }catch(ArrayIndexOutOfBoundsException e)
            {
                e.printStackTrace();
                System.out.print("Block index problem");
                lineStatus = LineHighlight.unHighlighted;
            }
        return lineStatus;
    }

    public static void setBlockArrayList(ArrayList<Block> bArrayList) {
        blockArrayList = bArrayList;
    }

    @Override
    public void clickBlock() {
        if(blockIndex >=  0 ) {
            System.out.println("Model Block Clikc test : " + blockIndex);
            blockArrayList.get(blockIndex).click();

            System.out.println("Model Blcok selected :" + blockArrayList.get(blockIndex).getSelected());
        }
    }

    @Override
    public String getContent(boolean isLastLine) {
        if(isLastLine) return content;
        else return content + "\n";
    }
    @Deprecated
    @Override
    public int[] getBlockRangeofLine() {
        int[] range = new int[2];
        range[0] = blockArrayList.get(blockIndex).startLineNum;
        range[1] = blockArrayList.get(blockIndex).endLineNum;
        return range;
    }

    public static ArrayList<Block> getBlockArray() { return blockArrayList; }//@@only for testing

}