package xyz.dunshow.exception;

public class PageException extends BusinessException {
    private static final long serialVersionUID = 1L;

    public PageException(String code, String message) {
        super(code, message);
    }

    public PageException(String message) {
        super(message);
    }
}
