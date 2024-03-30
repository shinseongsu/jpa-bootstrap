package persistence.action;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ActionQueueImpl implements ActionQueue {

    private final Queue<InsertEntityAction<?>> insertEntityActionQueue;
    private final Queue<UpdateEntityAction<?>> updateEntityActionQueue;
    private final Queue<DeleteEntityAction<?>> deleteEntityActionQueue;

    public ActionQueueImpl() {
        this.insertEntityActionQueue = new ConcurrentLinkedQueue<>();
        this.updateEntityActionQueue = new ConcurrentLinkedQueue<>();
        this.deleteEntityActionQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void addInsertAction(final InsertEntityAction<?> insertEntityAction) {
        if(insertEntityActionQueue.contains(insertEntityAction)) {
            return;
        }
        insertEntityActionQueue.add(insertEntityAction);
    }

    @Override
    public Object excuteWithReturnKey(InsertEntityAction<?> insertEntityAction) {
        insertEntityActionQueue.add(insertEntityAction);
        return executeInsert();
    }

    @Override
    public void addUpdateAction(final UpdateEntityAction<?> updateEntityAction) {
        updateEntityActionQueue.add(updateEntityAction);
    }

    @Override
    public void addDeleteAction(final DeleteEntityAction<?> deleteEntityAction) {
        deleteEntityActionQueue.add(deleteEntityAction);
    }

    @Override
    public void allExecute() {
        executeInsert();
        executeUdpate();
        executeDelete();
    }

    @Override
    public InsertEntityAction<?> getInsertEntityAction() {
        if(insertEntityActionQueue.isEmpty()) {
            return null;
        }
        return insertEntityActionQueue.poll();
    }

    @Override
    public UpdateEntityAction<?> getUpdateEntityAction() {
        if(updateEntityActionQueue.isEmpty()) {
            return null;
        }
        return updateEntityActionQueue.poll();
    }

    @Override
    public DeleteEntityAction<?> getDeleteEntityAction() {
        if(deleteEntityActionQueue.isEmpty()) {
            return null;
        }
        return deleteEntityActionQueue.poll();
    }

    private Object executeInsert() {
        Object key = null;
        while(!insertEntityActionQueue.isEmpty()) {
            key = insertEntityActionQueue.poll().executeWithReturn();
        }
        return key;
    }

    private void executeUdpate() {
        while(!updateEntityActionQueue.isEmpty()) {
            updateEntityActionQueue.poll().execute();
        }
    }

    private void executeDelete() {
        while(!deleteEntityActionQueue.isEmpty()) {
            deleteEntityActionQueue.poll().execute();
        }
    }
}
