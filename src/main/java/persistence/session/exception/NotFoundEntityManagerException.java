package persistence.session.exception;

public class NotFoundEntityManagerException extends RuntimeException {

    private static final String MESSAGE = "EntityManager가 존재하지 않습니다.";

    public NotFoundEntityManagerException() {
        super(MESSAGE);
    }
}
