package persistence.metadata;

import persistence.sql.dml.query.builder.*;

public class QueryMetaImpl implements QueryMeta {

    private final SelectQueryBuilder selectQueryBuilder;
    private final EagerSelectQueryBuilder eagerSelectQueryBuilder;
    private final InsertQueryBuilder insertQueryBuilder;
    private final DeleteQueryBuilder deleteQueryBuilder;
    private final UpdateQueryBuilder updateQueryBuilder;

    public QueryMetaImpl() {
        this.selectQueryBuilder = SelectQueryBuilder.getInstance();
        this.eagerSelectQueryBuilder = EagerSelectQueryBuilder.getInstance();
        this.insertQueryBuilder = InsertQueryBuilder.getInstance();
        this.deleteQueryBuilder = DeleteQueryBuilder.getInstance();
        this.updateQueryBuilder = UpdateQueryBuilder.getInstance();
    }

    @Override
    public SelectQueryBuilder getSelectQueryBuilder() {
        return selectQueryBuilder;
    }

    @Override
    public EagerSelectQueryBuilder getEagerSelectQueryBuilder() {
        return eagerSelectQueryBuilder;
    }

    @Override
    public InsertQueryBuilder getInsertQueryBuilder() {
        return insertQueryBuilder;
    }

    @Override
    public DeleteQueryBuilder getDeleteQueryBuilder() {
        return deleteQueryBuilder;
    }

    @Override
    public UpdateQueryBuilder getUpdateQueryBuilder() {
        return updateQueryBuilder;
    }
}
