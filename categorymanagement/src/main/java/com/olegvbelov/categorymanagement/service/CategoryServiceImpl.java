package com.olegvbelov.categorymanagement.service;

import com.olegvbelov.categorymanagement.dto.CategoryDto;
import com.yandex.ydb.table.Session;
import com.yandex.ydb.table.query.DataQuery;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.result.ResultSetReader;
import com.yandex.ydb.table.transaction.TxControl;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.yandex.ydb.table.values.PrimitiveValue.string;
import static com.yandex.ydb.table.values.PrimitiveValue.uint64;

public class CategoryServiceImpl implements CategoryService {
    private static final String EMPTY_STRING = "";
    private static final String ZERO_ID = "0";
    private final Session session;
    
    public CategoryServiceImpl() {
        DBConnector dbConnector = new DBConnector();
        this.session = dbConnector.connect();
    }
    
    @Override
    public List<CategoryDto> getCategoriesForPeriod(String account, String period) {
        
        DataQuery query = session.prepareDataQuery(
                        "DECLARE $period AS String;" +
                                "DECLARE $account AS String;" +
                                "SELECT c.id AS id, c.name AS name, c.parent_id AS parentId, \n" +
                                "c.create_period AS createPeriod, c.close_period AS closePeriod, \n" +
                                "c.sort_order AS sortOrder, cf.id AS fundsId, cf.period AS period, \n" +
                                "cf.allocated_funds AS allocated, cf.used_funds AS used, \n" +
                                "cf.available_funds AS available\n" +
                                "FROM category AS c\n" +
                                "INNER JOIN category_funds AS cf ON (cf.category_id = c.id)\n" +
                                "WHERE c.account = $account AND c.create_period <= $period \n" +
                                "AND (c.close_period = \"\" OR c.close_period <= $period) \n" +
                                "AND cf.period = $period\n" +
                                "ORDER BY sortOrder;")
                .join()
                .expect("query failed");
    
        TxControl txControl = TxControl.serializableRw().setCommitTx(true);
    
        ResultSetReader result = makeQuery(query, txControl, account, period);
    
        List<CategoryDto> categoryList = new ArrayList<>();
        while (result.next()) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(result.getColumn("id").getString(Charset.defaultCharset()));
            categoryDto.setName(result.getColumn("name").getString(Charset.defaultCharset()));
            categoryDto.setParentId(result.getColumn("parentId").getString(Charset.defaultCharset()));
            categoryDto.setCreatePeriod(result.getColumn("createPeriod").getString(Charset.defaultCharset()));
            categoryDto.setClosePeriod(result.getColumn("closePeriod").getString(Charset.defaultCharset()));
            categoryDto.setSortOrder(result.getColumn("sortOrder").getUint64());
            categoryDto.setFundsId(result.getColumn("fundsId").getString(Charset.defaultCharset()));
            categoryDto.setPeriod(result.getColumn("period").getString(Charset.defaultCharset()));
            categoryDto.setAllocatedFunds(new BigDecimal(result.getColumn("allocated")
                    .getString(Charset.defaultCharset())));
            categoryDto.setUsedFunds(new BigDecimal(result.getColumn("used").getString(Charset.defaultCharset())));
            categoryDto.setAvailableFunds(new BigDecimal(result.getColumn("available")
                    .getString(Charset.defaultCharset())));
            categoryList.add(categoryDto);
        }
        
