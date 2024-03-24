package persistence.metadata;

import jdbc.JdbcTemplate;

public class InFlightMetadataCollector {

    private final MetaModel metaModel;
    private final QueryMeta queryMeta;

    public InFlightMetadataCollector(final String basePackage,
                                     final JdbcTemplate jdbcTemplate) {
        this.queryMeta = new QueryMetaImpl();
        this.metaModel = new MetaModelImpl(basePackage, jdbcTemplate, queryMeta);
    }

    public MetaModel getMetaModel() {
        return metaModel;
    }

    public QueryMeta getQueryMeta() {
        return queryMeta;
    }
}
