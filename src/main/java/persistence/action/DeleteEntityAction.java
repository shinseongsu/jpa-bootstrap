package persistence.action;

import persistence.sql.entity.persister.EntityPersister;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteEntityAction<?> that = (DeleteEntityAction<?>) o;
        return Objects.equals(entityPersister, that.entityPersister) && Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityPersister, entity);
    }
}
