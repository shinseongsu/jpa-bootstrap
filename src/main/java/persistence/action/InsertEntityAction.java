package persistence.action;

import persistence.sql.entity.EntityMappingTable;
import persistence.sql.entity.model.PrimaryDomainType;
import persistence.sql.entity.persister.EntityPersister;

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
}
