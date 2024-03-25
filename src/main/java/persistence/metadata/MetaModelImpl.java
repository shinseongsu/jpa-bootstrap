package persistence.metadata;

import jdbc.JdbcTemplate;
import persistence.sql.entity.collection.CollectionLoader;
import persistence.sql.entity.collection.CollectionLoaderImpl;
import persistence.sql.entity.loader.*;
import persistence.sql.entity.persister.EntityPersister;
import persistence.sql.entity.persister.EntityPersisterImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetaModelImpl implements MetaModel {

    private final Map<Class<?>, EntityPersister> entityPersisterMetaes = new ConcurrentHashMap<>();
    private final Map<Class<?>, EntityLoader> entityLoaderMetas = new ConcurrentHashMap<>();
    private final Map<Class<?>, CollectionLoader> collectionMetas = new ConcurrentHashMap<>();
    private final Map<Class<?>, EntityEagerLoader> entityEagerLoaderMetas = new ConcurrentHashMap<>();

    public MetaModelImpl(final String basePackage,
                         final JdbcTemplate jdbcTemplate,
                         final QueryMeta queryMeta) {
        final ComponentScanner componentScanner = new ComponentScanner();
        final EntityLoaderMapper entityLoaderMapper = EntityLoaderMapper.getInstance();

        componentScanner.scan(basePackage)
                .forEach(clazz -> {
                    entityPersisterMetaes.put(clazz, new EntityPersisterImpl(
                            jdbcTemplate,
                            queryMeta.getInsertQueryBuilder(),
                            queryMeta.getUpdateQueryBuilder(),
                            queryMeta.getDeleteQueryBuilder()
                    ));

                    entityLoaderMetas.put(clazz, new EntityLoaderImpl(
                            jdbcTemplate,
                            entityLoaderMapper,
                            queryMeta.getSelectQueryBuilder(),
                            queryMeta.getEagerSelectQueryBuilder()
                    ));

                    collectionMetas.put(clazz, new CollectionLoaderImpl(
                            entityLoaderMapper,
                            queryMeta.getSelectQueryBuilder(),
                            jdbcTemplate
                    ));

                    entityEagerLoaderMetas.put(clazz, new EntityEagerLoaderImpl(
                            jdbcTemplate,
                            entityLoaderMapper,
                            queryMeta.getEagerSelectQueryBuilder()
                    ));
                });
    }

    @Override
    public EntityPersister getEntityPersister(final Class<?> clazz) {
        return entityPersisterMetaes.get(clazz);
    }

    @Override
    public EntityLoader getEntityLoader(final Class<?> clazz) {
        return entityLoaderMetas.get(clazz);
    }

    @Override
    public CollectionLoader getCollectionLoader(Class<?> clazz) {
        return collectionMetas.get(clazz);
    }

    @Override
    public EntityEagerLoader getEntityEagerLoader(Class<?> clazz) {
        return entityEagerLoaderMetas.get(clazz);
    }
}
