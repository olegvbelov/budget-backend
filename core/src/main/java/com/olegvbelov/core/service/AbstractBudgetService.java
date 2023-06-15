package com.olegvbelov.core.service;

import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.olegvbelov.core.dto.BaseDto;
import com.olegvbelov.core.mapper.BaseMapper;
import com.olegvbelov.core.util.Constants;
import com.yandex.ydb.table.SessionRetryContext;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.result.ResultSetReader;
import com.yandex.ydb.table.transaction.TxControl;
import com.yandex.ydb.table.values.PrimitiveValue;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBudgetService<Dto extends BaseDto,
        Mapper extends BaseMapper<Dto>> implements CoreBudgetService {

    public final SessionRetryContext retryCtx;
    protected abstract Mapper getMapper();

    public AbstractBudgetService() {
        var dbConnector = new DBConnector();
        this.retryCtx = dbConnector.getContext();
    }

    @Override
    public String getById(String id) {

        var params = Params.of("$id", PrimitiveValue.string(id.getBytes(Charset.defaultCharset())));
        return runSelectQuery(getQueryForGet(), params);
    }

    private String runSelectQuery(String query, Params params) {
        var txControl = TxControl.serializableRw().setCommitTx(true);
        var result = retryCtx.supplyResult(session -> session.executeDataQuery(query, txControl, params))
                .join()
                .expect(Constants.QUERY_EXECUTE);

        if (result.getResultSetCount() <= 0) {
            return null;
        }

        return parse(result.getResultSet(0));
    }

    private void runUpdateQuery(String query, Params params) {
        var txControl = TxControl.serializableRw().setCommitTx(true);
        retryCtx.supplyResult(session -> session.executeDataQuery(query, txControl, params))
                .join()
                .expect(Constants.QUERY_EXECUTE);
    }

    private String parse(ResultSetReader resultSetReader) {
        if (resultSetReader.getRowCount() == 1) {
            if (!resultSetReader.next()) {
                return null;
            }
            return JsonStream.serialize(getMapper().mapToDto(resultSetReader));
        }
        List<Dto> result = new ArrayList<>();
        while (resultSetReader.next()) {
            result.add(getMapper().mapToDto(resultSetReader));
        }
        return JsonStream.serialize(result);
    }

    protected abstract String getQueryForGet();

    public void deleteById(String id) {
        var params = Params.of("$id", PrimitiveValue.string(id.getBytes(Charset.defaultCharset())));
        runSelectQuery(getQueryForDelete(), params);
    }

    public String create(Any any) {
        var forParams = prepareParamsForUpdate(any);
        forParams.put("$createdAt", PrimitiveValue.datetime(LocalDateTime.now()));
        runUpdateQuery(getQueryForCreate(), forParams);
        var id = any.get("id").toString();
        return getById(id);
    }


    public String update(Any any) {
        runUpdateQuery(getQueryForUpdate(), prepareParamsForUpdate(any));
        var id = any.get("id").toString();
        return getById(id);
    }

    protected abstract String getQueryForDelete();

    protected abstract String getQueryForCreate();

    protected abstract String getQueryForUpdate();

    protected abstract Params prepareParamsForUpdate(Any any);
}
