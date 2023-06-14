package com.olegvbelov.core;

import com.jsoniter.any.Any;
import com.yandex.ydb.table.SessionRetryContext;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.result.ResultSetReader;
import com.yandex.ydb.table.transaction.TxControl;
import com.yandex.ydb.table.values.PrimitiveValue;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

public abstract class AbstractBudgetService implements CoreBudgetService {

    public final SessionRetryContext retryCtx;

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
                .expect("execute data query");

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

    protected abstract String parse(ResultSetReader resultSetReader);

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
