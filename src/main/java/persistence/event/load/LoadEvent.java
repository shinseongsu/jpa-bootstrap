package persistence.event.load;

public class LoadEvent<T> {

    private final Class<T> clazz;
    private final Object entityId;

    public LoadEvent(final Class<T> clazz, final Object entityId) {
        this.clazz = clazz;
        this.entityId = entityId;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public Object getEntityId() {
        return entityId;
    }
}
