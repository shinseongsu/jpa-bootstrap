package persistence.sql.entity.loader;

import domain.LazyOrder;
import domain.Order;
import domain.OrderItem;
import domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.metadata.MetaModel;
import persistence.sql.db.H2Database;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EntityLoaderImplTest extends H2Database {

    private Person person1;
    private Person person2;

    private Order insertOrder;
    private OrderItem jpaOrderItem;
    private LazyOrder lazyOrder;

    private EntityLoader personEntityLoader;
    private EntityLoader orderEntityLoader;

    @BeforeEach
    void setUp() {
        this.person1 = new Person(1L, "박재성", 10, "jason");
        this.person2 = new Person(2L, "이동규", 20, "cu");

        this.jpaOrderItem = new OrderItem(1L, "만들면서 배우는 JPA", 1);
        this.insertOrder = new Order(1L, "nextstep jpa 강의", List.of(jpaOrderItem));
        this.lazyOrder = new LazyOrder(1L, "만드면서 배우는 Spring", List.of(jpaOrderItem));

        MetaModel metaModel = inFlightMetadataCollector.getMetaModel();
        personEntityLoader = metaModel.getEntityLoader(Person.class);
        orderEntityLoader = metaModel.getEntityLoader(Order.class);

        entityManager.removeAll(Order.class);
        entityManager.removeAll(LazyOrder.class);
        entityManager.removeAll(OrderItem.class);
        entityManager.removeAll(Person.class);

        entityManager.persist(jpaOrderItem);
        entityManager.persist(insertOrder);
        entityManager.persist(lazyOrder);
        entityManager.persist(person1);
        entityManager.persist(person2);
    }

    @DisplayName("아이디가 있는 값을 조회후, 엔티티로 반환한다.")
    @Test
    void findByIdTest() {
        Person person = personEntityLoader.find(Person.class, person1.getId());

        assertThat(person).isEqualTo(person1);
    }

    @DisplayName("아이디값이 없을 시, 널로 반환한다.")
    @Test
    void isNotExistsFindById() {
        Person person = personEntityLoader.find(Person.class, 3);

        assertThat(person).isNull();
    }

    @DisplayName("엔티티에 매핑된 테이블 값을 모두 조회한다.")
    @Test
    void findAllTest() {
        List<Person> persons = personEntityLoader.findAll(Person.class);

        assertThat(persons).containsExactly(person1, person2);
    }

    @DisplayName("주문에서 연관된 데이터를 같이 조회한다.")
    @Test
    void eagerFind() {
        Order order = orderEntityLoader.find(Order.class, 1);

        assertThat(order).isEqualTo(insertOrder);
    }

}
