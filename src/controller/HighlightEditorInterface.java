package controller;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Created by SH on 2016-05-21.
 */
public interface HighlightEditorInterface {

    void setEditable(boolean value);
    void setText(String s);

    void setHighlightLines(int... lines);

    boolean isEditable();
    String getText();

    Parent getParent();
    Scene getScene();

}
