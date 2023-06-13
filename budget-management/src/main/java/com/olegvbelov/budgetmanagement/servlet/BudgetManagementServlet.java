package com.olegvbelov.budgetmanagement.servlet;

import com.google.common.base.Strings;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.olegvbelov.budgetmanagement.service.BudgetService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BudgetManagementServlet extends HttpServlet {
    private final BudgetService service = new BudgetService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        String userId = req.getParameter("userId");
        try (var out = resp.getWriter()) {
            if (Strings.isNullOrEmpty(id) && Strings.isNullOrEmpty(userId)) {
                resp.sendError(400);
                return;
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            String result;
            if (Strings.isNullOrEmpty(id)) {
                result = service.getAllForUser(userId);
            } else {
                result = service.getById(id);
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
        var id = req.getParameter("id");
        service.deleteById(id);
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
