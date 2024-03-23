package persistence.sql.entity.manager;

import jakarta.persistence.Id;
import persistence.metadata.MetaModel;
import persistence.sql.dml.exception.FieldSetValueException;
import persistence.sql.entity.EntityMappingTable;
import persistence.sql.entity.collection.CollectionPersister;
import persistence.sql.entity.collection.CollectionPersisterImpl;
import persistence.sql.entity.collection.LazyLoadingManager;
import persistence.sql.entity.context.PersistenceContext;
import persistence.sql.entity.context.PersistenceContextImpl;
import persistence.sql.entity.exception.ReadOnlyException;
import persistence.sql.entity.exception.RemoveEntityException;
import persistence.sql.entity.model.DomainType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityManagerImpl implements EntityManager {

    private final MetaModel metaModel;
    private final PersistenceContext persistenceContext;
    private final LazyLoadingManager lazyLoadingManager;
    private final CollectionPersister collectionPersister;

    public EntityManagerImpl(final MetaModel metaModel) {
        this.metaModel = metaModel;
        this.collectionPersister = new CollectionPersisterImpl();
        this.persistenceContext = new PersistenceContextImpl();
        this.lazyLoadingManager = new LazyLoadingManager();
    }

    @Override
    public <T> List<T> findAll(final Class<T> clazz) {
        return metaModel.getEntityLoader(clazz).findAll(clazz);
    }

    @Override
    public <T> T find(final Class<T> clazz, final Object id) {
        T persistenceEntity = persistenceContext.getEntity(clazz, id);
        if(persistenceEntity != null && persistenceContext.isGone(persistenceEntity)) {
            throw new RemoveEntityException();
        }

        if(persistenceEntity != null) {
            persistenceContext.loading(persistenceEntity, id);
            return persistenceEntity;
        }

        T entity = metaModel.getEntityLoader(clazz).find(clazz, id);
        if(lazyLoadingManager.isLazyLoading(clazz)) {
            entity = lazyLoadingManager.setLazyLoading(entity, collectionPersister, metaModel.getCollectionLoader(clazz));
        }

        if(entity != null) {
            insertEntityLoader(entity, id);
            persistenceContext.loading(entity, id);
        }
        return entity;
    }

    @Override
    public <T> T findOfReadOnly(Class<T> clazz, Object id) {
        T persistenceEntity = persistenceContext.getEntity(clazz, id);
        if(persistenceEntity != null) {
            persistenceContext.readOnly(persistenceEntity, id);
            return persistenceEntity;
        }

        T entity = metaModel.getEntityLoader(clazz).find(clazz, id);
        if(lazyLoadingManager.isLazyLoading(clazz)) {
            entity = lazyLoadingManager.setLazyLoading(entity, collectionPersister, metaModel.getCollectionLoader(clazz));
        }

        if(entity != null) {
            insertEntityLoader(entity, id);
            persistenceContext.readOnly(entity, id);
        }
        return entity;
    }

    @Override
    public void persist(final Object entity) {
        if(persistenceContext.isReadOnly(entity)) {
            throw new ReadOnlyException();
        }

        final EntityMappingTable entityMappingTable = EntityMappingTable.of(entity.getClass(), entity);
        final DomainType pkDomainType = entityMappingTable.getPkDomainTypes();
        final Object key = pkDomainType.getValue();

        final Object cacheEntity = persistenceContext.getEntity(entity.getClass(), key);
        if(cacheEntity == null) {
            insertEntity(entity);
        }

        final Object snapshotEntity = persistenceContext.getDatabaseSnapshot(entity.getClass(), key);
        if(key != null && !entity.equals(snapshotEntity)) {
            updateEntity(entity, key);
            persistenceContext.saving(entity);
        }
    }

    private void insertEntity(final Object entity) {
        Object newKey = metaModel.getEntityPersister(entity.getClass()).insertWithPk(entity);
        newInstance(entity, newKey);
        insertEntityLoader(entity, newKey);
    }

    private void updateEntity(final Object entity, final Object key) {
        metaModel.getEntityPersister(entity.getClass()).update(entity);
        insertEntityLoader(entity, key);
    }

    private void newInstance(Object entity, Object id) {
        Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .forEach(field -> setField(entity, field, id));
    }

    private void setField(final Object entity, final Field field, final Object id) {
        try {
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new FieldSetValueException();
        }
    }

    private void insertEntityLoader(final Object entity, final Object id) {
        if(entity != null) {
            persistenceContext.addEntity(entity, id);
        }
    }

    @Override
    public void remove(final Object entity) {
        if(persistenceContext.isReadOnly(entity)) {
            throw new ReadOnlyException();
        }

        persistenceContext.removeEntity(entity);
        metaModel.getEntityPersister(entity.getClass()).delete(entity);
        persistenceContext.goneEntity(entity);
    }

    @Override
    public void removeAll(final Class<?> clazz) {
        metaModel.getEntityPersister(clazz).deleteAll(clazz);
        persistenceContext.removeAll();
    }
}
