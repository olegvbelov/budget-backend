package com.olegvbelov.categorymanagement.mapper;

import com.olegvbelov.categorymanagement.dto.CategoryDto;
import com.olegvbelov.core.mapper.BaseMapper;
import com.yandex.ydb.table.result.ResultSetReader;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper implements BaseMapper<CategoryDto> {
    @Override
    public CategoryDto mapToDto(ResultSetReader resultSetReader) {
        var result = new CategoryDto();
        result.setId(resultSetReader.getColumn("id").getUtf8());
        result.setBudgetId(resultSetReader.getColumn("budgetId").getUtf8());
        result.setName(resultSetReader.getColumn("name").getUtf8());
        result.setParentId(resultSetReader.getColumn("parentId").getUtf8());
        result.setClosed(resultSetReader.getColumn("closed").getUtf8());
        result.setCreated(resultSetReader.getColumn("created").getUtf8());
        result.setSort(resultSetReader.getColumn("sort").getInt64());
        result.setHidden(resultSetReader.getColumn("hidden").getBool());
        return result;
    }

    @Override
    public List<CategoryDto> mapList(ResultSetReader resultSetReader) {
        List<CategoryDto> result = new ArrayList<>();
        while (resultSetReader.next()) {
            result.add(mapToDto(resultSetReader));
        }
        return result;
    }
}
