package com.olegvbelov.categorymanagement.servlet;

import com.google.common.base.Strings;
import com.olegvbelov.categorymanagement.service.CategoryFundService;
import com.olegvbelov.core.enumeration.QueryType;
import com.olegvbelov.core.util.BudgetUtils;
import com.olegvbelov.core.util.Constants;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.values.PrimitiveValue;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CategoryFundManagementServlet extends HttpServlet {

    private final CategoryFundService service = new CategoryFundService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        var id = BudgetUtils.extractPathVariable(req);
        if (id.length() < 32) {
            id = null;
        }
        var categoryId = req.getParameter("categoryId");
        var month = req.getParameter("month");
        try (var out = resp.getWriter()) {
            if (Strings.isNullOrEmpty(id) && Strings.isNullOrEmpty(categoryId)
            && Strings.isNullOrEmpty(month)) {
                resp.sendError(400);
                return;
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String result;
            if (Strings.isNullOrEmpty(id)) {
                result = service.getSelectQuery(Params.of("$categoryId", PrimitiveValue.utf8(categoryId),
                        "month", PrimitiveValue.utf8(month)), QueryType.SELECTBYPARAMS);
            } else {
                result = service.getSelectQuery(Params.of(Constants.PARAM_ID, PrimitiveValue.utf8(id)),
                        QueryType.GETBYID);
            }
            if (Strings.isNullOrEmpty(result)) {
                resp.sendError(404);
                return;
            }
            out.print(result);
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }
}
