package com.olegvbelov.categorymanagement.service;

import com.jsoniter.any.Any;
import com.olegvbelov.categorymanagement.dto.CategoryDto;
import com.olegvbelov.categorymanagement.mapper.CategoryMapper;
import com.olegvbelov.core.enumeration.QueryType;
import com.olegvbelov.core.service.AbstractBudgetService;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.values.PrimitiveValue;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.function.Supplier;

public class CategoryService extends AbstractBudgetService<CategoryDto, CategoryMapper> {
    private final CategoryMapper mapper = new CategoryMapper();

    public CategoryService() {
        super();
        Supplier<String> getById = () -> "DECLARE $id AS Utf8; "
                + "SELECT c.id AS id, c.budget_id AS budgetId, c.parent_id AS parentId, c.name AS name, c.sort AS sort,"
                + "c.created AS created, c.closed AS closed, c.hidden AS hidden\n"
                + "FROM category AS c\n"
                + "WHERE c.id = $id;";
        Supplier<String> deleteById = () -> "DECLARE $id AS String;"
                + "DECLARE $month AS Utf8;"
                + "UPDATE category\n"
                + "SET closed = $month\n"
                + "WHERE id = $id";
        Supplier<String> getByBudgetId = () -> "DECLARE $budgetId AS Utf8; "
                + "SELECT c.id AS id, c.budget_id AS budgetId, c.parent_id AS parentId, c.name AS name, c.sort AS sort,"
                + "c.created AS created, c.closed AS closed, c.hidden AS hidden\n"
                + "FROM category AS c\n"
                + "WHERE c.budget_id = $budgetId;";
        super.queryMap.put(QueryType.GETBYID, getById);
        super.queryMap.put(QueryType.DELETEBYID, deleteById);
        super.queryMap.put(QueryType.SELECTBYPARAMS, getByBudgetId);
    }

    @Override
    protected CategoryMapper getMapper() {
        return this.mapper;
    }


    @Override
    protected String getQueryForCreate() {
        return "DECLARE $id AS String;"
                + "DECLARE $budgetId AS String;"
                + "DECLARE $parentId AS String;"
                + "DECLARE $sort AS Uint64;"
                + "DECLARE $name AS String;"
                + "DECLARE $created AS String;"
                + "DECLARE $closed AS String;"
                + "DECLARE $createdAt AS DateTime;"
                + "DECLARE $updatedAt AS DateTime;"
                + "DECLARE $hidden AS Bool"
                + "UPSET INTO category (id, budget_id, parent_id, name, sort, created, closed, hidden, "
                + "created_at, updated_at)\n"
                + "VALUES ($id, $budgetId, $parentId, $name, $sort, $created, $closer, $hidden, "
                + "$createdAt, $updatedAt)";
    }

    @Override
    protected String getQueryForUpdate() {
        return "DECLARE $id AS String;"
                + "DECLARE $budgetId AS String;"
                + "DECLARE $parentId AS String;"
                + "DECLARE $sort AS Uint64;"
                + "DECLARE $name AS String;"
                + "DECLARE $created AS String;"
                + "DECLARE $closed AS String;"
                + "DECLARE $updatedAt AS DateTime;"
                + "DECLARE $hidden AS Bool"
                + "UPSET INTO category (id, budget_id, parent_id, name, sort, created, closed, hidden, "
                + "updated_at)\n"
                + "VALUES ($id, $budgetId, $parentId, $name, $sort, $created, $closed, $hidden, "
                + "$updatedAt)";
    }

    protected Params prepareParamsForSelect(Params params) {
        params.put("$month", PrimitiveValue
                .string(String.format("%4d-%02d", LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue())
                        .getBytes(StandardCharsets.UTF_8)));
        return params;
    }

    @Override
    protected Params prepareParamsForUpdate(Any any) {
        var params = Params.create();
        params.put("$id", PrimitiveValue.utf8(any.get("id").toString()))
                .put("$budgetId", PrimitiveValue.utf8(any.get("budgetId").toString()))
                .put("$parentId", PrimitiveValue.utf8(any.get("parentId").toString()))
                .put("$sort", PrimitiveValue.uint64(any.get("sort").toLong()))
                .put("$name", PrimitiveValue.utf8(any.get("name").toString()))
                .put("$created", PrimitiveValue.utf8(any.get("created").toString()))
                .put("$closed", PrimitiveValue.utf8(any.get("closed").toString()))
                .put("$hidden", PrimitiveValue.bool(any.get("hidden").toBoolean()))
                .put("$updatedAt", PrimitiveValue.datetime(LocalDateTime.now()));
        return params;
    }
}
