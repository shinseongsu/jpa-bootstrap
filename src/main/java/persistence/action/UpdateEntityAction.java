package persistence.action;

import persistence.sql.entity.persister.EntityPersister;

public class UpdateEntityAction<T> implements EntityAction {

    private final EntityPersister entityPersister;
    private final T entity;


    public UpdateEntityAction(final EntityPersister entityPersister,
                              final T entity) {
        this.entityPersister = entityPersister;
        this.entity = entity;
    }

    @Override
    public void execute() {
        entityPersister.update(entity);
    }
}
