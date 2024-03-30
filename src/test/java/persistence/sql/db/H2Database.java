package persistence.sql.db;

import database.DatabaseServer;
import database.H2;
import domain.LazyOrder;
import domain.Order;
import domain.OrderItem;
import domain.Person;
import jdbc.JdbcTemplate;
import org.junit.jupiter.api.BeforeAll;
import persistence.metadata.InFlightMetadataCollector;
import persistence.session.EntityManagerFactory;
import persistence.session.EntityManagerFactoryImpl;
import persistence.sql.ddl.query.builder.CreateQueryBuilder;
import persistence.sql.ddl.query.builder.DropQueryBuilder;
import persistence.sql.dialect.Dialect;
import persistence.sql.dialect.DialectResolutionInfo;
import persistence.sql.dialect.database.Database;
import persistence.sql.entity.EntityMappingTable;
import persistence.sql.entity.manager.EntityManager;

import java.sql.SQLException;

public abstract class H2Database {

    private static final String BASE_PACKAGE = "domain";

    protected static EntityMappingTable entityMappingTable;
    protected static EntityMappingTable eagerEntityMappingTable;
    protected static EntityMappingTable lazyEntityMappingTable;

    protected static DatabaseServer server;

    protected static JdbcTemplate jdbcTemplate;

    protected static InFlightMetadataCollector inFlightMetadataCollector;
    protected static EntityManagerFactory entityManagerFactory;

    protected static EntityManager entityManager;

    @BeforeAll
    static void setUpAll() throws SQLException {
        server = new H2();
        jdbcTemplate = new JdbcTemplate(server.getConnection());

        inFlightMetadataCollector = new InFlightMetadataCollector(BASE_PACKAGE, jdbcTemplate);
        entityManagerFactory = new EntityManagerFactoryImpl(server.getConnection(), inFlightMetadataCollector.getMetaModel());
        entityManagerFactory.create();

        entityManager = entityManagerFactory.openSession();

        createTable();
    }

    private static void createTable() throws SQLException {
        DialectResolutionInfo dialectResolutionInfo = new DialectResolutionInfo(server.getConnection().getMetaData());
        Dialect dialect = Database.from(dialectResolutionInfo).getDialectSupplier().get();
        entityMappingTable = EntityMappingTable.from(Person.class);
        CreateQueryBuilder createQueryBuilder = CreateQueryBuilder.of(entityMappingTable, dialect);
        DropQueryBuilder dropQueryBuilder = new DropQueryBuilder(entityMappingTable);

        eagerEntityMappingTable = EntityMappingTable.from(Order.class);
        CreateQueryBuilder eagerCreateBuilder = CreateQueryBuilder.of(eagerEntityMappingTable, dialect);
        DropQueryBuilder eagerDropBuilder = new DropQueryBuilder(eagerEntityMappingTable);

        lazyEntityMappingTable = EntityMappingTable.from(LazyOrder.class);
        CreateQueryBuilder lazyCreateBuilder = CreateQueryBuilder.of(lazyEntityMappingTable, dialect);
        DropQueryBuilder lazyDropBuilder = new DropQueryBuilder(lazyEntityMappingTable);

        EntityMappingTable orderItemEntityMappingTable = EntityMappingTable.from(OrderItem.class);
        CreateQueryBuilder orderItemCreateBuilder = CreateQueryBuilder.of(orderItemEntityMappingTable, dialect);
        DropQueryBuilder orderItemDropBuilder = new DropQueryBuilder(orderItemEntityMappingTable);

        jdbcTemplate.execute(dropQueryBuilder.toSql());
        jdbcTemplate.execute(eagerDropBuilder.toSql());
        jdbcTemplate.execute(lazyDropBuilder.toSql());
        jdbcTemplate.execute(orderItemDropBuilder.toSql());

        jdbcTemplate.execute(createQueryBuilder.toSql());
        jdbcTemplate.execute(orderItemCreateBuilder.toSql());
        jdbcTemplate.execute(eagerCreateBuilder.toSql());
        jdbcTemplate.execute(lazyCreateBuilder.toSql());

        entityManager.flush();
    }
}
