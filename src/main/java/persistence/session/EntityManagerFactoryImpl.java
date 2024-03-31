package persistence.session;

import persistence.action.ActionQueue;
import persistence.action.ActionQueueImpl;
import persistence.event.EventListenerRegistry;
import persistence.metadata.MetaModel;
import persistence.session.exception.NotFoundEntityManagerException;
import persistence.sql.dialect.Dialect;
import persistence.sql.dialect.DialectResolutionInfo;
import persistence.sql.dialect.database.Database;
import persistence.sql.entity.manager.EntityManager;
import persistence.sql.entity.manager.EntityManagerImpl;

import java.sql.Connection;
import java.sql.SQLException;

public class EntityManagerFactoryImpl implements EntityManagerFactory {

    private final CurrentSessionContext currentSessionContext;
    private final MetaModel metaModel;
    private final Dialect dialect;

    public EntityManagerFactoryImpl(final Connection connection,
                                    final MetaModel metaModel) {
        this.metaModel = metaModel;
        this.currentSessionContext = new CurrentSessionContext();
        this.dialect = Database.from(getDialectResolutionInfo(connection)).getDialectSupplier().get();
    }

    private DialectResolutionInfo getDialectResolutionInfo(final Connection connection) {
        try {
            return new DialectResolutionInfo(connection.getMetaData());
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void create() {
        final ActionQueue actionQueue = new ActionQueueImpl();
        final EventListenerRegistry eventListenerRegistry = EventListenerRegistry.create(metaModel, actionQueue);
        final EntityManager entityManager = new EntityManagerImpl(metaModel, eventListenerRegistry);
        currentSessionContext.create(entityManager);
    }

    @Override
    public EntityManager openSession() {
        return currentSessionContext.openSession()
                .orElseThrow(NotFoundEntityManagerException::new);
    }

    @Override
    public void closeSession() {
        currentSessionContext.closeSession();
    }

    @Override
    public Dialect getDialect() {
        return this.dialect;
    }
}