        return categoryList;
    }
    
    @Override
    public void initBudget(String account, String period) {
        
        createAvailableFundsCategory(account, period);
        
        createCategory(account, period, EMPTY_STRING, "Кредитные карты", 10);
        
        String parentId = createCategory(account, period, EMPTY_STRING, "Счета", 20);
        createCategory(account, period, parentId, "Интернет", 30);
        createCategory(account, period, parentId, "Телефон", 40);
        createCategory(account, period, parentId, "Газ", 50);
        createCategory(account, period, parentId, "Квартплата", 60);
        createCategory(account, period, parentId, "Налоги, штрафы и комиссии", 70);
        createCategory(account, period, parentId, "Вода", 80);
        createCategory(account, period, parentId, "Горячая вода и отопление", 90);
        
        parentId = createCategory(account, period, EMPTY_STRING, "Необходимые платежи", 100);
        createCategory(account, period, parentId, "Продукты и другие покупки", 110);
        createCategory(account, period, parentId, "Обслуживание автомобиля", 120);
        createCategory(account, period, parentId, "Дети и родители", 130);
        createCategory(account, period, parentId, "Поездки в общественном транспорте", 140);
        createCategory(account, period, parentId, "Обустройство дома", 150);
        createCategory(account, period, parentId, "Страхование", 160);
        createCategory(account, period, parentId, "Медицина", 170);
        createCategory(account, period, parentId, "Одежда", 180);
        createCategory(account, period, parentId, "Подарки", 190);
        createCategory(account, period, parentId, "Расходы на компьютеры", 200);
        createCategory(account, period, parentId, "Непредвиденные расходы", 220);
    
        createCategory(account, period, EMPTY_STRING, "Долги и кредиты", 230);
        
        parentId = createCategory(account, period, EMPTY_STRING, "Накопления", 240);
        createCategory(account, period, parentId, "Отпуск", 250);
        
        createCategory(account, period, EMPTY_STRING, "Подписки", 260);
        
        parentId = createCategory(account, period, EMPTY_STRING, "Качество жизни", 270);
        createCategory(account, period, parentId, "Отдых", 280);
        createCategory(account, period, parentId, "Обеды вне дома", 290);
        createCategory(account, period, parentId, "Игры", 300);
        createCategory(account, period, parentId, "Книги и музыка", 310);
        createCategory(account, period, parentId, "Развлечения", 320);
        
    }
    
    private void createAvailableFundsCategory(String account, String period) {
        DataQuery query = session.prepareDataQuery(
                        "DECLARE $id AS String;" +
                                "DECLARE $parent_id AS String;" +
                                "DECLARE $account AS String;" +
                                "DECLARE $name AS String;" +
                                "DECLARE $create_period AS String;" +
                                "DECLARE $close_period AS String;" +
                                "DECLARE $sort_order AS Uint64;" +
                                "UPSERT INTO category (id, parent_id, account, name, create_period, close_period,\n" +
                                "sort_order)\n" +
                                "VALUES ($id, $parent_id, $account, $name, $create_period, $close_period, $sort_order);")
                .join()
                .expect("query failed");
        Params params = query.newParams()
                .put("$id", string(ZERO_ID.getBytes(StandardCharsets.UTF_8)))
                .put("$parent_id", string(EMPTY_STRING.getBytes(StandardCharsets.UTF_8)))
                .put("$account", string(account.getBytes(StandardCharsets.UTF_8)))
                .put("$name", string("Доступные средства".getBytes(StandardCharsets.UTF_8)))
                .put("$create_period", string(period.getBytes(StandardCharsets.UTF_8)))
                .put("$close_period", string(EMPTY_STRING.getBytes(StandardCharsets.UTF_8)))
                .put("$sort_order", uint64(0));
        query.execute(TxControl.serializableRw().setCommitTx(true), params)
                .join()
                .expect("query failed");
        
        createCategoryFunds(ZERO_ID, period, BigDecimal.ZERO);
    }
    
    private void createCategoryFunds(String categoryId, String period, BigDecimal availableFunds) {
        UUID uuid = UUID.randomUUID();
        DataQuery query = session.prepareDataQuery(
                "DECLARE $id AS String;" +
                        "DECLARE $category_id AS String;" +
                        "DECLARE $period AS String;" +
                        "DECLARE $allocated_funds AS String;" +
                        "DECLARE $used_funds AS String;" +
                        "DECLARE $available_funds AS String;" +
                        "INSERT INTO category_funds (id, category_id, period, allocated_funds," +
                        "used_funds, available_funds)\n" +
                        "VALUES ($id, $category_id, $period, $allocated_funds, $used_funds, $available_funds)")
                .join()
                .expect("query failed");
    
        Params params = query.newParams()
                .put("$id", string(uuid.toString().getBytes(StandardCharsets.UTF_8)))
                .put("$category_id", string(categoryId.getBytes(StandardCharsets.UTF_8)))
                .put("$period", string(period.getBytes(StandardCharsets.UTF_8)))
                .put("$allocated_funds", string(BigDecimal.ZERO.toString().getBytes(StandardCharsets.UTF_8)))
                .put("$used_funds", string(BigDecimal.ZERO.toString().getBytes(StandardCharsets.UTF_8)))
                .put("$available_funds", string(availableFunds.toString().getBytes(StandardCharsets.UTF_8)));
        
        query.execute(TxControl.serializableRw().setCommitTx(true), params)
                .join()
                .expect("query failed");
    }
    
    @Override
    public void createNewPeriod(List<CategoryDto> categories, String period) {
        for (CategoryDto category : categories) {
            createCategoryFunds(category.getId(), period, category.getAvailableFunds());
        }
    }
    
    private ResultSetReader makeQuery(DataQuery query, TxControl txControl, String account, String period) {
        Params params = query.newParams()
                .put("$account", string(account.getBytes(StandardCharsets.UTF_8)))
                .put("$period", string(period.getBytes(StandardCharsets.UTF_8)));
        return query.execute(txControl, params)
                .join()
                .expect("query failed")
                .getResultSet(0);
    }
    
    public String createCategory(String account, String period, String parentId, String name, long sortOrder) {
        String uuid = UUID.randomUUID().toString();
        DataQuery query = session.prepareDataQuery(
                        "DECLARE $id AS String;" +
                                "DECLARE $parent_id AS String;" +
                                "DECLARE $account AS String;" +
                                "DECLARE $name AS String;" +
                                "DECLARE $create_period AS String;" +
                                "DECLARE $close_period AS String;" +
                                "DECLARE $sort_order AS Uint64;" +
                                "UPSERT INTO category (id, parent_id, account, name, create_period, close_period,\n" +
                                "sort_order)\n" +
                                "VALUES ($id, $parent_id, $account, $name, $create_period, $close_period, $sort_order);")
                .join()
                .expect("query failed");
    
        Params params = query.newParams()
                .put("$id", string(uuid.getBytes(StandardCharsets.UTF_8)))
                .put("$parent_id", string(parentId.getBytes(StandardCharsets.UTF_8)))
                .put("$account", string(account.getBytes(StandardCharsets.UTF_8)))
                .put("$name", string(name.getBytes(StandardCharsets.UTF_8)))
                .put("$create_period", string(period.getBytes(StandardCharsets.UTF_8)))
                .put("$close_period", string(EMPTY_STRING.getBytes(StandardCharsets.UTF_8)))
                .put("$sort_order", uint64(sortOrder));
    
        query.execute(TxControl.serializableRw().setCommitTx(true), params)
                .join()
                .expect("query failed");
        
        createCategoryFunds(uuid, period, BigDecimal.ZERO);
        return uuid;
    }
}
