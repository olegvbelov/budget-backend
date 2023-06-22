package com.olegvbelov.core.mapper;

import com.yandex.ydb.table.result.ResultSetReader;

import java.util.ArrayList;
import java.util.List;

public interface BaseMapper<Dto> {
    Dto mapToDto(ResultSetReader resultSetReader);
    default  List<Dto> mapList(ResultSetReader resultSetReader){
        var result = new ArrayList<Dto>();
        while (resultSetReader.next()) {
            result.add(mapToDto(resultSetReader));
        }
        return result;
    }
}
