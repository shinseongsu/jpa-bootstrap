package persistence.session;

import persistence.sql.entity.manager.EntityManager;

import java.util.Optional;

public class CurrentSessionContext {

    private final ThreadLocal<EntityManager> entityManagerThreadPool;

    public CurrentSessionContext() {
        entityManagerThreadPool = new ThreadLocal<>();
    }

    public void create(final EntityManager entityManager) {
        entityManagerThreadPool.set(entityManager);
    }

    public Optional<EntityManager> openSession() {
        return Optional.ofNullable(entityManagerThreadPool.get());
    }

    public void closeSession() {
        entityManagerThreadPool.remove();
    }
}
