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

    public Line(String input){
        content = input;
        blockIndex = -1;
    }

    public Line(String input,int index, boolean whiteSpace){
        content = input;
        blockIndex = index;
        isWhitespace = whiteSpace;
    }

    /**
     * 비교로인해 생긴 공백줄인지 아닌지를 반환한다
     * @return 공백이면 True, 아니면 False
     */
    @Override
    public boolean getIsWhiteSpace() { return isWhitespace; }
    /**
     * 이 라인이 어떤 하이라이트정보를 가지는지 반환한다
     * @return LineHighlight 정보
     */
    @Override
    public LineHighlight getHighlight(){
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
                lineStatus = LineHighlight.unHighlighted;
            }
        return lineStatus;
    }

    public static void setBlockArrayList(ArrayList<Block> bArrayList) {
        blockArrayList = bArrayList;
    }
    /**
     * line이 속해있는 블럭에 클릭 이벤트를 전달한다
     */
    @Override
    public void clickBlock() {
        if(blockIndex >=  0 ) {
            blockArrayList.get(blockIndex).click();
        }
    }
    /**
     * 라인의 content를 반환한다
     * @param isLastLine 마지막 라인인지의 여부 마지막줄이 아님 개행 추가 아니면 개행을 추가하지 않는다
     * @return line의  content에 개행을 알맞게 추가한 것
     */
    @Override
    public String getContent(boolean isLastLine) {
        if(isLastLine) return content;
        else return content + "\n";
    }


    public static ArrayList<Block> getBlockArray() { return blockArrayList; }//@@only for testing

    /**
     * 테스트용 메소드
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null) //
            return false;
        if (getClass() != obj.getClass()) return false;
        Line other = (Line) obj;
        if (!content.equals(other.content)) {
            return false;
        } else
            return blockIndex == other.blockIndex && isWhitespace == other.isWhitespace;
    }

    @Override
    public String toString() {
        return this.content;
    }
}
