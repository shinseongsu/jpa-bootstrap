package persistence.session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.sql.db.H2Database;
import persistence.sql.entity.manager.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

class CurrentSessionContextTest extends H2Database {

    private CurrentSessionContext currentSessionContext;

    @BeforeEach
    void setUp() {
        this.currentSessionContext = new CurrentSessionContext();
    }

    @DisplayName("현재 쓰레드에서 사용할 EntityManager를 생성한다.")
    @Test
    void openSession() {
        currentSessionContext.create(entityManager);

        EntityManager sessionEntityManger = currentSessionContext.openSession()
                        .orElseThrow(() -> new RuntimeException("EntityManager가 존재하지 않습니다."));

        assertThat(sessionEntityManger).isEqualTo(entityManager);
    }

    @DisplayName("현재 쓰레드에서 사용하지않을떄는 제거할 수 있다.")
    @Test
    void closeSession() {
        currentSessionContext.create(entityManager);

        currentSessionContext.closeSession();

        assertThat(currentSessionContext.openSession()).isEmpty();
    }

}
