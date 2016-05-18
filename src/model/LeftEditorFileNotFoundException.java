package model;

/**
 * Created by Elliad on 2016-05-17.
 */
public class LeftEditorFileNotFoundException extends Exception {
    private String msg;

    public LeftEditorFileNotFoundException(String msg) {
        this.msg = msg;
    }
    @Override
    public String getMessage() {
        return msg;
    }
}
