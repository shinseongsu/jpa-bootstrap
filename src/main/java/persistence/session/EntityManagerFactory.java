package persistence.session;

import persistence.action.ActionQueue;
import persistence.sql.dialect.Dialect;
import persistence.sql.entity.manager.EntityManager;

public interface EntityManagerFactory {

    void create();

    EntityManager openSession();

    void closeSession();

    Dialect getDialect();
}
