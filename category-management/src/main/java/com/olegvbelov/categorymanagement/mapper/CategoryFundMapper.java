package com.olegvbelov.categorymanagement.mapper;

import com.olegvbelov.categorymanagement.dto.CategoryFundDto;
import com.olegvbelov.core.mapper.BaseMapper;
import com.yandex.ydb.table.result.ResultSetReader;

import java.util.ArrayList;
import java.util.List;

public class CategoryFundMapper implements BaseMapper<CategoryFundDto> {
    @Override
    public CategoryFundDto mapToDto(ResultSetReader resultSetReader) {
        var result = new CategoryFundDto();
        result.setId(resultSetReader.getColumn("id").getUtf8());
        result.setCategoryId(resultSetReader.getColumn("categoryId").toString());
        result.setMonth(resultSetReader.getColumn("month").getUtf8());
        result.setBalance(resultSetReader.getColumn("balance").getDecimal().toBigDecimal());
        result.setUsed(resultSetReader.getColumn("used").getDecimal().toBigDecimal());
        result.setBudgeted(resultSetReader.getColumn("budgeted").getDecimal().toBigDecimal());
        return result;
    }

    public List<CategoryFundDto> mapList(ResultSetReader resultSetReader) {
        List<CategoryFundDto> result = new ArrayList<>();
        while (resultSetReader.next()) {
            result.add(mapToDto(resultSetReader));
        }
        return result;
    }
}
