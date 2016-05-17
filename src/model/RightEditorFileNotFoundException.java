package model;

/**
 * Created by Elliad on 2016-05-17.
 */
public class RightEditorFileNotFoundException extends Exception {
    private String msg;

    public RightEditorFileNotFoundException(String msg) {
        this.msg = msg;
    }
    @Override
    public String getMessage() {
        return msg;
    }
}
