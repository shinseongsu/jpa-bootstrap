package persistence.action;

import persistence.sql.entity.persister.EntityPersister;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateEntityAction<?> that = (UpdateEntityAction<?>) o;
        return Objects.equals(entityPersister, that.entityPersister) && Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityPersister, entity);
    }
}
