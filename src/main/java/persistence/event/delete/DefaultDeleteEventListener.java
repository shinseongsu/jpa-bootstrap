package persistence.event.delete;

import persistence.action.ActionQueue;
import persistence.action.DeleteEntityAction;
import persistence.metadata.MetaModel;

public class DefaultDeleteEventListener implements DeleteEventListener {

    private final MetaModel metaModel;
    private final ActionQueue actionQueue;

    public DefaultDeleteEventListener(final MetaModel metaModel,
                                      final ActionQueue actionQueue) {
        this.metaModel = metaModel;
        this.actionQueue = actionQueue;
    }

    @Override
    public <T> void onDelete(DeleteEvent<T> event) {
        actionQueue.addDeleteAction(
                new DeleteEntityAction<T>(
                        metaModel.getEntityPersister(event.getClazz()),
                        event.getEntity())
        );
    }

    @Override
    public void execute() {
        actionQueue.executeDeleteAction();
    }
}
