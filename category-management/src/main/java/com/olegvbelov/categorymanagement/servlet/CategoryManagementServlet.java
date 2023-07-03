package com.olegvbelov.categorymanagement.servlet;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.olegvbelov.categorymanagement.service.CategoryService;
import com.olegvbelov.core.enumeration.QueryType;
import com.olegvbelov.core.util.BudgetUtils;
import com.olegvbelov.core.util.Constants;
import com.olegvbelov.core.util.ObjectUtils;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.values.PrimitiveValue;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CategoryManagementServlet extends HttpServlet {
    
    private final CategoryService service = new CategoryService();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var id = BudgetUtils.extractPathVariable(req);
        if (id.length() < 32) {
            id = null;
        }
        var budgetId = req.getParameter("budgetId");
        try (PrintWriter out = resp.getWriter()) {
            if (ObjectUtils.isNullOrBlank(id) && ObjectUtils.isNullOrBlank(budgetId)) {
                resp.sendError(400);
                return;
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            Params params = Params.create();
            QueryType queryType = QueryType.GETBYID;
            if (ObjectUtils.isNullOrBlank(id)) {
                params.put("$budgetId", PrimitiveValue.utf8(budgetId));
                queryType = QueryType.SELECTBYPARAMS;
            } else {
                params.put(Constants.PARAM_ID, PrimitiveValue.utf8(
                        new String(id.getBytes(Charset.defaultCharset()), StandardCharsets.UTF_8)));
            }
            var result = service.getSelectQuery(params, queryType);
            if (ObjectUtils.isNullOrBlank(result)) {
                resp.sendError(404);
                return;
            }

            out.print(result);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (var inputStream = req.getInputStream(); PrintWriter out = resp.getWriter()) {
            Any any = JsonIterator.deserialize(inputStream.readAllBytes());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            if (ValueType.OBJECT.equals(any.valueType())) {
                out.print(service.create(any));
            }
            if (ValueType.ARRAY.equals(any.valueType())) {
                service.bulkCreate(any);
            }
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try (var inputStream = req.getInputStream(); PrintWriter out = resp.getWriter()) {
            Any any = JsonIterator.deserialize(inputStream.readAllBytes());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(service.update(any));
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var idParam = Params.of("$id", PrimitiveValue.utf8(BudgetUtils.extractPathVariable(req)));
        service.deleteById(idParam);
    }
}
