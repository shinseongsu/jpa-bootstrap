package persistence.metadata;

import persistence.sql.entity.collection.CollectionLoader;
import persistence.sql.entity.loader.EntityEagerLoader;
import persistence.sql.entity.loader.EntityLoader;
import persistence.sql.entity.persister.EntityPersister;

public interface MetaModel {

    EntityPersister getEntityPersister(Class<?> clazz);

    EntityLoader getEntityLoader(Class<?> clazz);

    CollectionLoader getCollectionLoader(Class<?> clazz);

    EntityEagerLoader getEntityEagerLoader(Class<?> clazz);
}
