package persistence.metadata;

import persistence.sql.dml.query.builder.*;

public interface QueryMeta {

    SelectQueryBuilder getSelectQueryBuilder();

    EagerSelectQueryBuilder getEagerSelectQueryBuilder();

    InsertQueryBuilder getInsertQueryBuilder();

    DeleteQueryBuilder getDeleteQueryBuilder();

    UpdateQueryBuilder getUpdateQueryBuilder();
}
