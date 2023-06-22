package com.olegvbelov.budgetmanagement.mapper;

import com.olegvbelov.budgetmanagement.dto.BudgetDto;
import com.olegvbelov.core.mapper.BaseMapper;
import com.yandex.ydb.table.result.ResultSetReader;

public class BudgetMapper implements BaseMapper<BudgetDto> {
    @Override
    public BudgetDto mapToDto(ResultSetReader resultSetReader) {
        var result = new BudgetDto();
        result.setId(resultSetReader.getColumn("id").getUtf8());
        result.setUserId(resultSetReader.getColumn("userId").getUtf8());
        result.setCurrency(resultSetReader.getColumn("currency").getUtf8());
        result.setDateFormat(resultSetReader.getColumn("dateFormat").getUtf8());
        result.setDecimalDigits(resultSetReader.getColumn("decimalDigits").getInt16());
        result.setDecimalSeparator(resultSetReader.getColumn("decimalSeparator").getUtf8());
        result.setDefaultBudget(resultSetReader.getColumn("defaultBudget").getBool());
        result.setFirstMonth(resultSetReader.getColumn("firstMonth").getUtf8());
        result.setGroupSeparator(resultSetReader.getColumn("groupSeparator").getUtf8());
        result.setLastMonth(resultSetReader.getColumn("lastMonth").getUtf8());
        result.setName(resultSetReader.getColumn("name").getUtf8());
        result.setDeleted(resultSetReader.getColumn("isDeleted").getBool());
        result.setSymbolFirst(resultSetReader.getColumn("symbolFirst").getBool());
        return result;
    }
}
