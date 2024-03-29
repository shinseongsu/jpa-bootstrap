package persistence.event.persist;

import domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.sql.db.H2Database;

import static org.assertj.core.api.Assertions.assertThat;

class PersisterEventListenerTest extends H2Database {

    private PersisterEventListener persisterEventListener;
    private PersistEvent<Person> kakoPersistEvent;
    private PersistEvent<Person> naverPersistEvent;
    private Person kakaoPerson;
    private Person naverPerson;

    @BeforeEach
    void setUp() {
        this.kakaoPerson = new Person(1L, "kakao", 10, "@kakao.com");
        this.naverPerson = new Person(null, "naver", 11, "@naver.com");

        this.kakoPersistEvent = PersistEvent.createEvent(kakaoPerson);
        this.naverPersistEvent = PersistEvent.createEvent(naverPerson);

        this.persisterEventListener = new DefaultPersisterEventListener(
                inFlightMetadataCollector.getMetaModel(),
                entityManagerFactory.getActionQueue()
        );

        entityManager.removeAll(Person.class);
    }

    @DisplayName("ActionQueue에 insert 될것이 쌓인다.")
    @Test
    void onPersisterTest() {
        persisterEventListener.onPersister(kakoPersistEvent);

        Person person = entityManager.find(Person.class, kakaoPerson.getId());

        assertThat(person).isNull();
    }

    @DisplayName("id값이 없는것이 들어왔을 경우에는 ActionQueue에 넣은후 바로 실행된다.")
    @Test
    void onPersisterWithPk() {
        Object key = persisterEventListener.onPersisterwithPk(naverPersistEvent);

        Person person = entityManager.find(Person.class, key);

        Person newPerson = new Person((Long) key, "naver", 11, "@naver.com");
        assertThat(person).isEqualTo(newPerson);
    }

}