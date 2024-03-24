package persistence.metadata;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentScannerTest {

    private ComponentScanner componentScanner;

    @BeforeEach
    void setUp() {
        this.componentScanner = new ComponentScanner();
    }

    @DisplayName("domain 패키지의 클래스를 스캔한다.")
    @Test
    void scanTest() {
        List<Class<?>> entityClasses = componentScanner.scan("domain");

        assertThat(entityClasses).containsExactly(Person.class,
                LegacyPerson.class,
                Order.class,
                User.class,
                OrderItem.class,
                LazyOrder.class);
    }

    @DisplayName("패키지에 Entity가 없을 경우 빈값을 반환한다.")
    @Test
    void isNotExsitsScanTest() {
        List<Class<?>> entityClasses = componentScanner.scan("database");

        assertThat(entityClasses).isEmpty();
    }


}
