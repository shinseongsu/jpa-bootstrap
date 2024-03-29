package persistence.event.delete;

public class DeleteEvent<T> {

    private final T entity;
    private final Class<T> clazz;

    public DeleteEvent(final T entity) {
        this.clazz = (Class<T>) entity.getClass();
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
