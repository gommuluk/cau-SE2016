package controller;

import javafx.scene.control.Button;
import model.FileManagerInterface;

import java.io.File;

/**
 * Created by SH on 2016-05-21.
 */
public interface FileExplorer {

    File getDialog(Button btn, FileManagerInterface.SideOfEditor side);

}
