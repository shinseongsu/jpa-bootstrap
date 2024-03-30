package persistence.event.persist;

import persistence.action.ActionQueue;
import persistence.action.InsertEntityAction;
import persistence.metadata.MetaModel;
import persistence.sql.entity.EntityMappingTable;
import persistence.sql.entity.model.PrimaryDomainType;

import java.util.Objects;

public class DefaultPersisterEventListener implements PersisterEventListener {

    private final MetaModel metaModel;
    private final ActionQueue actionQueue;

    public DefaultPersisterEventListener(final MetaModel metaModel,
                                         final ActionQueue actionQueue) {
        this.metaModel = metaModel;
        this.actionQueue = actionQueue;
    }

    @Override
    public <T> void onPersister(PersistEvent<T> event) {
        EntityMappingTable entityMappingTable = event.getEntityMappingTable();
        PrimaryDomainType pkDomainTypes = entityMappingTable.getPkDomainTypes();
        boolean isPrimaryValue = Objects.isNull(pkDomainTypes.getValue());

        actionQueue.addInsertAction(
                new InsertEntityAction<T>(
                        metaModel.getEntityPersister(event.getClazz()),
                        event.getEntity(),
                        isPrimaryValue)
        );
    }

    @Override
    public <T> Object onPersisterwithPk(PersistEvent<T> event) {
        return actionQueue.excuteWithReturnKey(
                new InsertEntityAction<T>(
                        metaModel.getEntityPersister(event.getClazz()),
                        event.getEntity(),
                        false)
        );
    }

    @Override
    public void execute() {
        actionQueue.executeInsertAction();
    }
}
