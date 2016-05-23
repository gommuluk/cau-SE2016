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


}
