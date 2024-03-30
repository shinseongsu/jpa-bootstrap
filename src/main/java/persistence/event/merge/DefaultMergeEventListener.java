package persistence.event.merge;

import persistence.action.ActionQueue;
import persistence.action.UpdateEntityAction;
import persistence.metadata.MetaModel;

public class DefaultMergeEventListener implements MergeEventListener {

    private final MetaModel metaModel;
    private final ActionQueue actionQueue;

    public DefaultMergeEventListener(final MetaModel metaModel,
                                     final ActionQueue actionQueue) {
        this.metaModel = metaModel;
        this.actionQueue = actionQueue;
    }

    @Override
    public <T> void onMerge(MergeEvent<T> event) {
        actionQueue.addUpdateAction(
                new UpdateEntityAction<T>(
                        metaModel.getEntityPersister(event.getClazz()),
                        event.getEntity())
        );
    }

    @Override
    public void execute() {
        actionQueue.executeUpdateAction();
    }
}
