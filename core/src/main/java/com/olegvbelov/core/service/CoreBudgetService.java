package com.olegvbelov.core.service;

import com.jsoniter.any.Any;
import com.olegvbelov.core.enumeration.QueryType;
import com.yandex.ydb.table.query.Params;

public interface CoreBudgetService {
    String getSelectQuery(Params paramsFromRequest, QueryType queryType);
    String create(Any any);
    String update(Any any);
    void deleteById(Params paramsFromRequest);
}
