package com.olegvbelov.usermanagement.service;

import com.jsoniter.any.Any;
import com.olegvbelov.core.enumeration.QueryType;
import com.olegvbelov.core.service.AbstractBudgetService;
import com.olegvbelov.usermanagement.dto.UserDto;
import com.olegvbelov.usermanagement.mapper.UserMapper;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.values.PrimitiveValue;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class UserService extends AbstractBudgetService<UserDto, UserMapper> {
    private final UserMapper mapper = new UserMapper();

    @Override
    protected UserMapper getMapper() {
        return this.mapper;
    }

    public UserService() {
        super();
        Supplier<String> getById = () -> "DECLARE $id AS Utf8;" +
                "SELECT u.id AS id, u.first_name AS firstName, u.last_name AS lastName, " +
                "u.middle_name AS middleName, u.is_deleted AS isDeleted, u.created_at AS createdAt, " +
                "u.updated_at AS updatedAt\n" +
                "FROM user AS u\n" +
                "WHERE u.id = $id and u.is_deleted = false;";
        Supplier<String> deleteById = () -> "DECLARE $id AS String;" +
                "UPDATE user\n" +
                "SET is_deleted = true\n" +
                "WHERE id = $id and is_deleted = false;";
        super.queryMap.put(QueryType.GETBYID, getById);
        super.queryMap.put(QueryType.DELETEBYID, deleteById);
    }


    @Override
    protected String getQueryForCreate() {
        return "DECLARE $id AS Utf8;" +
                "DECLARE $firstName AS Utf8;" +
                "DECLARE $lastName AS Utf8;" +
                "DECLARE $middleName AS Utf8;" +
                "DECLARE $createdAt AS DateTime;" +
                "DECLARE $updatedAt AS DateTime;" +
                "DECLARE $isDeleted AS Bool;" +
                "UPSERT INTO user (id, first_name, last_name, middle_name, created_at, updated_at, is_deleted) " +
                "VALUES ($id, $firstName, $lastName, $middleName, $createdAt, $updatedAt, $isDeleted);";
    }

    @Override
    protected String getQueryForUpdate() {
        return "DECLARE $id AS Utf8;" +
                "DECLARE $firstName AS Utf8;" +
                "DECLARE $lastName AS Utf8;" +
                "DECLARE $middleName AS Utf8;" +
                "DECLARE $updatedAt AS DateTime;" +
                "DECLARE $isDeleted AS Bool;" +
                "UPSERT INTO user (id, first_name, last_name, middle_name, updated_at, is_deleted) " +
                "VALUES ($id, $firstName, $lastName, $middleName, $updatedAt, $isDeleted);";
    }

    @Override
    protected Params prepareParamsForUpdate(Any any) {
        var params = Params.create();
                params.put("$id", PrimitiveValue.utf8(any.get("id").toString()))
                .put("$firstName", PrimitiveValue.utf8(any.get("firstName").toString()))
                .put("$lastName", PrimitiveValue.utf8(any.get("lastName").toString()))
                .put("$middleName", PrimitiveValue.utf8(any.get("middleName").toString()))
                .put("$updatedAt", PrimitiveValue.datetime(LocalDateTime.now()))
                .put("$isDeleted", PrimitiveValue.bool(any.get("isDeleted").toBoolean()));
        return params;
    }
}
