package persistence.sql.dml.query.clause;

import persistence.sql.dml.conditional.Criteria;

import static persistence.sql.constant.SqlConstant.EMPTY;
import static persistence.sql.constant.SqlFormat.WHERE;

public class WhereClause {

    private final Criteria criteria;

    public WhereClause(final Criteria criteria) {
        this.criteria = criteria;
    }

    public String toSql() {
        String sql = criteria.toSql();

        if(isEmptySql(sql)) {
            return EMPTY.getValue();
        }

        return String.format(WHERE.getFormat(), criteria.toSql());
    }

    private boolean isEmptySql(final String sql) {
        return EMPTY.getValue().equals(sql);
    }

}
