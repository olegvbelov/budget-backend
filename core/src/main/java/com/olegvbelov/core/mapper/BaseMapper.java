package com.olegvbelov.core.mapper;

import com.yandex.ydb.table.result.ResultSetReader;

public interface BaseMapper<Dto> {
    Dto mapToDto(ResultSetReader resultSetReader);
}
