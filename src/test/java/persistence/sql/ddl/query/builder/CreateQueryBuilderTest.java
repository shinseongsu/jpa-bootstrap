package persistence.sql.ddl.query.builder;

import domain.LegacyPerson;
import domain.Order;
import domain.Person;
import domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.sql.db.H2Database;
import persistence.sql.dialect.Dialect;
import persistence.sql.dialect.DialectResolutionInfo;
import persistence.sql.dialect.database.Database;
import persistence.sql.entity.EntityMappingTable;

import static org.assertj.core.api.Assertions.assertThat;

class CreateQueryBuilderTest extends H2Database {

    private EntityMappingTable legacyPersonDomain;
    private EntityMappingTable personDomain;
    private EntityMappingTable userDomain;

    private Dialect dialect;

    @BeforeEach
    void setUp() throws Exception {
        DialectResolutionInfo dialectResolutionInfo = new DialectResolutionInfo(server.getConnection().getMetaData());
        dialect = Database.from(dialectResolutionInfo).getDialectSupplier().get();

        this.legacyPersonDomain = EntityMappingTable.from(LegacyPerson.class);
        this.personDomain = EntityMappingTable.from(Person.class);
        this.userDomain = EntityMappingTable.from(User.class);
    }

    @DisplayName("일반적인 Entity에서 테이블을 만드는 쿼리를 반환한다.")
    @Test
    void createQuery() {
        CreateQueryBuilder createQueryBuilder = CreateQueryBuilder.of(
                legacyPersonDomain,
                dialect
        );

        final String expected = "CREATE TABLE LegacyPerson(\n" +
                "id BIGINT PRIMARY KEY,\n" +
                "name VARCHAR,\n" +
                "age INTEGER\n" +
                ");";

        assertThat(createQueryBuilder.toSql()).isEqualTo(expected);
    }

    @DisplayName("컬럼 name으로 쿼리문을 반환한다.")
    @Test
    void createColumnNameSql() {
        CreateQueryBuilder createQueryBuilder = CreateQueryBuilder.of(
                personDomain,
                dialect
        );

        final String expected = "CREATE TABLE Person(\n" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT,\n" +
                "nick_name VARCHAR,\n" +
                "old INTEGER,\n" +
                "email VARCHAR  NOT NULL\n" +
                ");";

        assertThat(createQueryBuilder.toSql()).isEqualTo(expected);
    }

    @DisplayName("Table name으로 쿼리문 반환한다.")
    @Test
    void createTableNameSql() {
        CreateQueryBuilder createQueryBuilder = CreateQueryBuilder.of(
                userDomain,
                dialect
        );

        final String expected = "CREATE TABLE users(\n" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT,\n" +
                "nick_name VARCHAR,\n" +
                "old INTEGER,\n" +
                "email VARCHAR  NOT NULL\n" +
                ");";

        assertThat(createQueryBuilder.toSql()).isEqualTo(expected);
    }

    @DisplayName("외래키가 필요한 테이블일 경우 컬럼이 추가된다.")
    @Test
    void isExistForeKey() {
        EntityMappingTable entityMappingTable = EntityMappingTable.from(Order.class);

        CreateQueryBuilder createQueryBuilder = CreateQueryBuilder.of(
                entityMappingTable,
                dialect
        );

        assertThat(createQueryBuilder.toSql()).isEqualTo("CREATE TABLE orders(\n" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT,\n" +
                "orderNumber VARCHAR,\n" +
                "order_id BIGINT,\n" +
                "FOREIGN KEY (order_id) REFERENCES order_items(id)\n" +
                ");");
    }

}
