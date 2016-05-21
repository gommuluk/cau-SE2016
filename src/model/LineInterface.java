package model;

import java.util.ArrayList;

/**
 * Created by ano on 2016. 5. 21..
 */
public interface LineInterface {

    enum LineHighlight
    {
        unHighlighted, whitespace, isDifferent, selected;
    }


    int getBlockIndex(); //이 Line이 속해있는 Block의 Index를 반환
    boolean getIsWhiteSpace();//이 줄이 비교로 인해 생긴 공백인지를 반환
    LineHighlight getHighlight(); //현재 이 줄이 어떤 하이라이트 정보를 가지는지 반환함

    void clickBlock();//이 라인이 속해있는 블럭을 클릭함.
    String getContent(boolean isLastLine);//이 라인의 컨텐츠인 String을 반환. LastLine인지 아닌지에 따라 다르게

}

