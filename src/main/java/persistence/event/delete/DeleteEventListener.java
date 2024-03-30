package persistence.event.delete;

public interface DeleteEventListener {

    <T> void onDelete(DeleteEvent<T> event);

    void execute();
}
