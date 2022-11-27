package com.olegvbelov.usermanagement.service;

import com.olegvbelov.usermanagement.dto.UserDto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetUserServlet extends HttpServlet {

    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        UserDto userDto = service.getUserById(id);
    }

}
