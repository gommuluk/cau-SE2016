package model;

import java.util.ArrayList;

/**
 * Created by ano on 2016. 5. 21..
 */
public interface LineInterface {

    enum Highlight//하이라이트 객체
    {
        unHilighted, whitespace, isDifferent,selected
    }
    int getBlockIndex();
    boolean getIsWhiteSpace();
    Highlight getHighlight();
    void setBlockArrayList(ArrayList<Block> blockArrayList);
    void clickBlock();
    String getContent();



}

