package com.olegvbelov.usermanagement.service;

import com.olegvbelov.usermanagement.dto.UserDto;
import com.yandex.ydb.table.Session;
import com.yandex.ydb.table.query.DataQuery;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.result.ResultSetReader;
import com.yandex.ydb.table.transaction.TxControl;
import com.yandex.ydb.table.values.PrimitiveValue;

import java.nio.charset.Charset;


public class UserServiceImpl implements UserService {

    private final Session session;

    public UserServiceImpl() {
        DBConnector dbConnector = new DBConnector();
        this.session = dbConnector.connect();
    }

    @Override
    public UserDto getUserById(String id) {
        var query = session.prepareDataQuery(
                "DECLARE $id AS String;" +
                        "SELECT u.id AS id, u.first_name AS firstName, u.last_name AS lastName, \n" +
                        "u.middle_name AS middleName\n" +
                        "FROM user AS u" +
                        "WHERE u.id = $id;")
                .join()
                .expect("query failed");
        var txControl = TxControl.serializableRw().setCommitTx(true);

        var params = query.newParams()
                .put("$id", PrimitiveValue.string(id.getBytes(Charset.defaultCharset())));
        var result = query.execute(txControl, params)
                .join()
                .expect("query failed")
                .getResultSet(0);
        var userDto = new UserDto();
        userDto.setId(result.getColumn("id").getString(Charset.defaultCharset()));
        userDto.setFirstName(result.getColumn("firstName").getString(Charset.defaultCharset()));
        userDto.setLastName(result.getColumn("lastName").getString(Charset.defaultCharset()));
        userDto.setMiddleName(result.getColumn("middleName").getString(Charset.defaultCharset()));
        return userDto;
    }
}
