package persistence.sql.entity.loader;

public interface EntityEagerLoader {

    boolean isEagerFetchType(Class<?> clazz);

    <T> T find(Class<T> clazz, Object id);

}
