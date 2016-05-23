package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by 효민 on 2016-05-23.
 */
public class SavingCaution {


    ButtonType buttonTypeSave = new ButtonType("저장");
    ButtonType buttonTypeNotSave = new ButtonType("저장 안 함");


    public ButtonType getSavebtn(){
        return buttonTypeSave;
    }


    public  Optional<ButtonType> getWindow(){

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Simple Merge - 소공 2팀");
        alert.setHeaderText(null);
        alert.setContentText("새로운 파일로 저장하시겠습니까?");

        alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeNotSave);

        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }


}
