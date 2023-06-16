package com.olegvbelov.usermanagement.servlet;

import com.google.common.base.Strings;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.olegvbelov.core.util.BudgetUtils;
import com.olegvbelov.usermanagement.service.UserService;

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
            if (Strings.isNullOrEmpty(id)) {
                resp.sendError(400);
                return;
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            var result = service.getById(id);
            if (Strings.isNullOrEmpty(result)) {
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
        service.deleteById(BudgetUtils.extractPathVariable(req));
    }
}
