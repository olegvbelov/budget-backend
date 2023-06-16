package com.olegvbelov.budgetmanagement.mapper;

import com.olegvbelov.budgetmanagement.dto.BudgetDto;
import com.olegvbelov.core.mapper.BaseMapper;
import com.yandex.ydb.table.result.ResultSetReader;

import java.nio.charset.Charset;

public class BudgetMapper implements BaseMapper<BudgetDto> {
    @Override
    public BudgetDto mapToDto(ResultSetReader resultSetReader) {
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
}
