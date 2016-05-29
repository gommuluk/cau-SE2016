package controller;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileManagerInterface;

import java.io.File;

/**
 * Created by 효민 on 2016-05-20.
 */
public class FileSaveExplorer implements FileExplorer {

    /**
     * 저장용 file chooser를 생성
     * @return File
     * */
    @Override
    public File getDialog(Button btn, FileManagerInterface.SideOfEditor side) {
        Stage s = (Stage) btn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        if(side == FileManagerInterface.SideOfEditor.Left)
            fileChooser.setTitle("Save Left File");
        else
            fileChooser.setTitle("Save Right File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.java", "*.c", "*.cpp"));

        return fileChooser.showSaveDialog(s);
    }

}



