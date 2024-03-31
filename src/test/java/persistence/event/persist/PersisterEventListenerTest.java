package persistence.event.persist;

import domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.action.ActionQueue;
import persistence.action.InsertEntityAction;
import persistence.metadata.MetaModel;
import persistence.sql.db.H2Database;

import static org.assertj.core.api.Assertions.assertThat;

class PersisterEventListenerTest extends H2Database {

    private PersisterEventListener persisterEventListener;
    private PersistEvent<Person> kakoPersistEvent;
    private PersistEvent<Person> naverPersistEvent;
    private Person existIdPerson;
    private Person notExistsIdPerson;
    private ActionQueue actionQueue;
    private MetaModel metaModel;

    @BeforeEach
    void setUp() {
        this.existIdPerson = new Person(1L, "kakao", 10, "@kakao.com");
        this.notExistsIdPerson = new Person(null, "naver", 11, "@naver.com");

        this.kakoPersistEvent = PersistEvent.createEvent(existIdPerson);
        this.naverPersistEvent = PersistEvent.createEvent(notExistsIdPerson);

        this.persisterEventListener = new DefaultPersisterEventListener(
                inFlightMetadataCollector.getMetaModel(),
                entityManager.getActionQueue()
        );

        entityManager.removeAll(Person.class);

        metaModel = inFlightMetadataCollector.getMetaModel();
        actionQueue = entityManager.getActionQueue();
    }

    @DisplayName("ActionQueue에 insert 될것이 쌓인다.")
    @Test
    void onPersisterTest() {
        persisterEventListener.onPersister(kakoPersistEvent);

        Person person = entityManager.find(Person.class, existIdPerson.getId());
        InsertEntityAction<?> insertEntityAction = actionQueue.pollInsertEntityAction();

        assertThat(person).isNull();

        assertThat(insertEntityAction).isEqualTo(new InsertEntityAction<>(metaModel.getEntityPersister(Person.class),
                existIdPerson,
                false));
    }

    @DisplayName("id값이 없는것이 들어왔을 경우에는 ActionQueue에 넣은후 바로 실행된다.")
    @Test
    void onPersisterWithPk() {
        Object key = persisterEventListener.onPersisterwithPk(naverPersistEvent);

        Person person = entityManager.find(Person.class, key);

        Person newPerson = new Person((Long) key, "naver", 11, "@naver.com");
        assertThat(person).isEqualTo(newPerson);

        assertThat(actionQueue.pollInsertEntityAction()).isNull();
    }

}
