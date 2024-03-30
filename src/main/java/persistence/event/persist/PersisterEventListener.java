package persistence.event.persist;

public interface PersisterEventListener {

    <T> void onPersister(PersistEvent<T> event);

    <T> Object onPersisterwithPk(PersistEvent<T> event);

    void execute();
}
