package com.olegvbelov.budgetmanagement.service;

import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.olegvbelov.budgetmanagement.dto.BudgetDto;
import com.olegvbelov.core.AbstractBudgetService;
import com.olegvbelov.core.Constants;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.result.ResultSetReader;
import com.yandex.ydb.table.transaction.TxControl;
import com.yandex.ydb.table.values.PrimitiveValue;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BudgetService extends AbstractBudgetService {

    public BudgetService() {
        super();
    }

    @Override
    protected String parse(ResultSetReader resultSetReader) {
        if (resultSetReader.getRowCount() == 1) {
            if (!resultSetReader.next()) {
                return null;
            }
            return JsonStream.serialize(mapToDto(resultSetReader));
        }
        List<BudgetDto> result = new ArrayList<>();
        while (resultSetReader.next()) {
            result.add(mapToDto(resultSetReader));
        }
        return JsonStream.serialize(result);
    }

    private BudgetDto mapToDto(ResultSetReader resultSetReader) {
        var result = new BudgetDto();
        result.setId(resultSetReader.getColumn("id").getString(Charset.defaultCharset()));
        result.setUserId(resultSetReader.getColumn("userId").getString(Charset.defaultCharset()));
        result.setCurrency(resultSetReader.getColumn("currency").getString(Charset.defaultCharset()));
        result.setDateFormat(resultSetReader.getColumn("dateFormat").getString(Charset.defaultCharset()));
        result.setDecimalDigits(resultSetReader.getColumn("decimalDigits").getInt32());
        result.setDecimalSeparator(resultSetReader.getColumn("decimalSeparator").getString(Charset.defaultCharset()));
        result.setDefaultBudget(resultSetReader.getColumn("defaultBudget").getBool());
        result.setFirstMonth(resultSetReader.getColumn("firstMonth").getString(Charset.defaultCharset()));
        result.setGroupSeparator(resultSetReader.getColumn("groupSeparator").getString(Charset.defaultCharset()));
        result.setLastMonth(resultSetReader.getColumn("lastMonth").getString(Charset.defaultCharset()));
        result.setName(resultSetReader.getColumn("name").getString(Charset.defaultCharset()));
        result.setDeleted(resultSetReader.getColumn("isDeleted").getBool());
        result.setSymbolFirst(resultSetReader.getColumn("symbolFirst").getBool());
        return result;
    }

    @Override
    protected String getQueryForGet() {
        return "DECLARE $id AS String;"
                + "SELECT b.id AS id, b.currency AS currency, b.date_format AS dateFormat, "
                + "b.decimal_digits AS decimalDigits, b.decimal_separator AS decimalSeparator, "
                + "b.default_budget AS defaultBudget, b.first_month AS firstMonth, b.user_id AS userId, "
                + "b.group_separator AS groupSeparator, b.last_month AS lastMonth, b.name AS name, "
                + "b.is_deleted AS isDeleted, b.symbol_first AS symbolFirst, b.created_at AS createdAt, "
                + "b.updated_at AS updatedAt\n"
                + "FROM budget AS b\n"
                + "WHERE b.id = $id and b.is_deleted = false;";
    }

    private String getQueryForGetByUserId() {
        return "DECLARE $userId AS String;"
                + "SELECT b.id AS id, b.currency AS currency, b.date_format AS dateFormat, "
                + "b.decimal_digits AS decimalDigits, b.decimal_separator AS decimalSeparator, "
                + "b.default_budget AS defaultBudget, b.first_month AS firstMonth, b.user_id AS userId, "
                + "b.group_separator AS groupSeparator, b.last_month AS lastMonth, b.name AS name, "
                + "b.is_deleted AS isDeleted, b.symbol_first AS symbolFirst, b.created_at AS createdAt, "
                + "b.updated_at AS updatedAt\n"
                + "FROM budget AS b\n"
                + "WHERE b.user_id = $userId and b.is_deleted = false;";
    }

    @Override
    protected String getQueryForDelete() {
        return  "DECLARE $id AS String;"
                + "UPDATE budget\n"
                + "SET is_delete = true\n"
                + "WHERE idd= $id AND is_delete = false;";
    }

    @Override
    protected String getQueryForCreate() {
        return "DECLARE $id AS String;"
                + "DECLARE $userId AS String;"
                + "DECLARE $currency AS String;"
                + "DECLARE $dateFormat AS String;"
                + "DECLARE $decimalDigits AS Integer;"
                + "DECLARE $decimalSeparator AS String;"
                    + "DECLARE $defaultBudget AS Bool;"
                + "DECLARE $firstMonth AS String;"
                + "DECLARE $groupSeparator AS String;"
                + "DECLARE $lastMonth AS String;"
                + "DECLARE $name AS String;"
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
                + "DECLARE $userId AS String;"
                + "DECLARE $currency AS String;"
                + "DECLARE $dateFormat AS String;"
                + "DECLARE $decimalDigits AS Integer;"
                + "DECLARE $decimalSeparator AS String;"
                + "DECLARE $defaultBudget AS Bool;"
                + "DECLARE $firstMonth AS String;"
                + "DECLARE $groupSeparator AS String;"
                + "DECLARE $lastMonth AS String;"
                + "DECLARE $name AS String;"
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
        result.put("$id", PrimitiveValue.string(any.get("id").toString().getBytes(Charset.defaultCharset())));
        result.put("$userId", PrimitiveValue.string(any.get("userId").toString().getBytes(Charset.defaultCharset())));
        result.put("$currency", PrimitiveValue.string(any.get("currency").toString()
                .getBytes(Charset.defaultCharset())));
        result.put("$dateFormat", PrimitiveValue.string(any.get("dateFormat").toString()
                .getBytes(Charset.defaultCharset())));
        result.put("$decimalDigits", PrimitiveValue.int32(any.get("decimalDigits").toInt()));
        result.put("$decimalSeparator", PrimitiveValue.string(any.get("decimalSeparator").toString()
                .getBytes(Charset.defaultCharset())));
        result.put("$defaultBudget", PrimitiveValue.bool(any.get("defaultBudget").toBoolean()));
        result.put("$firstMonth", PrimitiveValue.string(any.get("firstMonth").toString()
                .getBytes(Charset.defaultCharset())));
        result.put("$groupSeparator", PrimitiveValue.string(any.get("groupSeparator").toString()
                .getBytes(Charset.defaultCharset())));
        result.put("$lastMonth", PrimitiveValue.string(any.get("lastMonth").toString()
                .getBytes(Charset.defaultCharset())));
        result.put("$name", PrimitiveValue.string(any.get("name").toString().getBytes(Charset.defaultCharset())));
        result.put("$isDeleted", PrimitiveValue.bool(any.get("isDeleted").toBoolean()));
        result.put("$symbolFirst", PrimitiveValue.bool(any.get("symbolFirst").toBoolean()));
        result.put("$updatedAt", PrimitiveValue.datetime(LocalDateTime.now()));
        return result;
    }

    public String getAllForUser(String userId) {
        var txControl = TxControl.serializableRw().setCommitTx(true);
        Params params = Params.of(
                "$userId", PrimitiveValue.string(userId.getBytes(Charset.defaultCharset()))
        );
        var queryResult = retryCtx.supplyResult(session -> session.executeDataQuery(getQueryForGetByUserId(), txControl, params))
                .join().expect(Constants.QUERY_EXECUTE);

        var resultSet = queryResult.getResultSet(0);
        List<BudgetDto> result = new ArrayList<>();
        while(resultSet.next()) {
            result.add(mapToDto(resultSet));
        }
        return JsonStream.serialize(result);
    }
}
