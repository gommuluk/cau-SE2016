package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Created by SH on 2016-05-12.
 */
public class TestModel {

    public BooleanProperty isActive = new SimpleBooleanProperty(false);

    public TestModel(){
        isActive.set(true);
    }

}
