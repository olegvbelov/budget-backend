package com.olegvbelov.core.service;

import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.olegvbelov.core.dto.BaseDto;
import com.olegvbelov.core.enumeration.QueryType;
import com.olegvbelov.core.mapper.BaseMapper;
import com.olegvbelov.core.util.Constants;
import com.yandex.ydb.table.SessionRetryContext;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.result.ResultSetReader;
import com.yandex.ydb.table.settings.BulkUpsertSettings;
import com.yandex.ydb.table.transaction.TxControl;
import com.yandex.ydb.table.values.ListValue;
import com.yandex.ydb.table.values.PrimitiveValue;
import com.yandex.ydb.table.values.StructType;
import com.yandex.ydb.table.values.Type;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractBudgetService<Dto extends BaseDto,
        Mapper extends BaseMapper<Dto>> implements CoreBudgetService {

    public final SessionRetryContext retryCtx;
    protected abstract Mapper getMapper();

    private final String database;
    protected EnumMap<QueryType, Supplier<String>> queryMap;

    protected Map<String, Type> mapForBulkCreate = new HashMap<>();
    private Supplier<StructType> prepareDataType = () -> StructType.of(mapForBulkCreate);

    protected String tableName = "";

    public AbstractBudgetService() {
        var dbConnector = new DBConnector();
        this.retryCtx = dbConnector.getContext();
        this.database = dbConnector.getDatabase();
        queryMap = new EnumMap<>(QueryType.class);
    }

    @Override
    public String getSelectQuery(Params paramsFromRequest, QueryType queryType) {
        return runSelectQuery(queryMap.get(queryType).get(), prepareParamsForSelect(paramsFromRequest));
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
            return JsonStream.serialize(List.of(getMapper().mapToDto(resultSetReader)));
        }
        return JsonStream.serialize(getMapper().mapList(resultSetReader));
    }

    public void deleteById(Params paramsFromRequest) {
        runSelectQuery(queryMap.get(QueryType.DELETEBYID).get(), prepareParamsForSelect(paramsFromRequest));
    }

    protected Params prepareParamsForSelect(Params params) {
        return params;
    }

    public String create(Any any) {
        var forParams = prepareParamsForUpdate(any);
        forParams.put("$createdAt", PrimitiveValue.datetime(LocalDateTime.now()));
        runUpdateQuery(getQueryForCreate(), forParams);
        var id = any.get("id").toString();
        return getSelectQuery(Params.of(Constants.PARAM_ID, PrimitiveValue.utf8(id)), QueryType.GETBYID);
    }

    public void bulkCreate(Any any) {
        var list = any.asList();
        ListValue queryData = prepareBulkData(list, prepareDataType);
        retryCtx.supplyStatus(session -> session.executeBulkUpsert(
                 this.database + tableName, queryData, new BulkUpsertSettings()
        )).join().expect(Constants.BULK_PROBLEM);
    }


    public String update(Any any) {
        runUpdateQuery(getQueryForUpdate(), prepareParamsForUpdate(any));
        var id = any.get("id").toString();
        return getSelectQuery(Params.of(Constants.PARAM_ID, PrimitiveValue.utf8(id)), QueryType.GETBYID);
    }

    protected abstract String getQueryForCreate();

    protected abstract String getQueryForUpdate();

    protected abstract Params prepareParamsForUpdate(Any any);

    protected ListValue prepareBulkData(List<Any> anyList, Supplier<StructType> structTypeSupplier) {
        return null;
    }
}
