package persistence.event.persist;

import persistence.sql.entity.EntityMappingTable;

public class PersistEvent<T> {

    private final T entity;
    private final Class<T> clazz;
    private final EntityMappingTable entityMappingTable;

    private PersistEvent(final T entity,
                        final Class<T> clazz,
                        final EntityMappingTable entityMappingTable) {
        this.entity = entity;
        this.clazz = clazz;
        this.entityMappingTable = entityMappingTable;
    }

    public static <T> PersistEvent<T> createEvent(final T entity) {
        Class<T> clazz = (Class<T>) entity.getClass();
        return new PersistEvent<>(
                entity,
                clazz,
                EntityMappingTable.of(clazz,entity)
        );
    }

    public T getEntity() {
        return entity;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public EntityMappingTable getEntityMappingTable() {
        return entityMappingTable;
    }
}
