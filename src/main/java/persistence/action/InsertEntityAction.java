package persistence.action;

import persistence.sql.entity.EntityMappingTable;
import persistence.sql.entity.model.PrimaryDomainType;
import persistence.sql.entity.persister.EntityPersister;

import java.util.Objects;

public class InsertEntityAction<T> implements EntityAction {

    private final EntityPersister entityPersister;
    private final T entity;
    private final boolean isPrimaryValue;

    public InsertEntityAction(final EntityPersister entityPersister,
                              final T entity,
                              final boolean isPrimaryValue) {
        this.entityPersister = entityPersister;
        this.entity = entity;
        this.isPrimaryValue = isPrimaryValue;

    }

    @Override
    public Object executeWithReturn() {
        if (!isPrimaryValue) {
            return entityPersister.insertWithPk(entity);
        }

        entityPersister.insert(entity);
        EntityMappingTable entityMappingTable  = EntityMappingTable.of(entity.getClass(), entity);
        PrimaryDomainType pkDomainTypes = entityMappingTable.getPkDomainTypes();
        return Long.valueOf(pkDomainTypes.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsertEntityAction<?> that = (InsertEntityAction<?>) o;
        return isPrimaryValue == that.isPrimaryValue && Objects.equals(entityPersister, that.entityPersister) && Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityPersister, entity, isPrimaryValue);
    }
}
