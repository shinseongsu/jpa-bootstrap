package persistence.sql.entity.loader;

public interface EntityEagerLoader {

    <T> T find(Class<T> clazz, Object id);

}
