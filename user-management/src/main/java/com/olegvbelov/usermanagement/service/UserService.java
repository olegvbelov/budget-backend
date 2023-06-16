package com.olegvbelov.usermanagement.service;

import com.jsoniter.any.Any;
import com.olegvbelov.core.service.AbstractBudgetService;
import com.olegvbelov.usermanagement.mapper.UserMapper;
import com.olegvbelov.usermanagement.dto.UserDto;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.values.PrimitiveValue;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

public class UserService extends AbstractBudgetService<UserDto, UserMapper> {
    private final UserMapper mapper = new UserMapper();

    @Override
    protected UserMapper getMapper() {
        return this.mapper;
    }

    public UserService() {
        super();
    }

//    @Override
//    protected String parse(ResultSetReader resultSetReader) {
//
//        if (!resultSetReader.next()) {
//            return null;
//        }
//        return JsonStream.serialize(mapper.mapToDto(resultSetReader));
//    }

    @Override
    protected String getQueryForGet() {
        return "DECLARE $id AS String;" +
                "SELECT u.id AS id, u.first_name AS firstName, u.last_name AS lastName, " +
                "u.middle_name AS middleName, u.is_deleted AS isDeleted, u.created_at AS createdAt, " +
                "u.updated_at AS updatedAt\n" +
                "FROM user AS u\n" +
                "WHERE u.id = $id and u.is_deleted = false;";
    }

    @Override
    protected String getQueryForDelete() {
        return "DECLARE $id AS String;" +
                "UPDATE user\n" +
                "SET is_deleted = true\n" +
                "WHERE id = $id and is_deleted = false;";
    }

    @Override
    protected String getQueryForCreate() {
        return "DECLARE $id AS String;" +
                "DECLARE $firstName AS String;" +
                "DECLARE $lastName AS String;" +
                "DECLARE $middleName AS String;" +
                "DECLARE $createdAt AS DateTime;" +
                "DECLARE $updatedAt AS DateTime;" +
                "DECLARE $isDeleted AS Bool;" +
                "UPSERT INTO user (id, first_name, last_name, middle_name, created_at, updated_at, is_deleted) " +
                "VALUES ($id, $firstName, $lastName, $middleName, $createdAt, $updatedAt, $isDeleted);";
    }

    @Override
    protected String getQueryForUpdate() {
        return "DECLARE $id AS String;" +
                "DECLARE $firstName AS String;" +
                "DECLARE $lastName AS String;" +
                "DECLARE $middleName AS String;" +
                "DECLARE $updatedAt AS DateTime;" +
                "DECLARE $isDeleted AS Bool;" +
                "UPSERT INTO user (id, first_name, last_name, middle_name, updated_at, is_deleted) " +
                "VALUES ($id, $firstName, $lastName, $middleName, $updatedAt, $isDeleted);";
    }

    @Override
    protected Params prepareParamsForUpdate(Any any) {
        var params = Params.create();
                params.put("$id", PrimitiveValue.string(any.get("id").toString()
                                .getBytes(Charset.defaultCharset())))
                .put("$firstName", PrimitiveValue.string(any.get("firstName").toString()
                        .getBytes(Charset.defaultCharset())))
                .put("$lastName", PrimitiveValue.string(any.get("lastName").toString()
                        .getBytes(Charset.defaultCharset())))
                .put("$middleName", PrimitiveValue.string(any.get("middleName").toString()
                        .getBytes(Charset.defaultCharset())))
                .put("$updatedAt", PrimitiveValue.datetime(LocalDateTime.now()))
                .put("$isDeleted", PrimitiveValue.bool(any.get("isDeleted").toBoolean()));
        return null;
    }
}
