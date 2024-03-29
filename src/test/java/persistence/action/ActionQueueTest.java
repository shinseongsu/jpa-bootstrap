package persistence.action;

import domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.metadata.MetaModel;
import persistence.sql.db.H2Database;

import static org.assertj.core.api.Assertions.assertThat;

class ActionQueueTest extends H2Database {

    private ActionQueue actionQueue;
    private MetaModel metaModel;

    private Person kakaoPerson;
    private Person naverPerson;

    @BeforeEach
    void setUp() {
        this.kakaoPerson = new Person(1L, "kakao", 10, "@kakao.com");
        this.naverPerson = new Person(null, "naver", 11, "@naver.com");

        metaModel = inFlightMetadataCollector.getMetaModel();
        actionQueue = entityManagerFactory.getActionQueue();

        actionQueue.allExecute();
        entityManager.removeAll(Person.class);
    }

    @DisplayName("insertQueue 아이디값이 있을경우에는 Queue에 추가한다.")
    @Test
    void addInsertAction() {
        InsertEntityAction<Person> personInsertEntityAction = new InsertEntityAction<>(metaModel.getEntityPersister(Person.class),
                kakaoPerson,
                false);

        actionQueue.addInsertAction(personInsertEntityAction);

        Person person = entityManager.find(Person.class, kakaoPerson.getId());

        assertThat(person).isNull();
    }

    @DisplayName("insertQueue 아이디값이 없을 경우에는 바로 반환이 된다.")
    @Test
    void addInsertWithReturnTest() {
        InsertEntityAction<Person> personInsertEntityAction = new InsertEntityAction<>(metaModel.getEntityPersister(Person.class),
                naverPerson,
                false);

        Object naverKey = actionQueue.excuteWithReturnKey(personInsertEntityAction);

        Person person = entityManager.find(Person.class, naverKey);
        Person newNaverPerson = new Person((Long) naverKey, "naver", 11, "@naver.com");

        assertThat(person).isEqualTo(newNaverPerson);
    }

    @DisplayName("update실행된 것들도 Queue에 저장이 되어 변경이 이루어 지지 않는다.")
    @Test
    void addUpdateActionTest() {
        entityManager.persist(kakaoPerson);
        actionQueue.allExecute();

        final Person person = new Person(kakaoPerson.getId(),
                "배민",
                19,
                "woowahan.com");

        final UpdateEntityAction<Person> updateEntityAction = new UpdateEntityAction<>(
                metaModel.getEntityPersister(Person.class),
                person);
        actionQueue.addUpdateAction(updateEntityAction);

        Person newPerson = entityManager.find(Person.class, kakaoPerson.getId());

        assertThat(newPerson).isEqualTo(kakaoPerson);
    }

    @DisplayName("Delete 쿼리도 Queue에 저장만 되어 실행이 되지 않는다.")
    @Test
    void deleteActionTest() {
        entityManager.persist(kakaoPerson);
        actionQueue.allExecute();

        final Person person = new Person(kakaoPerson.getId(),
                "배민",
                19,
                "woowahan.com");
        final DeleteEntityAction<Person> deleteEntityAction = new DeleteEntityAction<>(
                metaModel.getEntityPersister(Person.class),
                person
        );
        actionQueue.addDeleteAction(deleteEntityAction);

        Person newPerson = entityManager.find(Person.class, kakaoPerson.getId());
        assertThat(newPerson).isEqualTo(kakaoPerson);
    }


}