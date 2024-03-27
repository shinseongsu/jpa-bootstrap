package persistence;

import database.DatabaseServer;
import database.H2;
import domain.LegacyPerson;
import jdbc.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.metadata.InFlightMetadataCollector;
import persistence.session.EntityManagerFactory;
import persistence.session.EntityManagerFactoryImpl;
import persistence.sql.ddl.query.builder.CreateQueryBuilder;
import persistence.sql.ddl.query.builder.DropQueryBuilder;
import persistence.sql.dialect.Dialect;
import persistence.sql.entity.EntityMappingTable;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Starting application...");
        try {
            final DatabaseServer server = new H2();
            server.start();

            final JdbcTemplate jdbcTemplate = new JdbcTemplate(server.getConnection());

            InFlightMetadataCollector inFlightMetadataCollector = new InFlightMetadataCollector("domain", jdbcTemplate);
            EntityManagerFactory entityManagerFactory = new EntityManagerFactoryImpl(server.getConnection(), inFlightMetadataCollector.getMetaModel());
            Dialect dialect = entityManagerFactory.getDialect();

            final EntityMappingTable entityMappingTable = EntityMappingTable.from(LegacyPerson.class);
            String createTableSql = CreateQueryBuilder.of(entityMappingTable, dialect).toSql();
            String dropTableSql = new DropQueryBuilder(entityMappingTable).toSql();

            logger.debug(createTableSql);
            logger.debug(dropTableSql);
            jdbcTemplate.execute(createTableSql);
            jdbcTemplate.execute(dropTableSql);

            server.stop();
        } catch (Exception e) {
            logger.error("Error occurred", e);
        } finally {
            logger.info("Application finished");
        }
    }
}
