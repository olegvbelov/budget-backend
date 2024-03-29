package com.olegvbelov.budgetmanagement.servlet;

import com.google.common.base.Strings;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.olegvbelov.budgetmanagement.service.BudgetService;
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

public class BudgetManagementServlet extends HttpServlet {
    private final BudgetService service = new BudgetService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var id = BudgetUtils.extractPathVariable(req);
        if (id.length() < 32) {
            id = null;
        }
        var userId = req.getParameter("userId");
        try (var out = resp.getWriter()) {
            if (ObjectUtils.isNullOrBlank(id) && ObjectUtils.isNullOrBlank(userId)) {
                resp.sendError(400);
                return;
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String result;
            if (ObjectUtils.isNullOrBlank(id)) {
                result = service.getSelectQuery(Params.of("$userId",
                        PrimitiveValue.utf8(userId)), QueryType.SELECTBYPARAMS);
            } else {
                result = service.getSelectQuery(Params.of("$id",
                        PrimitiveValue.utf8(id)), QueryType.GETBYID);
            }
            if (Strings.isNullOrEmpty(result)) {
                resp.sendError(404);
                return;
            }
            out.print(result);
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var id = BudgetUtils.extractPathVariable(req);
        service.deleteById(Params.of(Constants.PARAM_ID, PrimitiveValue.utf8(id)));
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (var inputStream = req.getInputStream(); PrintWriter out = resp.getWriter()) {
            Any any = JsonIterator.deserialize(inputStream.readAllBytes());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(service.create(any));
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
}
