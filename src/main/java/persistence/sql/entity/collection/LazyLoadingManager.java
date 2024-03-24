package persistence.sql.entity.collection;

import jakarta.persistence.FetchType;
import net.sf.cglib.proxy.Enhancer;
import persistence.sql.entity.EntityMappingTable;
import persistence.sql.entity.exception.InvalidProxyException;
import persistence.sql.entity.model.DomainType;
import persistence.sql.entity.proxy.LazyLoadingProxy;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class LazyLoadingManager {

    public LazyLoadingManager() {
    }

    public boolean isLazyLoading(final Class<?> clazz) {
        final EntityMappingTable entityMappingTable = EntityMappingTable.from(clazz);
        return entityMappingTable.hasFetchType(FetchType.LAZY);
    }

    public <T> T setLazyLoading(final T entity,
                                final CollectionPersister collectionPersister,
                                final CollectionLoader collectionLoader) {
        final EntityMappingTable entityMappingTable = EntityMappingTable.of(entity.getClass(), entity);
        List<DomainType> lazyLoadingDomainTypes = entityMappingTable.getDomainTypeWithLazyLoading();

        lazyLoadingDomainTypes
                .forEach(domainType -> {
                    Class<?> subEntityType = getCollectionClass(domainType.getField());
                    Object lazyProxy = Enhancer.create(
                            List.class,
                            new LazyLoadingProxy(
                                    collectionPersister,
                                    collectionLoader,
                                    subEntityType,
                                    domainType.getValue()));

                    setField(domainType.getField(), entity, lazyProxy);
                });

        return entity;
    }

    private void setField(Field field, Object entity, Object proxy) {
        try {
            field.set(entity, proxy);
        } catch (Exception e) {
            throw new InvalidProxyException();
        }
    }

    private Class<?> getCollectionClass(Field field) {
        Type type = field.getGenericType();

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();

            if (typeArguments != null && typeArguments.length > 0) {
                Type typeArgument = typeArguments[0];
                try {
                    return Class.forName(typeArgument.getTypeName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new IllegalArgumentException();
    }

}
