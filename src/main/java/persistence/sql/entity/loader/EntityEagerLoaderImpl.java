package persistence.sql.entity.loader;

import jakarta.persistence.FetchType;
import jdbc.JdbcTemplate;
import persistence.sql.dml.conditional.Criteria;
import persistence.sql.dml.conditional.Criterion;
import persistence.sql.dml.query.builder.EagerSelectQueryBuilder;
import persistence.sql.dml.query.clause.WhereClause;
import persistence.sql.entity.EntityMappingTable;
import persistence.sql.entity.model.PrimaryDomainType;

import java.util.Collections;

public class EntityEagerLoaderImpl implements EntityEagerLoader {

    private final JdbcTemplate jdbcTemplate;
    private final EntityLoaderMapper entityLoaderMapper;
    private final EagerSelectQueryBuilder eagerSelectQueryBuilder;

    public EntityEagerLoaderImpl(final JdbcTemplate jdbcTemplate,
                                 final EntityLoaderMapper entityLoaderMapper,
                                 final EagerSelectQueryBuilder eagerSelectQueryBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityLoaderMapper = entityLoaderMapper;
        this.eagerSelectQueryBuilder = eagerSelectQueryBuilder;
    }

    @Override
    public <T> T find(final Class<T> clazz, final Object id) {
        final EntityMappingTable entityMappingTable = EntityMappingTable.from(clazz);
        final PrimaryDomainType primaryDomainType = entityMappingTable.getPkDomainTypes();
        final Criterion criterion = Criterion.of(primaryDomainType.getAlias(entityMappingTable.getTable().getAlias()), id.toString());
        final Criteria criteria = Criteria.ofCriteria(Collections.singletonList(criterion));
        final WhereClause whereClause = new WhereClause(criteria);

        String sql = eagerSelectQueryBuilder.toSql(entityMappingTable, whereClause);

        return jdbcTemplate.queryForObject(sql, resultSet -> entityLoaderMapper.eagerMapper(clazz, resultSet));
    }
}
