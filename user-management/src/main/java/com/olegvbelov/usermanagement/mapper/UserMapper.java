package com.olegvbelov.usermanagement.mapper;

import com.olegvbelov.core.mapper.BaseMapper;
import com.olegvbelov.usermanagement.dto.UserDto;
import com.yandex.ydb.table.result.ResultSetReader;

import java.util.List;

public class UserMapper implements BaseMapper<UserDto> {
    @Override
    public UserDto mapToDto(ResultSetReader resultSetReader) {
        var result = new UserDto();
        result.setId(resultSetReader.getColumn("id").getUtf8());
        result.setFirstName(resultSetReader.getColumn("firstName").getUtf8());
        result.setLastName(resultSetReader.getColumn("lastName").getUtf8());
        result.setMiddleName(resultSetReader.getColumn("middleName").getUtf8());
        result.setDeleted(resultSetReader.getColumn("isDeleted").getBool());
        return result;
    }

    @Override
    public List<UserDto> mapList(ResultSetReader resultSetReader) {
        return null;
    }
}
