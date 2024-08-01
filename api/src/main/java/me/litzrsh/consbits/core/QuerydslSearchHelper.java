package me.litzrsh.consbits.core;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface QuerydslSearchHelper {

    default BooleanExpression and(BooleanExpression exists, BooleanExpression next) {
        return exists == null ? next : exists.and(next);
    }

    default BooleanExpression or(BooleanExpression exists, BooleanExpression next) {
        return exists == null ? next : exists.or(next);
    }
}
