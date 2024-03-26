package persistence.sql.entity.loader;

import domain.Order;
import domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.metadata.MetaModel;
import persistence.sql.db.H2Database;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EntityEagerLoaderTest extends H2Database {

    private Order order;
    private OrderItem jpaItem;

    private EntityEagerLoader entityEagerLoader;

    @BeforeEach
    void setUp() {
        MetaModel metaModel = inFlightMetadataCollector.getMetaModel();
        this.entityEagerLoader = metaModel.getEntityEagerLoader(Order.class);

        this.jpaItem = new OrderItem(1L, "만들면서 배우는 JPA", 1);
        this.order = new Order(1L, "nextstep 강의", List.of(jpaItem));

        entityManager.removeAll(Order.class);
        entityManager.removeAll(OrderItem.class);

        entityManager.persist(jpaItem);
        entityManager.persist(order);
    }

    @DisplayName("Eager 로딩을 통해, Order를 조회한다.")
    @Test
    void eagerSelectTest() {
        Order eagerOrder = entityEagerLoader.find(Order.class, 1L);

        assertThat(eagerOrder).isEqualTo(order);
    }

}
