package persistence.event;

import persistence.action.ActionQueue;
import persistence.event.delete.DefaultDeleteEventListener;
import persistence.event.delete.DeleteEventListener;
import persistence.event.load.DefaultLoadEventListener;
import persistence.event.load.LoadEventListener;
import persistence.event.merge.DefaultMergeEventListener;
import persistence.event.merge.MergeEventListener;
import persistence.event.persist.DefaultPersisterEventListener;
import persistence.event.persist.PersisterEventListener;
import persistence.metadata.MetaModel;

public class EventListenerRegistry {

    private final ActionQueue actionqueue;
    private final LoadEventListener loadEventListener;
    private final MergeEventListener mergeEventListener;
    private final DeleteEventListener deleteEventListener;
    private final PersisterEventListener persisterEventListener;


    private EventListenerRegistry(final LoadEventListener loadEventListener,
                                  final MergeEventListener mergeEventListener,
                                  final DeleteEventListener deleteEventListener,
                                  final PersisterEventListener persisterEventListener,
                                  final ActionQueue actionQueue) {
        this.loadEventListener = loadEventListener;
        this.mergeEventListener = mergeEventListener;
        this.deleteEventListener = deleteEventListener;
        this.persisterEventListener = persisterEventListener;
        this.actionqueue = actionQueue;
    }

    public static EventListenerRegistry create(final MetaModel metaModel,
                                               final ActionQueue actionQueue) {
        return new EventListenerRegistry(
                new DefaultLoadEventListener(metaModel),
                new DefaultMergeEventListener(metaModel, actionQueue),
                new DefaultDeleteEventListener(metaModel, actionQueue),
                new DefaultPersisterEventListener(metaModel, actionQueue),
                actionQueue);
    }

    public LoadEventListener getLoadEventListener() {
        return loadEventListener;
    }

    public MergeEventListener getMergeEventListener() {
        return mergeEventListener;
    }

    public DeleteEventListener getDeleteEventListener() {
        return deleteEventListener;
    }

    public PersisterEventListener getPersisterEventListener() {
        return persisterEventListener;
    }

    public ActionQueue getActionQueue() {
        return actionqueue;
    }

    public void flush() {
        persisterEventListener.execute();
        mergeEventListener.execute();
        deleteEventListener.execute();
    }
}
