package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Created by SH on 2016-05-12.
 */
public class TestModel {

    public BooleanProperty isActive = new SimpleBooleanProperty(false);

    public TestModel(){

    }

    public void run(){

        new Thread(new Runnable() {

            @Override
            public void run() {
                int i = 0;

                while(true) {
                    if(i%2==0) isActive.set(false);
                    else isActive.set(true);
                    i++;

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

}
