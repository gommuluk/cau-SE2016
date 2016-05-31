package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.FileManager;
import model.FileManagerInterface;

/**
 * Created by 효민 on 2016-05-23.
 */
public abstract class Caution {

    protected abstract boolean get(FileManagerInterface.SideOfEditor side);

    private ButtonType buttonTypeSave = new ButtonType("저장");

    public enum CautionType {
        SaveChoice, SaveNotice, MergeNotice, AboutNotice, CompareNotice
    }

    public static boolean CautionFactory(CautionType cautionType, FileManagerInterface.SideOfEditor side) {
        Caution caution = null;
        switch (cautionType) {
            case SaveChoice :
                caution = new SaveChoiceWindow();
                break;
            case SaveNotice :
                caution = new SaveNoticeWindow();
                break;
            case MergeNotice:
                caution = new MergeNoticeWindow();
                break;
            case AboutNotice:
                caution = new AboutNoticeWindow();
                break;
            case CompareNotice:
                caution = new CompareNoticeWindow();
                break;
        }
        return caution.get(side);
    }
}

class SaveChoiceWindow extends Caution {

    private final ButtonType buttonTypeSave = new ButtonType("저장");
    private final ButtonType buttonTypeNotSave = new ButtonType("저장 안 함");

    @Override
    public boolean get(FileManagerInterface.SideOfEditor side) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (side == FileManagerInterface.SideOfEditor.Left)
            alert.setTitle("왼쪽 파일 저장");
        else
            alert.setTitle("오른쪽 파일 저장");

        alert.setHeaderText(null);

        if (FileManager.getFileManagerInterface().getFilePath(side) == null)
            alert.setContentText("새로운 파일로 저장하시겠습니까?");
        else
            alert.setContentText("변경 사항을 저장하시겠습니까?");

        alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeNotSave);

        return alert.showAndWait().get() == buttonTypeSave;
    }

}

class SaveNoticeWindow extends Caution {

    private final ButtonType buttonTypeClose = new ButtonType("닫기");
    @Override
    public boolean get(FileManagerInterface.SideOfEditor side) {


        Alert alert = new Alert(Alert.AlertType.ERROR);
        if(side == FileManagerInterface.SideOfEditor.Left)
            alert.setTitle("왼쪽 파일 저장");
        else alert.setTitle("오른쪽 파일 저장");
        alert.setHeaderText(null);
        if(side == FileManagerInterface.SideOfEditor.Left)
            alert.setContentText("Left side File - 저장에 실패하였습니다.");
        else
            alert.setContentText("Right side File - 저장에 실패하였습니다.");
        alert.getButtonTypes().setAll(buttonTypeClose);
        alert.showAndWait();
        return true;
    }
}

class MergeNoticeWindow extends Caution {

    @Override
    public boolean get(FileManagerInterface.SideOfEditor side) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simple Merge - 소공 2팀");
        alert.setHeaderText(null);
        alert.setContentText("복사에 실패하였습니다.");

        alert.showAndWait();
        return true;
    }
}

class AboutNoticeWindow extends Caution {

    @Override
    public boolean get(FileManagerInterface.SideOfEditor side) {
        AboutSceneDialog dialog = new AboutSceneController();
        dialog.showAndWait();
        return true;
    }
}

class CompareNoticeWindow extends Caution {


    @Override
    public boolean get(FileManagerInterface.SideOfEditor side) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simple Merge - 소공 2팀");
        alert.setHeaderText(null);
        alert.setContentText("Editing 중에는 파일을 비교할 수 없습니다.");

        alert.showAndWait();
        return true;
    }
}
