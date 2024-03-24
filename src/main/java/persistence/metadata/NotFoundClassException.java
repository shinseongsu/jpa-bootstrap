package persistence.metadata;

public class NotFoundClassException extends RuntimeException {

    private static final String MESSAGE = "존재하지 않는 클래스 입니다.";

    public NotFoundClassException() {
        super(MESSAGE);
    }
}
