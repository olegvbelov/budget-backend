package com.olegvbelov.usermanagement.service;

import com.olegvbelov.usermanagement.dto.UserDto;
import com.yandex.ydb.table.Session;
import com.yandex.ydb.table.query.DataQuery;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.settings.ExecuteDataQuerySettings;
import com.yandex.ydb.table.transaction.TxControl;
import com.yandex.ydb.table.values.PrimitiveValue;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;


public class UserServiceImpl implements UserService {
    private static final String QUERY_FAILED = "query failed";
    private final Session session;

    public UserServiceImpl() {
        DBConnector dbConnector = new DBConnector();
        this.session = dbConnector.connect();
    }

    @Override
    public UserDto getUserById(String id) {
        var userDto = new UserDto();
        var query = session.prepareDataQuery(
                "DECLARE $id AS String;" +
                        "SELECT u.id AS id, u.first_name AS firstName, u.last_name AS lastName, " +
                        "u.middle_name AS middleName, u.is_deleted AS isDeleted, u.created_at AS createdAt, " +
                        "u.updated_at AS updatedAt\n" +
                        "FROM user AS u\n" +
                        "WHERE u.id = $id and u.is_deleted = false;")
                .join()
                .expect(QUERY_FAILED);

        var params = query.newParams()
                .put("$id", PrimitiveValue.string(id.getBytes(Charset.defaultCharset())));
    
        return makeQueryAndGetResult(userDto, query, params);
        
    }
    
    private UserDto makeQueryAndGetResult(UserDto userDto, DataQuery query, Params params) {
        var txControl = TxControl.serializableRw().setCommitTx(true);
        var result = query.execute(txControl, params)
                .join()
                .expect(QUERY_FAILED)
                .getResultSet(0);
        if (!result.next()) {
            return new UserDto();
        }
        
        userDto.setId(result.getColumn("id").getString(Charset.defaultCharset()));
        userDto.setFirstName(result.getColumn("firstName").getString(Charset.defaultCharset()));
        userDto.setLastName(result.getColumn("lastName").getString(Charset.defaultCharset()));
        userDto.setMiddleName(result.getColumn("middleName").getString(Charset.defaultCharset()));
        userDto.setDeleted(result.getColumn("isDeleted").getBool());
        userDto.setCreatedAt(result.getColumn("createdAt").getDatetime());
        userDto.setUpdatedAt(result.getColumn("updatedAt").getDatetime());
        
        return userDto;
    }
    
    @Override
    public UserDto createOrUpdate(UserDto userDto) {
        if (userDto.getId() == null || "".equals(userDto.getId())) {
            userDto.setId(UUID.randomUUID().toString());
            userDto.setDeleted(false);
            userDto.setCreatedAt(LocalDateTime.now());
        }
        userDto.setUpdatedAt(LocalDateTime.now());
        var queryString = "";
        
        if (userDto.getCreatedAt() != null) {
            queryString = "DECLARE $id AS String;" +
                    "DECLARE $firstName AS String;" +
                    "DECLARE $lastName AS String;" +
                    "DECLARE $middleName AS String;" +
                    "DECLARE $createdAt AS DateTime;" +
                    "DECLARE $updatedAt AS DateTime;" +
                    "DECLARE $isDeleted AS Bool;" +
                    "UPSERT INTO user (id, first_name, last_name, middle_name, created_at, updated_at, is_deleted) " +
                    "VALUES ($id, $firstName, $lastName, $middleName, $createdAt, $updatedAt, $isDeleted);";
            
        }
        else {
            queryString = "DECLARE $id AS String;" +
                                    "DECLARE $firstName AS String;" +
                                    "DECLARE $lastName AS String;" +
                                    "DECLARE $middleName AS String;" +
                                    "DECLARE $updatedAt AS DateTime;" +
                                    "DECLARE $isDeleted AS Bool;" +
                                    "UPSERT INTO user (id, first_name, last_name, middle_name, updated_at, is_deleted) " +
                                    "VALUES ($id, $firstName, $lastName, $middleName, $updatedAt, $isDeleted);";
                    
        }
        var query = session.prepareDataQuery(queryString)
                .join()
                .expect(QUERY_FAILED);
        var params = query.newParams()
                .put("$id", PrimitiveValue.string(userDto.getId().getBytes(Charset.defaultCharset())))
                .put("$firstName", PrimitiveValue.string(userDto.getFirstName().getBytes(Charset.defaultCharset())))
                .put("$lastName", PrimitiveValue.string(userDto.getLastName().getBytes(Charset.defaultCharset())))
                .put("$middleName", PrimitiveValue.string(userDto.getMiddleName().getBytes(Charset.defaultCharset())))
                .put("$updatedAt", PrimitiveValue.datetime(userDto.getUpdatedAt().toInstant(ZoneOffset.UTC)))
                .put("$isDeleted", PrimitiveValue.bool(userDto.isDeleted()));
        
        if (userDto.getCreatedAt() != null) {
            params.put("$createdAt", PrimitiveValue.datetime(userDto.getCreatedAt().toInstant(ZoneOffset.UTC)));
        }
    
        var txControl = TxControl.serializableRw().setCommitTx(true);
        query.execute(txControl, params, new ExecuteDataQuerySettings())
                .join()
                .expect(QUERY_FAILED);
        return new UserDto();
    }
    
    @Override
    public void deleteUserById(String id) {
        var query = session.prepareDataQuery(
                        "DECLARE $id AS String;" +
                                "UPDATE user\n" +
                                "SET is_deleted = true\n" +
                                "WHERE id = $id and is_deleted = false;")
                .join()
                .expect(QUERY_FAILED);
    
        var params = query.newParams()
                .put("$id", PrimitiveValue.string(id.getBytes(Charset.defaultCharset())));
    
        var txControl = TxControl.serializableRw().setCommitTx(true);
        query.execute(txControl, params, new ExecuteDataQuerySettings())
                .join()
                .expect(QUERY_FAILED);
    }
}
