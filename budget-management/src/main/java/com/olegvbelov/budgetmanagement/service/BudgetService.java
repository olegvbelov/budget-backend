package com.olegvbelov.budgetmanagement.service;

import com.jsoniter.any.Any;
import com.olegvbelov.budgetmanagement.dto.BudgetDto;
import com.olegvbelov.budgetmanagement.mapper.BudgetMapper;
import com.olegvbelov.core.enumeration.QueryType;
import com.olegvbelov.core.service.AbstractBudgetService;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.values.PrimitiveValue;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class BudgetService extends AbstractBudgetService<BudgetDto, BudgetMapper> {
    private final BudgetMapper mapper = new BudgetMapper();

    @Override
    protected BudgetMapper getMapper() {
        return this.mapper;
    }

    public BudgetService() {

        super();
        Supplier<String> getById = () -> "DECLARE $id AS Utf8;"
                + "SELECT b.id AS id, b.currency AS currency, b.date_format AS dateFormat, "
                + "b.decimal_digits AS decimalDigits, b.decimal_separator AS decimalSeparator, "
                + "b.default_budget AS defaultBudget, b.first_month AS firstMonth, b.user_id AS userId, "
                + "b.group_separator AS groupSeparator, b.last_month AS lastMonth, b.name AS name, "
                + "b.is_deleted AS isDeleted, b.symbol_first AS symbolFirst, b.created_at AS createdAt, "
                + "b.updated_at AS updatedAt\n"
                + "FROM budget AS b\n"
                + "WHERE b.id = $id and b.is_deleted = false;";

        Supplier<String> deleteById = () -> "DECLARE $id AS Utf8;"
                + "UPDATE budget\n"
                + "SET is_delete = true\n"
                + "WHERE idd= $id AND is_delete = false;";
        Supplier<String> selectByUserId = () -> "DECLARE $userId AS Utf8;"
                + "SELECT b.id AS id, b.currency AS currency, b.date_format AS dateFormat, "
                + "b.decimal_digits AS decimalDigits, b.decimal_separator AS decimalSeparator, "
                + "b.default_budget AS defaultBudget, b.first_month AS firstMonth, b.user_id AS userId, "
                + "b.group_separator AS groupSeparator, b.last_month AS lastMonth, b.name AS name, "
                + "b.is_deleted AS isDeleted, b.symbol_first AS symbolFirst, b.created_at AS createdAt, "
                + "b.updated_at AS updatedAt\n"
                + "FROM budget AS b\n"
                + "WHERE b.user_id = $userId and b.is_deleted = false;";

        super.queryMap.put(QueryType.GETBYID, getById);
        super.queryMap.put(QueryType.DELETEBYID, deleteById);
        super.queryMap.put(QueryType.SELECTBYPARAMS, selectByUserId);
    }

    @Override
    protected String getQueryForCreate() {
        return "DECLARE $id AS Utf8;"
                + "DECLARE $userId AS Utf8;"
                + "DECLARE $currency AS Utf8;"
                + "DECLARE $dateFormat AS Utf8;"
                + "DECLARE $decimalDigits AS Int16;"
                + "DECLARE $decimalSeparator AS Utf8;"
                + "DECLARE $defaultBudget AS Bool;"
                + "DECLARE $firstMonth AS Utf8;"
                + "DECLARE $groupSeparator AS Utf8;"
                + "DECLARE $lastMonth AS Utf8;"
                + "DECLARE $name AS Utf8;"
                + "DECLARE $isDeleted AS Bool;"
                + "DECLARE $symbolFirst AS Bool;"
                + "DECLARE $createdAt AS DateTime;"
                + "DECLARE $updatedAt AS DateTime;"
                + "UPSERT INTO budget (id, user_id, currency, date_format, decimal_digits, decimal_separator, "
                + "default_budget, first_month, group_separator, last_month, name, is_deleted, "
                + "symbol_first, created_at, updated_at)\n"
                + "VALUES ($id, $userId, $currency, $dateFormat, $decimalDigits, $decimalSeparator, $defaultBudget, "
                + "$firstMonth, $groupSeparator, $lastMonth, $name, $isDeleted, $symbolFirst, $createdAt, $updatedAt);";
    }

    @Override
    protected String getQueryForUpdate() {
        return "DECLARE $id AS String;"
                + "DECLARE $userId AS Utf8;"
                + "DECLARE $currency AS Utf8;"
                + "DECLARE $dateFormat AS Utf8;"
                + "DECLARE $decimalDigits AS Int16;"
                + "DECLARE $decimalSeparator AS Utf8;"
                + "DECLARE $defaultBudget AS Bool;"
                + "DECLARE $firstMonth AS Utf8;"
                + "DECLARE $groupSeparator AS Utf8;"
                + "DECLARE $lastMonth AS Utf8;"
                + "DECLARE $name AS Utf8;"
                + "DECLARE $isDeleted AS Bool;"
                + "DECLARE $symbolFirst AS Bool;"
                + "DECLARE $updatedAt AS DateTime;"
                + "UPSERT INTO budget (id, user_id, currency, date_format, decimal_digits, decimal_separator, "
                + "default_budget, first_month, group_separator, last_month, name, is_deleted, created_at, updated_at)\n"
                + "VALUES ($id, $userId, $currency, $dateFormat, $decimalDigits, $decimalSeparator, $defaultBudget, "
                + "$firstMonth, $groupSeparator, $lastMonth, $name, $isDeleted, $symbolFirst, $updatedAt);";
    }


    @Override
    protected Params prepareParamsForUpdate(Any any) {
        var result = Params.create();
        result.put("$id", PrimitiveValue.utf8(any.get("id").toString()))
                .put("$userId", PrimitiveValue.utf8(any.get("userId").toString()))
                .put("$currency", PrimitiveValue.utf8(any.get("currency").toString()))
                .put("$dateFormat", PrimitiveValue.utf8(any.get("dateFormat").toString()))
                .put("$decimalDigits", PrimitiveValue.int16(any.get("decimalDigits").toBigDecimal().shortValue()))
                .put("$decimalSeparator", PrimitiveValue.utf8(any.get("decimalSeparator").toString()))
                .put("$defaultBudget", PrimitiveValue.bool(any.get("defaultBudget").toBoolean()))
                .put("$firstMonth", PrimitiveValue.utf8(any.get("firstMonth").toString()))
                .put("$groupSeparator", PrimitiveValue.utf8(any.get("groupSeparator").toString()))
                .put("$lastMonth", PrimitiveValue.utf8(any.get("lastMonth").toString()))
                .put("$name", PrimitiveValue.utf8(any.get("name").toString()))
                .put("$isDeleted", PrimitiveValue.bool(any.get("isDeleted").toBoolean()))
                .put("$symbolFirst", PrimitiveValue.bool(any.get("symbolFirst").toBoolean()))
                .put("$updatedAt", PrimitiveValue.datetime(LocalDateTime.now()));
        return result;
    }
}
