package com.olegvbelov.usermanagement.servlet;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.olegvbelov.usermanagement.dto.UserDto;
import com.olegvbelov.usermanagement.service.UserService;
import com.olegvbelov.usermanagement.service.UserServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserManagementServlet extends HttpServlet {

    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        try (PrintWriter out = resp.getWriter()) {
            var userDto = service.getUserById(id);
            if (userDto.getId() == null) {
                resp.sendError(404);
                return;
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(JsonStream.serialize(userDto));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        makeRequest(req, resp);
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        makeRequest(req, resp);
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        service.deleteUserById(id);
    }
    
    private void makeRequest(HttpServletRequest req, HttpServletResponse resp) {
        UserDto userDto = new UserDto();
        try (var inputStream = req.getInputStream()) {
            var bytes = inputStream.readAllBytes();
            Any any = JsonIterator.deserialize(bytes);
            userDto.setId(any.get("id").toString());
            userDto.setFirstName(any.get("firstName").toString());
            userDto.setLastName(any.get("lastName").toString());
            userDto.setMiddleName(any.get("middleName").toString());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        try (PrintWriter out = resp.getWriter()) {
            service.createOrUpdate(userDto);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(JsonStream.serialize(userDto));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
}
