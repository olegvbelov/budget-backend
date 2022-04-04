package com.olegvbelov.categorymanagement.service;

import com.jsoniter.output.JsonStream;
import com.olegvbelov.categorymanagement.dto.CategoryDto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static java.util.Objects.isNull;

public class GetCategoryServlet extends HttpServlet {
    
    private final CategoryService categoryService = new CategoryServiceImpl();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String period = req.getParameter("period");
        String account = req.getParameter("account");
        System.out.println(period);
        System.out.println(account);
        
        List<CategoryDto> categories = categoryService.getCategoriesForPeriod(account, period);
        System.out.print("Record count = ");
        System.out.println(categories.size());
    
    
        if (isNull(categories) || categories.size() == 0) {
            String oldPeriod = getPrevPeriod(period);
            categories = categoryService.getCategoriesForPeriod(account, oldPeriod);
            if (isNull(categories) || categories.size() == 0) {
                categoryService.initBudget(account, period);
            } else {
                categoryService.createNewPeriod(categories, period);
            }
            categories = categoryService.getCategoriesForPeriod(account, period);
        }
        
        
        
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.print(JsonStream.serialize(categories));
        out.flush();
    }
    
    private String getPrevPeriod(String period) {
        final String[] periodParts = period.split("-");
        var year = Integer.parseInt(periodParts[0]);
        System.out.println(year);
        var month = Integer.parseInt(periodParts[1]);
        System.out.println(month);
        if (month == 1) {
            month = 12;
            --year;
        } else {
            --month;
        }
        return String.format("%d-%02d", year, month);
    }
    
    
}
