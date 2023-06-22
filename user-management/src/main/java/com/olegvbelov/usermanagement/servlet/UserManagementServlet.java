package com.olegvbelov.usermanagement.servlet;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.olegvbelov.core.enumeration.QueryType;
import com.olegvbelov.core.util.BudgetUtils;
import com.olegvbelov.core.util.Constants;
import com.olegvbelov.core.util.ObjectUtils;
import com.olegvbelov.usermanagement.service.UserService;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.values.PrimitiveValue;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserManagementServlet extends HttpServlet {

    private final UserService service = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var id = BudgetUtils.extractPathVariable(req);
        try (PrintWriter out = resp.getWriter()) {
            if (ObjectUtils.isNullOrBlank(id) || id.length() < 32) {
                resp.sendError(400);
                return;
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            var idParam = Params.of(Constants.PARAM_ID, PrimitiveValue.utf8(id));
            var result = service.getSelectQuery(idParam, QueryType.GETBYID);
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
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        var idParam = Params.of(Constants.QUERY_EXECUTE, PrimitiveValue.utf8(BudgetUtils.extractPathVariable(req)));
        service.deleteById(idParam);
    }
}
