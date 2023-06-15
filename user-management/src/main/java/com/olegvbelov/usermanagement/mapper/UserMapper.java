package com.olegvbelov.usermanagement.mapper;

import com.olegvbelov.core.mapper.BaseMapper;
import com.olegvbelov.usermanagement.dto.UserDto;
import com.yandex.ydb.table.result.ResultSetReader;

import java.nio.charset.Charset;

public class UserMapper implements BaseMapper<UserDto> {
    @Override
    public UserDto mapToDto(ResultSetReader resultSetReader) {
        var result = new UserDto();
        result.setId(resultSetReader.getColumn("id").getString(Charset.defaultCharset()));
        result.setFirstName(resultSetReader.getColumn("firstName").getString(Charset.defaultCharset()));
        result.setLastName(resultSetReader.getColumn("lastName").getString(Charset.defaultCharset()));
        result.setMiddleName(resultSetReader.getColumn("middleName").getString(Charset.defaultCharset()));
        result.setDeleted(resultSetReader.getColumn("isDeleted").getBool());
        return result;
    }
}
