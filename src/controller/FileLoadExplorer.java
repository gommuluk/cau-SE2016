package controller;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    public File getDialog(Button btn) {
        Stage s = (Stage) btn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(s);
        return selectedFile;
    }

}
