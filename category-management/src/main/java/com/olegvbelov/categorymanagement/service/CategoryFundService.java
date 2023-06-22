package com.olegvbelov.categorymanagement.service;

import com.jsoniter.any.Any;
import com.olegvbelov.categorymanagement.dto.CategoryFundDto;
import com.olegvbelov.categorymanagement.mapper.CategoryFundMapper;
import com.olegvbelov.core.enumeration.QueryType;
import com.olegvbelov.core.service.AbstractBudgetService;
import com.yandex.ydb.table.query.Params;

import java.util.function.Supplier;

public class CategoryFundService extends AbstractBudgetService<CategoryFundDto, CategoryFundMapper> {

    private final CategoryFundMapper mapper = new CategoryFundMapper();

    public CategoryFundService() {
        super();
        Supplier<String> getById = () -> "DECLARE $id AS String;"
                + "SELECT cf.id AS id, cf.category_id AS categoryId, cf.month AS month, cf.balance AS balance, "
                + "cf.used AS used, cf.budgeted AS budgeted, c hidden AS hidden\n"
                + "FROM category_fund AS cf\n"
                + "WHERE cf.id = $id;";
        super.queryMap.put(QueryType.GETBYID, getById);
    }
    @Override
    protected CategoryFundMapper getMapper() {
        return this.mapper;
    }

    @Override
    protected String getQueryForCreate() {
        return null;
    }

    @Override
    protected String getQueryForUpdate() {
        return null;
    }

    @Override
    protected Params prepareParamsForUpdate(Any any) {
        return null;
    }
}
