package persistence.event.merge;

public class MergeEvent<T> {

    private final Class<T> clazz;
    private final T entity;

    public MergeEvent(final T entity) {
        this.clazz = (Class<T>) entity.getClass();
        this.entity = entity;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public T getEntity() {
        return entity;
    }
}
