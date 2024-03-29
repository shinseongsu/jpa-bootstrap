package persistence.action;

public interface EntityAction {

    default void execute() {
        throw new RuntimeException("정의되지 않은 메소드입니다.");
    }

    default Object executeWithReturn() {
        throw new RuntimeException("정의되지 않은 메소드입니다.");
    }
}
