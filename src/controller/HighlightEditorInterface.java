package controller;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Created by SH on 2016-05-21.
 */
public interface HighlightEditorInterface {

    boolean isEditable();
    void setEditable(boolean value);

    void setText(String s);
    void setHighlightLines(int... lines);

    String getText();
    Parent getParent();
    Scene getScene();

}
