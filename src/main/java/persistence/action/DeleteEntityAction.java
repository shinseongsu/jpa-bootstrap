package persistence.action;

import persistence.sql.entity.persister.EntityPersister;

public class DeleteEntityAction<T> implements EntityAction {

    private EntityPersister entityPersister;
    private T entity;

    public DeleteEntityAction(final EntityPersister entityPersister, final T entity) {
        this.entityPersister = entityPersister;
        this.entity = entity;
    }

    @Override
    public void execute() {
        entityPersister.delete(entity);
    }
}
