package controller;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileManagerInterface;

import java.io.File;

/**
 * Created by SH on 2016-05-21.
 */
public class FileLoadExplorer implements FileExplorer {

    /**
     * 열기(로드)용 file chooser를 생성
     * @return File
     */
    @Override
    public File getDialog(Button btn, FileManagerInterface.SideOfEditor side) {
        Stage s = (Stage) btn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = new File(".");
        fileChooser.setInitialDirectory(file);
        if (side == FileManagerInterface.SideOfEditor.Left)
        fileChooser.setTitle("Open Left File");
        else fileChooser.setTitle("Open Right File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.java", "*.c", "*.cpp"));

        return fileChooser.showOpenDialog(s);
    }

}
