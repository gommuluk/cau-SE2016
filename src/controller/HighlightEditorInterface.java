package controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import model.FileManagerInterface;
import model.LineInterface;

import java.util.ArrayList;

/**
 * Created by SH on 2016-05-21.
 */
public interface HighlightEditorInterface {

    boolean isEditable();
    void setEditable(boolean value);

    void setText(FileManagerInterface.SideOfEditor side, String s);
    void setHighlightLines(ArrayList<LineInterface> lines);

    void setEditMode(boolean isEditMode);
    boolean isEditMode();

    String getText();
    Parent getParent();
    Scene getScene();

    TextArea getTextArea();
    ListView getHighlightListView();

    void update(FileManagerInterface.SideOfEditor side);


}
