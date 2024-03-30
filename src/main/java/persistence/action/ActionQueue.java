package persistence.action;

public interface ActionQueue {

    void addInsertAction(InsertEntityAction<?> insertEntityAction);

    Object excuteWithReturnKey(InsertEntityAction<?> insertEntityAction);

    void addUpdateAction(UpdateEntityAction<?> updateEntityAction);

    void addDeleteAction(DeleteEntityAction<?> deleteEntityAction);

    void executeInsertAction();

    void executeUpdateAction();

    void executeDeleteAction();

    InsertEntityAction<?> pollInsertEntityAction();

    UpdateEntityAction<?> pollUpdateEntityAction();

    DeleteEntityAction<?> pollDeleteEntityAction();
}
